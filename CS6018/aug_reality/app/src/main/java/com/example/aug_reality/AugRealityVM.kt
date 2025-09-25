package com.example.aug_reality

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Surface
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.math.abs

enum class Recording(val state: String) {
    Start("Start"),
    Stop("Stop")
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

    // TODO: ML STUFF
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

            imageProxy.image?.let { //inside here, it is the MediaImage passed to the analyzer
                //Log.d("original image size", "${it.width} x ${it.height}")
                val inputImage = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
                //will always be 640x480 because setOuputImageRotation is false above
                Log.d("imgsize", "${inputImage.width} x " + "${inputImage.height}")
                //should be 0 or 90, I think, if 180/270 are possible, add more flippers
                Log.d("rotation", "rotation: ${imageProxy.imageInfo.rotationDegrees}")
                faceDetector.process(inputImage)
                    .addOnSuccessListener { res ->
                        _faces.value = res
                        // FIXME: OLD implementation
                        imageProxy.image?.let { //inside here, it is the MediaImage passed to the analyzer
                            //Log.d("original image size", "${it.width} x ${it.height}")
                            val inputImage =
                                InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
                            //will always be 640x480 because setOuputImageRotation is false above
                            Log.d("imgsize", "${inputImage.width} x " + "${inputImage.height}")
                            //should be 0 or 90, I think, if 180/270 are possible, add more flippers
                            Log.d("rotation", "rotation: ${imageProxy.imageInfo.rotationDegrees}")
                            faceDetector.process(inputImage)
                                .addOnSuccessListener { res ->
                                    _faces.value = res
                                    //Log.d("analyzer mat before invert", "${imageProxy.imageInfo.sensorToBufferTransformMatrix}")
                                    //store the buffer to sensor transform in this state
                                    // since we'll be transforming boxes in buffer coordinates
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
                                        //for debugging
//                                faces.firstOrNull()?.let {
//                                    Log.d("face bb", "${it.boundingBox}")
//                                    val transformed = RectF(it.boundingBox)
//                                    flipper.mapRect(transformed)
//                                    Log.d("flipper transform", "$transformed")
//                                }

                                        //apply this rotation/flipping BEFORE the other transform, if
                                        //we need to do it
                                        inverse.preConcat(flipper)

                                    }

                                    _analyzerToBuffer.value = inverse
                                    //Log.d("analyzer mat after invert", "${analyzerToBuffer.value}")
                                }
                                .addOnFailureListener { err ->
                                    Log.d("MLKIT", "ObjectDetectionFailure: $err")
                                }
                                .addOnCompleteListener {
                                    //don't close before we look at it
                                    imageProxy.close()
                                }
                        }
                    }
            }
        }
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

    fun setSurfaceRequest(value: SurfaceRequest?) {
        _surfaceRequest.value = value
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)
        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            cameraPreviewUseCase,
            imageCapture,
            mlKitManualUseCase,
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
}
