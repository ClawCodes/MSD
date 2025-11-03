package com.example.aug_reality

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.net.Uri
import android.util.Log
import android.view.Surface
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.CameraSelector.DEFAULT_FRONT_CAMERA
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.collectAsState
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.aug_reality.ml.Model
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import org.tensorflow.lite.support.image.TensorImage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors
import kotlin.math.abs

enum class Recording(val state: String) {
    Start("Start"),
    Stop("Stop")
}

enum class CVModel(val state: String) {
    Face("Face Detection"),
    Obj("Object Detection")
}

data class ContrastPoint(
    val rowIdx: Float,
    val colIdx: Float,
    val bufferWidth: Int,
    val bufferHeight: Int
)

@ExperimentalGetImage
class AugRealityVM : ViewModel() {
    private var cameraJob: Job? = null
    private val _recordingState = MutableStateFlow(Recording.Start)

    val recordingState: StateFlow<Recording> = _recordingState.asStateFlow()

    private val _modelInUse = MutableStateFlow(CVModel.Face)
    val modelInUse: StateFlow<CVModel> = _modelInUse.asStateFlow()

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest.asStateFlow()

    private val _contrastPoint = MutableStateFlow<ContrastPoint?>(null)
    val contrastPoint: StateFlow<ContrastPoint?> = _contrastPoint.asStateFlow()

