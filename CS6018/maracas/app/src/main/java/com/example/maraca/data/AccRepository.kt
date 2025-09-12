package com.example.maraca.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    fun deleteOldestN(n: Int) {
        scope.launch {
            val toDelete = allReadings.first().take(n)
            db.delete(toDelete)
        }
    }

    fun deleteAll(){
        scope.launch {
            db.deleteAll()
        }

}
}