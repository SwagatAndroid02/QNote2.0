package ssa.worriorx.com.qnote.ui.home.db.room

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun insertNote(notes: Notes)

    @Update
    suspend fun updateNote(notes: Notes)

    @Delete
    suspend fun deleteNote(notes: Notes)

    @Query("DELETE FROM note_data_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM note_data_table WHERE trashed = 0 ORDER BY note_id DESC")
    fun getAllNotes(): LiveData<MutableList<Notes>>

    @Query("SELECT * FROM note_data_table WHERE trashed = 0")
    fun getUpdatedAllNotes(): LiveData<MutableList<Notes>>

    @Query("SELECT * FROM note_data_table WHERE favorite = 1")
    fun getAllFavNotes(): LiveData<MutableList<Notes>>

    @Query("SELECT * FROM note_data_table WHERE category = null")
    fun getUnCategorizedNotes(): LiveData<MutableList<Notes>>

    @Query("SELECT * FROM note_data_table WHERE trashed = 1 ORDER BY note_id DESC")
    fun getAllTrashedNotes(): LiveData<MutableList<Notes>>

    @Query("SELECT * FROM note_data_table ORDER BY category ASC")
    fun getAllNoteByCategory(): LiveData<MutableList<Notes>>

    @Query("SELECT DISTINCT  category FROM note_data_table")
    fun getAllCategoriess(): MutableList<String>

    @Query("UPDATE note_data_table SET category=:catagory WHERE note_id IN (:ids)")
    fun setCategory(ids: List<Int>,catagory: String)

    @Query("SELECT * FROM note_data_table WHERE category = :catagory ORDER BY dateTimeAdded DESC")
    fun getNotesByCatagory(@NonNull catagory: String): MutableList<Notes>

    @Query("SELECT * FROM note_data_table WHERE note_id=:id")
    fun getNotesById(@NonNull id: Int): MutableList<Notes>

    @Query("DELETE FROM note_data_table WHERE note_id = :id")
    fun deleteSingle(@NonNull id: Int)

    @Query("DELETE FROM note_data_table WHERE note_id IN (:ids)")
    fun deleteMultiple(@NonNull ids: List<Int>)

    @Query("UPDATE note_data_table SET trashed=:isTrashed WHERE note_id=:id")
    fun setTrashed(id: Int,isTrashed: Boolean)

    @Query("UPDATE note_data_table SET trashed=:isTrashed WHERE note_id=:id")
    fun unTrashed(id: Int,isTrashed: Boolean)

    @Query("UPDATE note_data_table SET favorite=:fav WHERE note_id=:id")
    fun setUnsetFavorite(id: Int,fav: Boolean)

    @Query("SELECT category from note_data_table")
    fun getCategoryNames(): LiveData<MutableList<String>>

    @Query("UPDATE note_data_table SET category=:newName WHERE  category=:oldName")
    fun renameCategory(@NonNull oldName: String,@NonNull newName: String)

    @Query("DELETE FROM note_data_table WHERE category = :categoryName")
    fun deleteByCategory(@NonNull categoryName: String)

    @Query("SELECT note_id FROM note_data_table WHERE trashed = 1")
    fun getTrashNotesIds():MutableList<Int>
}