    private val executor = Executors.newSingleThreadExecutor()

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.update { newSurfaceRequest }
        }
    }

    private val contrastAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build().apply {
            setAnalyzer(executor) { image: ImageProxy ->
                try {
                    setContrastPoint(image)
                } finally {
                    image.close()
                }
            }
        }

    // Image capture
    val imageCapture = ImageCapture.Builder()
        .setTargetRotation(Surface.ROTATION_0)
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()

    private var cameraSelector = DEFAULT_BACK_CAMERA

    private val faceDetector = FaceDetection.getClient(FaceDetectorOptions.Builder().apply {
        enableTracking() // tracks faces over time
        setExecutor(executor)
    }.build())

    private val _faces = MutableStateFlow<List<Face>>(emptyList())
    val faces: StateFlow<List<Face>> = _faces.asStateFlow()

    private val _analyzerToBuffer = MutableStateFlow(android.graphics.Matrix())
    val analyzerToBuffer: StateFlow<android.graphics.Matrix> = _analyzerToBuffer.asStateFlow()
    private val mlKitManualUseCase = ImageAnalysis.Builder().apply {
        //throw away any frames we can't handle in time
        setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
        setOutputImageRotationEnabled(false)
    }.build().apply {
        setAnalyzer(executor) { imageProxy ->

            imageProxy.image?.let {
                val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
                Log.d("imgsize", "${inputImage.width} x " + "${inputImage.height}")
                Log.d("rotation 1", "rotation: ${imageProxy.imageInfo.rotationDegrees}")
                faceDetector.process(inputImage)
                    .addOnSuccessListener { res ->
                        _faces.value = res
                        imageProxy.image?.let {
                            val inputImage =
                                InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
                            Log.d("imgsize", "${inputImage.width} x " + "${inputImage.height}")
                            Log.d("rotation 2", "rotation: ${imageProxy.imageInfo.rotationDegrees}")
                            faceDetector.process(inputImage)
                                .addOnSuccessListener { res ->
                                    _faces.value = res
                                    val inverse = android.graphics.Matrix()
                                    imageProxy.imageInfo.sensorToBufferTransformMatrix.invert(
                                        inverse
                                    )

                                    if (imageProxy.imageInfo.rotationDegrees == 90) {
                                        //need to apply a rotation to handle this before the rest of the transformations
                                        //this flips the x and y axes, and applies and offset which is necessary
                                        //because 0, 0 is at the top right
                                        val flipper = android.graphics.Matrix().apply {
                                            setValues(
                                                floatArrayOf(
                                                    0F, 1F, 0F,
                                                    -1f, 0f, inputImage.height.toFloat(),
                                                    0f, 0f, 1f
                                                )
                                            )
                                        }
                                        inverse.preConcat(flipper)
                                    }
                                    _analyzerToBuffer.value = inverse
                                }
                                .addOnFailureListener { err ->
                                    Log.d("MLKIT", "ObjectDetectionFailure: $err")
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()
                                }
                        }
                    }
            }
        }
    }
    private val _detObjects = MutableStateFlow<Model.Outputs?>(null)
    val detObjects: StateFlow<Model.Outputs?> = _detObjects.asStateFlow()

    //    val model by lazy {ObjDet.newInstance(this)}
    // The following two members are constructed during camera binding as they require context
    private lateinit var objDetector: Model
    private lateinit var liteRTUseCase: ImageAnalysis
    private lateinit var CVUseCase: ImageAnalysis

    // Authentication management
    private val _client by lazy {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    private lateinit var _authManager: AuthManager

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authManager.login(username, password)
        }
    }

    fun signUp(username: String, password: String) {
        viewModelScope.launch {
            _authManager.signUp(username, password)
        }
    }

    fun currentPreferences(): Flow<Preferences> {
        return _authManager.prefsFlow
    }

    fun currentToken(): Preferences.Key<String> {
        return _authManager.tokenKey
    }

    private fun setContrastPoint(image: ImageProxy) {
        val yPlane = image.planes[0]
        val rowStride = yPlane.rowStride
        val height = image.height
        val width = image.width

        val buffer = yPlane.buffer
        if (!buffer.hasRemaining()) return
        val data = ByteArray(buffer.remaining())
        buffer.get(data)

        fun yAt(rowIdx: Int, coIdx: Int): Int {
            val index = rowIdx * rowStride + coIdx
            return (data[index].toInt() and 0xFF)
        }

        var best = -1
        var bestRow = 0
        var bestCol = 0

        val step = 8
        for (row in 0 until (height - 1) step step) {
            for (column in 0 until (width - 1) step step) {
                val current = yAt(row, column)
                val below = yAt(row + 1, column)
                val right = yAt(row, column + 1)
                val contrast = abs(current - right) + abs(current - below)
                if (contrast > best) {
                    best = contrast
                    bestRow = row
                    bestCol = column
                }
            }
        }

        _contrastPoint.value = ContrastPoint(
            rowIdx = bestRow.toFloat(),
            colIdx = bestCol.toFloat(),
            bufferWidth = width,
            bufferHeight = height
        )
    }

    fun injectContext(appContext: Context) {
        initObjectDetection(appContext)
        initAuthManager(appContext)
    }

    fun initAuthManager(appContext: Context) {
        _authManager = AuthManager(appContext.dataStore, _client)
    }

    fun initObjectDetection(appContext: Context) {
        objDetector = Model.newInstance(appContext)
        liteRTUseCase = ImageAnalysis.Builder().apply {
            setBackpressureStrategy(STRATEGY_KEEP_ONLY_LATEST)
            setOutputImageRotationEnabled(false)
        }.build().apply {
            setAnalyzer(executor) { imageProxy ->
                val bitmap = imageProxy.toBitmap()
                val tensorBuffer = TensorImage.fromBitmap(bitmap)

                _detObjects.value = objDetector.process(tensorBuffer)

                imageProxy.close()
            }
        }
    }

    fun setSurfaceRequest(value: SurfaceRequest?) {
        _surfaceRequest.value = value
    }

    fun setCVModel(value: CVModel) {
        _modelInUse.value = value
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        if (_modelInUse.value == CVModel.Face) {
            CVUseCase = mlKitManualUseCase
        } else {
            initObjectDetection(appContext)
            CVUseCase = liteRTUseCase
        }
        initObjectDetection(appContext)
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase,
            imageCapture,
            CVUseCase
//            mlKitManualUseCase,
//            liteRTUseCase
//            contrastAnalysis,
        )

        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
        }
    }

    fun rebindCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        // If the camera was running, cancel the old job and bind to the new lifecycle owner
        cameraJob?.cancel()
        cameraJob = viewModelScope.launch {
            bindToCamera(appContext, lifecycleOwner)
        }
    }

    fun takePhotoToUri(
        appContext: Context,
        saveUri: Uri,
        onResult: (Result<Uri>) -> Unit
    ) {
        val os = appContext.contentResolver.openOutputStream(saveUri)
        if (os == null) {
            onResult(Result.failure(IllegalStateException("Unable to open OutputStream for $saveUri")))
            return
        }

        val output = ImageCapture.OutputFileOptions.Builder(os).build()

        imageCapture.takePicture(
            output,
            ContextCompat.getMainExecutor(appContext),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                    try {
                        os.close()
                    } catch (_: Exception) {
                    }
                    onResult(Result.success(saveUri))
                }

                override fun onError(exception: ImageCaptureException) {
                    try {
                        os.close()
                    } catch (_: Exception) {
                    }
                    onResult(Result.failure(exception))
                }
            }
        )
    }

    fun flipCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        cameraJob?.cancel()
        cameraSelector = if (cameraSelector == DEFAULT_BACK_CAMERA) {
            DEFAULT_FRONT_CAMERA
        } else {
            DEFAULT_BACK_CAMERA
        }
        cameraJob = viewModelScope.launch {
            bindToCamera(appContext, lifecycleOwner)
        }
    }

    fun toggleCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        when (_recordingState.value) {
            Recording.Start -> {
                cameraJob = viewModelScope.launch {
                    bindToCamera(appContext, lifecycleOwner)
                }
                _recordingState.value = Recording.Stop
            }

            Recording.Stop -> {
                cameraJob?.cancel()
                _recordingState.value = Recording.Start
                setSurfaceRequest(null)
            }
        }
    }

    fun swapCVModel() {
        when (_modelInUse.value) {
            CVModel.Face -> _modelInUse.value = CVModel.Obj
            CVModel.Obj -> _modelInUse.value = CVModel.Face
        }
    }

    fun saveImgToDevice(resultLauncher: ActivityResultLauncher<String>) {
        val fname =
            "IMG_" + SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.US
            ).format(
                Date()
            ) + ".jpg"
        resultLauncher.launch(fname)
    }

    fun takePicture(appContext: Context): Bitmap? {
        var bitmap: Bitmap? = null
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(appContext),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    try {
                        bitmap = image.toBitmap()
                    } finally {
                        image.close()
                    }
                }

                override fun onError(ex: ImageCaptureException) {
                    println("Image capture failed: ${ex.message}")
                }
            }
        )
        return bitmap
    }

    private suspend fun captureBitmap(appContext: Context): Bitmap? =
        suspendCancellableCoroutine { cont ->
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(appContext),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        try {
                            val bitmap = image.toBitmap()
                            cont.resume(
                                bitmap,
                                onCancellation = { Log.d("IMAGE CAPTURE", "coroutine cancelled") })
                        } catch (t: Throwable) {
                            Log.d("IMAGE CAPTURE", "${t.message}")
                        } finally {
                            image.close()
                        }
                    }

                    override fun onError(ex: ImageCaptureException) {
                        println("Image capture failed: ${ex.message}")
                    }
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun postImage(appContext: Context) {
        viewModelScope.launch {
            val image = captureBitmap(appContext)
            val baos = ByteArrayOutputStream()
            Log.d("TEST", "IMAGE")
            image?.let {
                it.compress(Bitmap.CompressFormat.PNG, 0, baos)
                val token =
                    _authManager.dataStore.data.mapLatest { it[_authManager.tokenKey]!! }.first()
                try {
                    Log.d("TEST", "POSTING")
                    val resp = _client.post("http://10.0.2.2:8080/api/images") {
                        Log.d("AUTH", "Token being sent: $token")
                        setBody(
                            MultiPartFormDataContent(
                                formData {
                                    append("description", ";)")
                                    append("image", baos.toByteArray(), Headers.build {
                                        append(HttpHeaders.ContentType, "image/png")
                                        bearerAuth(token)
                                        append(
                                            HttpHeaders.ContentDisposition,
                                            "filename=\"someImage.png\""
                                        )
                                    })
                                }
                            )
                        )
                    }
                    Log.d("TEST", "SENT IMAGE RESP: ${resp.status}")
                } catch (e: Exception) {
                    Log.e("upl", "Image upload failed: ${e}")
                }
            }
        }
    }

    suspend fun getImages(): List<ImageBytesDto>? {
        return try {
            val response = _client.get("http://10.0.2.2:8080/api/images") {
                val token =
                    _authManager.dataStore.data.mapLatest { it[_authManager.tokenKey]!! }.first()
                bearerAuth(token)
            }

            if (response.status == HttpStatusCode.OK) {
                response.body()
            } else {
                Log.e("Gallery", "Failed to fetch images: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("Gallery", "Error: ${e.message}")
            null
        }
    }
}
