package android.ifeanyi.read.app.data.source

import android.ifeanyi.read.app.data.models.WhatsNewModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WhatsNewDao {
    @Query(value = "SELECT * FROM whats_new")
    fun getAllUpdates() : Flow<List<WhatsNewModel>>

    @Query(value = "SELECT * FROM whats_new WHERE id IS :id")
    suspend fun getUpdate(id: String) : WhatsNewModel?

    @Insert(entity = WhatsNewModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WhatsNewModel)
}