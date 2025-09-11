package com.example.maraca.data
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "acc_data")
data class AccReading(val x: Float, val y: Float, val z: Float, val timestamp: Long = System.currentTimeMillis()){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun magnitude(): Float {
        return kotlin.math.sqrt(x * x + y * y + z * z)
    }
}

@Database(entities = [AccReading::class], version = 1, exportSchema = false)
abstract class AccDB : RoomDatabase() {
    abstract fun accDao(): AccDao
}

@Dao
interface AccDao {
    @Insert
    suspend fun insert(reading: AccReading)

    @Query("SELECT * FROM acc_data ORDER BY timestamp DESC LIMIT 1")
    fun latest(): Flow<AccReading?>

    @Query("SELECT * FROM acc_data ORDER BY timestamp DESC")
    fun all(): Flow<List<AccReading>>

}
