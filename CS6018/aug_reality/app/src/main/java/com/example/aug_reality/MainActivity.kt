package com.example.aug_reality

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aug_reality.ui.theme.AugRealityTheme
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.onSuccess

@ExperimentalGetImage
class MainActivity : ComponentActivity() {
    private var haveCameraPermissionsState = MutableStateFlow(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            haveCameraPermissionsState.value = granted
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        verifyPermissions()
        val vm by viewModels<AugRealityVM>()

        // Native file-creation picker: returns a Uri to write into
        val createImageDocument = registerForActivityResult(
            ActivityResultContracts.CreateDocument("image/jpeg")
        ) { uri: Uri? ->
            uri?.let {
                vm.takePhotoToUri(this@MainActivity, it) { result ->
                    result.onSuccess {
                        Log.d("MainActivity", "Photo saved to $it")
                    }.onFailure { e ->
                        Log.e("MainActivity", "Error saving photo", e)
                    }
                }
            }
        }


        setContent {
            AugRealityTheme {
                val surfaceRequestState = vm.surfaceRequest.collectAsStateWithLifecycle()
                val contrastPointState = vm.contrastPoint.collectAsStateWithLifecycle()

                val transformer = remember { MutableCoordinateTransformer() }

                val recordingState by vm.recordingState.collectAsState()

                LaunchedEffect(recordingState) {
                    if (recordingState == Recording.Stop) {
                        vm.rebindCamera(applicationContext, this@MainActivity)
                    }
                }

                // reset camera rotation for imageCapture use case
                window?.decorView?.display?.rotation?.let { rotation ->
                    vm.imageCapture.targetRotation = rotation
                }

                // Prebind context and owner for toggling callback without passing params
                val onToggleCamera = remember(vm) {
                    { vm.toggleCamera(appContext = applicationContext, lifecycleOwner = this@MainActivity) }
                }

                val onFlipCamera = remember(vm) {
                    { vm.flipCamera(appContext = applicationContext, lifecycleOwner = this@MainActivity) }
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
                    }

                    androidx.compose.material3.Surface(
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.BottomCenter)
                            .padding(16.dp)
                            .fillMaxWidth(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                        tonalElevation = 8.dp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.92f
                        )
                    ) {
                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(
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

                            Button(onClick = {
                                val fname = "IMG_" + SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                                    Date()
                                ) + ".jpg"
                                createImageDocument.launch(fname)
                            }) {
                                Text("Save photo")
                            }
                        }
                    }
                }
            }
        }
    }

    fun verifyPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            haveCameraPermissionsState.value = true
        }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}


