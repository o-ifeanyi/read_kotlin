package android.ifeanyi.read.app.data

import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.data.source.LibraryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LibraryRepository @Inject constructor(private val libraryDao: LibraryDao) {
    suspend fun insertItem(item: LibraryModel) = libraryDao.insert(item)
    suspend fun updateItem(item: LibraryModel) = libraryDao.update(item)
    suspend fun deleteItem(item: LibraryModel) = libraryDao.delete(item)
    fun getAllItems() = libraryDao.getLibraryItems().flowOn(Dispatchers.IO).conflate()
}