package com.example.aug_reality.composables

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.setFrom
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aug_reality.AugRealityVM
import com.example.aug_reality.CVModel
import com.example.aug_reality.Recording
import kotlinx.coroutines.awaitCancellation

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalGetImage::class)
@Composable
fun HomePage(vm: AugRealityVM, context: Context, lifecycleOwner: LifecycleOwner, onOpenGallery: () -> Unit){
    val surfaceRequestState = vm.surfaceRequest.collectAsStateWithLifecycle()
    val contrastPointState = vm.contrastPoint.collectAsStateWithLifecycle()
    val analyzerToBuffer = vm.analyzerToBuffer.collectAsStateWithLifecycle()
    val faces = vm.faces.collectAsStateWithLifecycle() // face detection
    val detObjects = vm.detObjects.collectAsStateWithLifecycle() // object detection

    val transformer = remember { MutableCoordinateTransformer() }

    val recordingState by vm.recordingState.collectAsState()
    val modelInUse by vm.modelInUse.collectAsState()

    LaunchedEffect(recordingState, modelInUse) {
        vm.setCVModel(modelInUse)
        if (recordingState == Recording.Stop) {
            vm.rebindCamera(context, lifecycleOwner)
        }
    }

    // reset camera rotation for imageCapture use case
//    window?.decorView?.display?.rotation?.let { rotation ->
//        vm.imageCapture.targetRotation = rotation
//    }

    val activity = LocalContext.current as? Activity
    val rotation = activity?.window?.decorView?.display?.rotation
    if (rotation != null) {
        vm.imageCapture.targetRotation = rotation
    }

    // Prebind context and owner for toggling callback without passing params
    val onToggleCamera = remember(vm) {
        {
            vm.toggleCamera(
                appContext = context,
                lifecycleOwner = lifecycleOwner
            )
        }
    }

    val onFlipCamera = remember(vm) {
        {
            vm.flipCamera(
                appContext = context,
                lifecycleOwner = lifecycleOwner
            )
        }
    }

    val onSwapCVModel = remember(vm) {
        {
            vm.swapCVModel()
            vm.rebindCamera(
                appContext = context,
                lifecycleOwner = lifecycleOwner
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        surfaceRequestState.value?.let { request ->

            CameraXViewfinder(
                surfaceRequest = request,
                coordinateTransformer = transformer,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            contrastPointState.value?.let { cPoint ->
                Canvas(modifier = Modifier.fillMaxSize()) {

                    val previewBufferW = request.resolution.width.toFloat()
                    val previewBufferH = request.resolution.height.toFloat()

                    val scaledToPreviewBuffer = Offset(
                        x = cPoint.rowIdx * (previewBufferW / cPoint.bufferWidth),
                        y = cPoint.colIdx * (previewBufferH / cPoint.bufferHeight)
                    )

                    val bufferToUi = Matrix().apply {
                        setFrom(transformer.transformMatrix)
                        invert()
                    }

                    val uiPt: Offset = bufferToUi.map(scaledToPreviewBuffer)

                    val half = 24f
                    drawRect(
                        color = Color.Red,
                        topLeft = Offset(uiPt.x - half, uiPt.y - half),
                        size = Size(half * 2, half * 2),
                        style = Stroke(width = 4f)
                    )
                }
            }
            val transformInfo by
            produceState<SurfaceRequest.TransformationInfo?>(
                null,
                request
            ) {
                request.setTransformationInfoListener(Runnable::run) {
                    value = it
                }
                try {
                    awaitCancellation()
                } finally {
                    request.clearTransformationInfoListener()
                }
            }
            if (modelInUse == CVModel.Face) {
                Text(
                    modifier = Modifier.offset(100.dp, 100.dp),
                    text = "Num Faces: ${faces.value.size}",
                    color = Color.Red
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    transformInfo?.let {

                        val bufferToUiTransformMatrix = Matrix().apply {
                            setFrom(transformer.transformMatrix)
                            invert()
                        }

                        transformInfo?.let {
                            val sensorTransform = it.sensorToBufferTransform

                            val totalMatrix =
                                android.graphics.Matrix(analyzerToBuffer.value)
                            totalMatrix.postConcat(sensorTransform)

                            val totalMatrixCompose = Matrix().apply {
                                setFrom(totalMatrix)
                            }

                            faces.value.forEach {
                                // FIXME: bounding box tracking is inverted in x and y motion
                                val faceBox = it.boundingBox

                                val bb = Rect(
                                    faceBox.left.toFloat(),
                                    faceBox.bottom.toFloat(),
                                    faceBox.right.toFloat(),
                                    faceBox.top.toFloat()
                                )
                                val bufferRect = totalMatrixCompose.map(bb)
                                val uiRect =
                                    bufferToUiTransformMatrix.map(bufferRect)

                                drawRect(
                                    Color.Red,
                                    topLeft = Offset(uiRect.left, uiRect.top),
                                    size = Size(uiRect.width, uiRect.height),
                                    style = Stroke(width = 10f),

                                    )
                            }
                        }
                    }
                }
            }

            if (modelInUse == CVModel.Obj) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val bufferToUi = Matrix().apply {
                        setFrom(transformer.transformMatrix)
                        invert()
                    }

                    val modelW = 640f
                    val modelH = 640f

                    val modelToBufferAndroid = android.graphics.Matrix().apply {
                        val dstW = request.resolution.width.toFloat()
                        val dstH = request.resolution.height.toFloat()
                        setRectToRect(
                            RectF(0f, 0f, modelW, modelH),
                            RectF(0f, 0f, dstW, dstH),
                            android.graphics.Matrix.ScaleToFit.FILL
                        )
                    }
                    val modelToBuffer =
                        Matrix().apply { setFrom(modelToBufferAndroid) }

                    // Draw ONE detection
                    fun drawDetRect(
                        modelRect: RectF,
                        label: String?,
                        score: Float
                    ) {
                        val bufferRect = modelToBuffer.map(
                            androidx.compose.ui.geometry.Rect(
                                modelRect.left,
                                modelRect.top,
                                modelRect.right,
                                modelRect.bottom
                            )
                        )
                        val uiRect = bufferToUi.map(bufferRect)

                        drawRect(
                            color = Color.Red,
                            topLeft = Offset(uiRect.left, uiRect.top),
                            size = Size(uiRect.width, uiRect.height),
                            style = Stroke(width = 6f)
                        )
                        drawIntoCanvas { canvas ->
                            val paint = Paint().apply {
                                color = android.graphics.Color.RED
                                textSize = 40f
                                isAntiAlias = true
                                style = Paint.Style.FILL
                            }

                            val text = "${label ?: "obj"} ${(score * 100).toInt()}%"
                            canvas.nativeCanvas.drawText(
                                text,
                                uiRect.left,
                                (uiRect.top - 8f).coerceAtLeast(40f),
                                paint
                            )
                        }
                    }

                    detObjects.value?.let { out ->
                        Log.d("TEST", "OBJECTS")
                        try {
                            // TODO: iterate through predictions, only draw predictions up to 50%
                            for (resNum in 0 until out.detectionResultList.size) {
                                val detectionResult =
                                    out.detectionResultList.get(resNum)
                                val location = detectionResult.locationAsRectF;
                                val category = detectionResult.categoryAsString;
                                val score = detectionResult.scoreAsFloat;
                                Log.d(
                                    "TEST",
                                    "location: $location, category: $category, score: $score"
                                )
                                drawDetRect(location, category, score)
                            }

//                                        val boxes = out.locationAsTensorBuffer.floatArray
//                                        val scores = out.scoreAsTensorBuffer.floatArray
//                                        val labels = out.categoryAsTensorBuffer.floatArray
//
//                                        val n = minOf(scores.size, boxes.size / 4, labels.size)
//
//                                        for (i in 0 until n) {
//                                            val ymin = boxes[i * 4 + 0]
//                                            val xmin = boxes[i * 4 + 1]
//                                            val ymax = boxes[i * 4 + 2]
//                                            val xmax = boxes[i * 4 + 3]
//                                            val score = scores[i]
//                                            if (score < 0.3f) continue
//
//                                            val normLike = (ymin in 0f..1f) && (xmax in 0f..1f)
//                                            val left = if (normLike) xmin * modelW else xmin
//                                            val top = if (normLike) ymin * modelH else ymin
//                                            val right = if (normLike) xmax * modelW else xmax
//                                            val bottom = if (normLike) ymax * modelH else ymax
//
//                                            drawDetRect(android.graphics.RectF(left, top, right, bottom), "obj", score)
//                                        }
                        } catch (t: Throwable) {
                            Log.e("MainActivity", "Error parsing detections", t)
                        }
                    }
                }
            }
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface.copy(
                alpha = 0.92f
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    12.dp
                )
            ) {
                Button(onClick = onToggleCamera) {
                    Text(recordingState.state)
                }

                Spacer(Modifier.weight(1f))

                Button(onClick = onFlipCamera) {
                    Text("Flip camera")
                }

                Spacer(Modifier.weight(1f))

                Button(onClick = onSwapCVModel) {
                    Text(modelInUse.state)
                }

                Spacer(Modifier.weight(1f))

                Button(onClick = { vm.postImage(context) } ) {
                    Text("Save photo")
                }
            }
        }
    }
}