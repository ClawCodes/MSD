package com.example.maraca
import com.example.maraca.data.AccDB
import com.example.maraca.data.AccRepository

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MaracaApplication : Application() {
    val scope = CoroutineScope(SupervisorJob())

    val db by lazy {
        Room.databaseBuilder(
                applicationContext,
                AccDB::class.java,
                "accel_database"
            ).fallbackToDestructiveMigration(true).build()
    }

    val accRepository by lazy { AccRepository(scope, db.accDao()) }
}