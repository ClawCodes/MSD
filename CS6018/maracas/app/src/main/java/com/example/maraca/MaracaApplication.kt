package com.example.maraca
import com.example.maraca.data.GyroDB
import com.example.maraca.data.GyroRepository

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MaracaApplication : Application() {
    val scope = CoroutineScope(SupervisorJob())

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            GyroDB::class.java,
            "gryro_database"
        ).build()
    }

    val gryoRepository by lazy { GyroRepository(scope, db.gyroDao()) }
}