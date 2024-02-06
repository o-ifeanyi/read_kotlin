package android.ifeanyi.read.core.services

import android.ifeanyi.read.app.data.models.LibraryModel
import android.ifeanyi.read.app.data.source.LibraryDao
import android.ifeanyi.read.core.util.DateConverter
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LibraryModel::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class DatabaseService : RoomDatabase() {
    abstract fun library() : LibraryDao
}
