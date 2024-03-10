package android.ifeanyi.read.app.data.models

import android.ifeanyi.read.core.theme.AppIcons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

enum class LibraryType { Pdf, Img, Url }

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
    val wordRange: IntRange = IntRange(0, 0),
    @ColumnInfo
    val wordIndex: Int = 0,
    @ColumnInfo
    val progress: Int = 0,
    @ColumnInfo
    val currentPage: Int = 1,
    @ColumnInfo
    val totalPages: Int = 1,
    @ColumnInfo
    val parent: UUID? = null,
) {
    fun icon(): ImageVector {
         return when (type) {
            LibraryType.Pdf -> AppIcons.doc
            LibraryType.Img -> AppIcons.image
            LibraryType.Url -> AppIcons.link
        }
    }

    val absProgress: Int
        get() {
            val value = (currentPage.toDouble() / totalPages.toDouble()) * 100
            if (totalPages > 1) {
                return value.toInt()
            }
            return progress
        }
}
