package android.ifeanyi.read.app.data.source

import android.ifeanyi.read.app.data.models.FileModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface FileDao {
    @Query(value = "SELECT * from file_table WHERE folder IS null")
    fun getAllFiles() : Flow<List<FileModel>>

    @Query(value = "SELECT * from file_table WHERE folder IS :id")
    fun getFolderFiles(id: UUID) : Flow<List<FileModel>>

    @Insert(entity = FileModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FileModel)

    @Update(entity = FileModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: FileModel)

    @Delete(entity = FileModel::class)
    suspend fun delete(item: FileModel)

}