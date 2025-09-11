package com.example.maraca.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AccRepository(
    val scope: CoroutineScope,
    private val db: AccDao
) {
    val currentReading: Flow<AccReading?> = db.latest()

    val allReadings: Flow<List<AccReading>> = db.all()

    fun write(reading: AccReading) {
        scope.launch {
            db.insert(reading)
        }
    }
}