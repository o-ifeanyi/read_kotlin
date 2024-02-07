package android.ifeanyi.read.app.data

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.data.source.FileDao
import android.ifeanyi.read.app.data.source.FolderDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.util.UUID
import javax.inject.Inject

class LibraryRepository @Inject constructor(private val fileDao: FileDao, private val folderDao: FolderDao) {
    suspend fun insertItem(item: FileModel) = fileDao.insert(item)
    suspend fun updateItem(item: FileModel) = fileDao.update(item)
    suspend fun deleteItem(item: FileModel) = fileDao.delete(item)
    fun getAllFiles() = fileDao.getAllFiles().flowOn(Dispatchers.IO).conflate()
    fun getFolderFiles(id: UUID) = fileDao.getFolderFiles(id).flowOn(Dispatchers.IO).conflate()

    suspend fun insertItem(item: FolderModel) = folderDao.insert(item)
    suspend fun updateItem(item: FolderModel) = folderDao.update(item)
    suspend fun deleteItem(item: FolderModel) = folderDao.delete(item)
    fun getAllFolders() = folderDao.getAllFolders().flowOn(Dispatchers.IO).conflate()
}