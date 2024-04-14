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
    id = "1.0.2",
    features = listOf(
        NewFeature(
            id = 0,
            title = "Introducing Generative AI ✨",
            body = "You now get an AI description of images that have no extractable text"
        ),
    )
)