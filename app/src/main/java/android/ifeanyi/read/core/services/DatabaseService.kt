package android.ifeanyi.read.core.services

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.data.source.FileDao
import android.ifeanyi.read.app.data.source.FolderDao
import android.ifeanyi.read.core.util.DateConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FileModel::class, FolderModel::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class DatabaseService : RoomDatabase() {
    abstract fun file() : FileDao
    abstract fun folder() : FolderDao
}
