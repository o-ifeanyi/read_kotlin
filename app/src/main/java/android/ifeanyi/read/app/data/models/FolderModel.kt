package android.ifeanyi.read.app.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

@Entity(tableName = "folder_table")
data class FolderModel(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val date: Date = Date.from(Instant.now()),
)