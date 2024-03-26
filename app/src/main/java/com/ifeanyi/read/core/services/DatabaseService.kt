package com.ifeanyi.read.core.services

import com.ifeanyi.read.app.data.models.FileModel
import com.ifeanyi.read.app.data.models.FolderModel
import com.ifeanyi.read.app.data.models.WhatsNewModel
import com.ifeanyi.read.app.data.source.FileDao
import com.ifeanyi.read.app.data.source.FolderDao
import com.ifeanyi.read.app.data.source.WhatsNewDao
import com.ifeanyi.read.core.util.RoomConverters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FileModel::class, FolderModel::class, WhatsNewModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class DatabaseService : RoomDatabase() {
    abstract fun file(): FileDao
    abstract fun folder(): FolderDao
    abstract fun whatsNew(): WhatsNewDao
}
