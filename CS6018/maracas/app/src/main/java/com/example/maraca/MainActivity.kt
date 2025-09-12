package com.example.maraca

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.maraca.composables.AccLineChart
import com.example.maraca.composables.BottomBanner
import com.example.maraca.data.AccReading
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
                val accReading by maracaVM.accFlow.collectAsStateWithLifecycle(
                    AccReading(0.0f, 0.0f, 1.0f),
                    lifecycleOwner = this@MainActivity
                )
                val allReadings by maracaVM.repository.allReadings.collectAsStateWithLifecycle(
                    initialValue = emptyList(),
                    lifecycleOwner = this@MainActivity
                )
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            AccLineChart(allReadings, Modifier.fillMaxSize())
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(8.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                reverseLayout = true // show most recent at top
                            ) {
                                items(allReadings) { reading ->
                                    Text(
                                        text = "x: ${reading.x}, y: ${reading.y}, z: ${reading.z}",
                                        color = Color.Black,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(8.dp)
                        ) {
                            BottomBanner(
                                modifier = Modifier.fillMaxSize(),
                                onDeleteN = maracaVM::deleteNRecords,
                                onDeleteAll = maracaVM::deleteAllRecords
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccelerometerReading(reading: AccReading, modifier: Modifier = Modifier) {

    Text(
        text = reading.toString(),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ReadingPreview() {
    MaracaTheme {
        AccelerometerReading(AccReading(1.0f, 2.0f, 3.0f))
    }
}