package com.ifeanyi.read.core.util

import com.ifeanyi.read.app.data.models.NewFeature
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

object RoomConverters {
    private val gson = Gson()

    @TypeConverter
    fun featuresFromJson(json: String): List<NewFeature> {
        val type = object : TypeToken<List<NewFeature>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun featuresToJson(features: List<NewFeature>): String {
        return gson.toJson(features)
    }

    @TypeConverter
    fun rangeFromJson(json: String): IntRange {
        val type = object : TypeToken<IntRange>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun rangeToJson(range: IntRange): String {
        return gson.toJson(range)
    }

    @TypeConverter
    fun dateFromTimeStamp(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}