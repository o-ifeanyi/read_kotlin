package android.ifeanyi.read.app.data.source

import android.ifeanyi.read.app.data.models.LibraryModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query(value = "SELECT * from library_table")
    fun getLibraryItems() : Flow<List<LibraryModel>>

    @Insert(entity = LibraryModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LibraryModel)

    @Update(entity = LibraryModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: LibraryModel)

    @Delete(entity = LibraryModel::class)
    suspend fun delete(item: LibraryModel)
}