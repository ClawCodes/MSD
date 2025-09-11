package com.example.maraca.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class GyroRepository(val scope: CoroutineScope,
                     private val gyroDao: GyroDao) {

    val currentReading: Flow<GyroReading?> = gyroDao.latest()

    val allReadings: Flow<List<GyroReading>> = gyroDao.all()

//    TODO: Figure out how to connect all the data
//    fun getRreading(id: Int): Flow<GyroReading?> {
//
//    }

}