package android.ifeanyi.read.app.data.models

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
    id = "1.0.4",
    features = listOf(
        NewFeature(id = 0, title = "Welcome to Read", body = "A powerful text to speech app"),
        NewFeature(id = 1, title = "PDF to text", body = "Listen to your PDFs"),
        NewFeature(id = 2, title = "Image to text", body = "Listen to your Images"),
        NewFeature(id = 3, title = "Web to text", body = "Listen to websites")
    )
)