package ssa.worriorx.com.qnote.ui.home.db.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_data_table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "webLink")
    val web_link: String?,

    @ColumnInfo(name = "dateTimeAdded")
    val date_time_added: String,

    @ColumnInfo(name = "dateTimeModified")
    val date_time_modified: String?,

    @ColumnInfo(name = "imagePath")
    val image_path: String?,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "color")
    val color: String?,

    @ColumnInfo(name = "favorite")
    val starred: Int,

    @ColumnInfo(name = "trashed")
    val trashed: Boolean

)
