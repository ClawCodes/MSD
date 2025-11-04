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
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aug_reality.composables.Gallery
import com.example.aug_reality.composables.HomePage
import com.example.aug_reality.composables.LoginPage
import com.example.aug_reality.ui.theme.AugRealityTheme
import kotlinx.coroutines.flow.MutableStateFlow
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
                    if (currentScreen == Screen.HOME) {
                        HomePage(vm, applicationContext, this@MainActivity) {
                            currentScreen = Screen.GALLERY
                        }
                    }
                    else {
                        Gallery(vm){
                            Log.d("MainActivity", "Returning to home")
                            currentScreen = Screen.HOME
                        }
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



