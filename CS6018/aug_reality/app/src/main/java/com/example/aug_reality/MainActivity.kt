package com.example.aug_reality

import android.Manifest
import android.content.Context
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
import androidx.camera.core.SurfaceRequest
import androidx.camera.viewfinder.compose.MutableCoordinateTransformer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.setFrom
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.aug_reality.composables.HomePage
import com.example.aug_reality.composables.LoginPage
import com.example.aug_reality.ui.theme.AugRealityTheme
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.onSuccess

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

enum class Screen {
    HOME,
    GALLERY
}

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
        vm.injectContext(this@MainActivity)

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
            val prefsFlow = vm.currentPreferences()
            val currentPrefs by prefsFlow.collectAsState(null)
            val loggedIn = currentPrefs?.contains(vm.currentToken()) ?: false
            var currentScreen by remember { mutableStateOf(Screen.HOME) }

            AugRealityTheme {
                if (loggedIn) {
                    HomePage(vm, applicationContext, this@MainActivity){
                        println("OPEN GALLERY")
                    }
                }
                else {
                    LoginPage(vm, modifier = Modifier)
                }
            }
        }
    }

    fun verifyPermissions() {
        if (checkSelfPermission(Manifest.permission.CAMERA) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            haveCameraPermissionsState.value = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}



