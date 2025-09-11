package com.example.maraca
import com.example.maraca.data.GyroReading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.maraca.ui.theme.MaracaTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maracaVM: MaracaVM by viewModels{ MaracaViewModelProvider.Factory}
        maracaVM.createGyroSensor(this)
        enableEdgeToEdge()
        setContent {
            MaracaTheme {
                val gyroReading by maracaVM.gyroFlow.collectAsStateWithLifecycle(
                    GyroReading(0.0f, 0.0f, 1.0f),
                    lifecycleOwner = this@MainActivity
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GyroReading(
                        reading = gyroReading,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GyroReading(reading: GyroReading, modifier: Modifier = Modifier) {

    Text(
        text = reading.toString(),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ReadingPreview() {
    MaracaTheme {
        GyroReading(GyroReading(1.0f, 2.0f, 3.0f))
    }
}