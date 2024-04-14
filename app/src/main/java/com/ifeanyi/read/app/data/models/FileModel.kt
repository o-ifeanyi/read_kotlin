package com.ifeanyi.read.app.data.models

import android.content.Context
import com.ifeanyi.read.core.theme.AppIcons
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifeanyi.read.core.util.formatted
import java.io.File
import java.time.Instant
import java.util.Date
import java.util.UUID

enum class LibraryType { Pdf, Img, Txt, Url }

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
    var cache: String? = null,
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
            LibraryType.Txt -> AppIcons.text
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

    fun readCache(): String? {
        return try {
            if (this.cache == null) return null
            val file = File(this.cache!!)
            file.readText().formatted
        } catch (_: Exception) {
            null
        }
    }

    fun writeCache(context: Context, text: String) {
        try {
            val path = "${context.filesDir.path}/${Instant.now()}"
            val outputFile = File(path)
            outputFile.writeText(text, Charsets.UTF_8)

            this.cache = outputFile.toUri().path
        } catch (_: Exception) {
        }
    }
}
