package com.ifeanyi.read.app.data.source

import com.ifeanyi.read.app.data.models.FolderModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query(value = "SELECT * FROM folder_table")
    fun getAllFolders() : Flow<List<FolderModel>>

    @Insert(entity = FolderModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FolderModel)

    @Update(entity = FolderModel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: FolderModel)

    @Delete(entity = FolderModel::class)
    suspend fun delete(item: FolderModel)
}