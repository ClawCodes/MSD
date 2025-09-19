package com.example.aug_reality

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Surface
import androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
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
    private val _recordingState = MutableStateFlow<Recording>(Recording.Start)

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
            DEFAULT_BACK_CAMERA,
            cameraPreviewUseCase,
            contrastAnalysis,
            imageCapture
        )

        try {
            awaitCancellation()
        } finally {
            processCameraProvider.unbindAll()
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
                    try { os.close() } catch (_: Exception) {}
                    onResult(Result.success(saveUri))
                }
                override fun onError(exception: ImageCaptureException) {
                    try { os.close() } catch (_: Exception) {}
                    onResult(Result.failure(exception))
                }
            }
        )
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
