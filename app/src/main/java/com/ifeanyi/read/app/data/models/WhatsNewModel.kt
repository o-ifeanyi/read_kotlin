package com.ifeanyi.read.app.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "whats_new")
data class WhatsNewModel(
    @PrimaryKey
    val id: String,
    @ColumnInfo
    val features: List<NewFeature>
)

data class NewFeature(
    val id: Int,
    val title: String,
    val body: String,
)

val latestUpdate = WhatsNewModel(
    id = "1.1.1",
    features = listOf(
        NewFeature(
            id = 0,
            title = "Scan page 📖",
            body = "Scan the pages of your favourite books with your camera and listen to them"
        ),
    )
)