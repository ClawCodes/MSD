package com.example.maraca

import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.hardware.SensorManager
import android.hardware.Sensor
import com.example.maraca.data.GyroReading
import kotlinx.coroutines.flow.Flow
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.maraca.data.GyroRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.channelFlow

class MaracaVM(gryoRepository: GyroRepository) : ViewModel() {
    lateinit var gyroFlow: Flow<GyroReading>

    val repository = gryoRepository
    fun createGyroSensor(context: Context) {
        val sensorManager = context.getSystemService(SENSOR_SERVICE) as SensorManager
        val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!
        gyroFlow = getGyroData(gyroscope, sensorManager)
    }

    private fun getGyroData(sensor: Sensor, sensorManager: SensorManager): Flow<GyroReading> {
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        channel.trySend(
                            GyroReading(
                                event.values[0],
                                event.values[1],
                                event.values[0]
                            )
                        )
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // Do nothing - only implemented for interface requirements
                }
            }
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
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
                        as MaracaApplication).gryoRepository
            )
        }
    }
}