package com.example.maraca.data
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "gyro_data")
data class GyroReading(val x: Float, val y: Float, val z: Float){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Database(entities = [GyroReading::class], version = 1, exportSchema = false)
abstract class GyroDB : RoomDatabase() {
    abstract fun gyroDao(): GyroDao
}

@Dao
interface GyroDao {
    @Insert
    suspend fun insert(reading: GyroReading)

    @Query("SELECT * FROM gyro_data ORDER BY id DESC LIMIT 1")
    fun latest(): Flow<GyroReading?>

    @Query("SELECT * FROM gyro_data")
    fun all(): Flow<List<GyroReading>>

}
