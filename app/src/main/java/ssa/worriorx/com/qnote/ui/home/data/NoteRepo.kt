package ssa.worriorx.com.qnote.ui.home.data

import android.util.Log
import androidx.annotation.NonNull
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.Notes

class NoteRepo(private val dao: NoteDao) {

    val allNote = dao.getAllNotes()

    val getUpdatedNotes = dao.getUpdatedAllNotes()

    val allTrashedNotes = dao.getAllTrashedNotes()

    val allCategory = dao.getCategoryNames()

    val allFavorite = dao.getAllFavNotes()

    val allUncategorized = dao.getUnCategorizedNotes()

    //val getTrashNoteIds = dao.getTrashNotesIds()

    suspend fun getTID():MutableList<Int>{
        return dao.getTrashNotesIds()
    }

    suspend fun insert(notes: Notes){
        dao.insertNote(notes)
    }

    suspend fun update(notes: Notes){
        dao.updateNote(notes)
    }

    suspend fun delete(notes: Notes){
        dao.deleteNote(notes)
    }

    suspend fun setCatg(ids: List<Int>,catagory: String){
        dao.setCategory(ids,catagory)
    }

    suspend fun getNotesByCatg(@NonNull catagory: String):MutableList<Notes>{
        return dao.getNotesByCatagory(catagory)
    }

    suspend fun getNoteById(@NonNull id: Int):MutableList<Notes>{
        return dao.getNotesById(id)
    }

    suspend fun deleteSingl(@NonNull id: Int){
        dao.deleteSingle(id)
    }

    suspend fun deleteMultiple(@NonNull ids: List<Int>){
        dao.deleteMultiple(ids)
    }

    suspend fun setTrashed(ids: Int,isTrashed: Boolean){
        dao.setTrashed(ids,isTrashed)
    }

    suspend fun renameCatg(@NonNull oldName: String,@NonNull newName: String){
        dao.renameCategory(oldName, newName)
    }

    suspend fun deleteByCatg(@NonNull categoryName: String){
        dao.deleteByCategory(categoryName)
    }

   /* suspend fun getTrashNoteIds():MutableList<Int>{
        return dao.getTrashNotesIds()
    }*/

}