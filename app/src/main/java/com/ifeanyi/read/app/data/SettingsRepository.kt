package com.ifeanyi.read.app.data

import com.ifeanyi.read.app.data.models.WhatsNewModel
import com.ifeanyi.read.app.data.source.WhatsNewDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val whatsNewDao: WhatsNewDao) {
    suspend fun insertItem(item: WhatsNewModel) = whatsNewDao.insert(item)
    suspend fun getUpdate(id: String) = whatsNewDao.getUpdate(id)
    fun getAllUpdates() = whatsNewDao.getAllUpdates().flowOn(Dispatchers.IO).conflate()
}