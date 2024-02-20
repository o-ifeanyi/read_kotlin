package android.ifeanyi.read.core.services

import android.ifeanyi.read.app.data.models.FileModel
import android.ifeanyi.read.app.data.models.FolderModel
import android.ifeanyi.read.app.data.models.WhatsNewModel
import android.ifeanyi.read.app.data.source.FileDao
import android.ifeanyi.read.app.data.source.FolderDao
import android.ifeanyi.read.app.data.source.WhatsNewDao
import android.ifeanyi.read.core.util.RoomConverters
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
