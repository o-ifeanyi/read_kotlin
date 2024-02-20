package android.ifeanyi.read.app.data.source

import android.ifeanyi.read.app.data.models.FileModel
import androidx.lifecycle.LiveData
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
    @Query(value = "SELECT * FROM file_table WHERE parent IS null")
    fun getAllFiles() : Flow<List<FileModel>>

    @Query(value = "SELECT * FROM file_table WHERE parent IS :id")
    fun getFolderFiles(id: UUID) : Flow<List<FileModel>>

    @Query(value = "SELECT COUNT(*) FROM file_table WHERE parent IS :id")
    fun getFolderFilesCount(id: UUID) : LiveData<Int>

    @Insert(entity = FileModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FileModel)

    @Update(entity = FileModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: FileModel)

    @Delete(entity = FileModel::class)
    suspend fun delete(item: FileModel)

}