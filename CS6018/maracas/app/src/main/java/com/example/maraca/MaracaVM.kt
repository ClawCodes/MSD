package com.example.maraca

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.hardware.Sensor
import kotlinx.coroutines.flow.Flow
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.maraca.data.AccReading
import com.example.maraca.data.AccRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MaracaVM(accRepository: AccRepository) : ViewModel() {
    lateinit var accFlow: Flow<AccReading>

    val repository = accRepository
    fun createGyroSensor(context: Context) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        accFlow = getGyroData(accelerometer, sensorManager)
    }

    private fun getGyroData(sensor: Sensor, sensorManager: SensorManager): Flow<AccReading> {
        return channelFlow {
            var lastShakeTime = 0L
            val shakeCooldownMs = 100L      // minimum time between shakes
            val shakeThreshold = 15f
            var lastReading: AccReading? = null

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]
                        val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
                        val now = System.currentTimeMillis()

                        if (magnitude > shakeThreshold && now - lastShakeTime > shakeCooldownMs) {
                            lastShakeTime = now
                            val eventMillis: Long =
                                System.currentTimeMillis() -
                                        (SystemClock.elapsedRealtimeNanos() - event.timestamp) / 1_000_000L
                            val reading = AccReading(x, y, z, eventMillis)
                            lastReading = reading
                            channel.trySend(reading)
                            repository.write(reading)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            }

            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

            // Coroutine to emit zero readings if no shake occurs for some time
            launch {
                val decayInterval = 100L       // check every 100 ms
                val inactivityTimeout = 500L   // consider no shake after 500 ms

                while (isActive) {
                    val now = System.currentTimeMillis()
                    if (lastShakeTime != 0L && now - lastShakeTime > inactivityTimeout) {
                        // Emit a reading with zero magnitude
                        val zeroReading = AccReading(0f, 0f, 0f, now)
                        if (lastReading?.magnitude() != 0f) {
                            channel.trySend(zeroReading)
                            lastReading = zeroReading
                        }
                    }
                    kotlinx.coroutines.delay(decayInterval)
                }
            }

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

}

object MaracaViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MaracaVM(
                (this[AndroidViewModelFactory.APPLICATION_KEY]
                        as MaracaApplication).accRepository
            )
        }
    }
}