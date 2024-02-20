package android.ifeanyi.read.core.util

import android.ifeanyi.read.app.data.models.NewFeature
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

object RoomConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): List<NewFeature> {
        val listType = object : TypeToken<List<NewFeature>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(features: List<NewFeature>): String {
        return gson.toJson(features)
    }
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}