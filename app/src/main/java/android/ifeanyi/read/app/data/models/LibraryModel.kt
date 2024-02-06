package android.ifeanyi.read.app.data.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Link
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

enum class LibraryType { Pdf, Image, Url }

@Entity(tableName = "library_table")
data class LibraryModel(
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
    val progress: Int,
    @ColumnInfo
    val parent: String,
) {
    fun icon(): ImageVector {
         return when (type) {
            LibraryType.Pdf -> Icons.AutoMirrored.Rounded.InsertDriveFile
            LibraryType.Image -> Icons.Rounded.Image
            LibraryType.Url -> Icons.Rounded.Link
        }
    }
}
