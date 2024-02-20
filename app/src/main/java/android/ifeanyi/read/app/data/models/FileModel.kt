package android.ifeanyi.read.app.data.models

import android.ifeanyi.read.core.theme.AppIcons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

enum class LibraryType { Pdf, Image, Url }

@Entity(tableName = "file_table")
data class FileModel(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val type: LibraryType,
    @ColumnInfo
    val date: Date = Date.from(Instant.now()),
    @ColumnInfo
    val path: String,
    @ColumnInfo
    val progress: Int = 0,
    @ColumnInfo
    val parent: UUID? = null,
) {
    fun icon(): ImageVector {
         return when (type) {
            LibraryType.Pdf -> AppIcons.doc
            LibraryType.Image -> AppIcons.image
            LibraryType.Url -> AppIcons.link
        }
    }
}
