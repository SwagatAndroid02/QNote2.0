package ssa.worriorx.com.qnote.ui.home.data

import android.util.Log
import androidx.annotation.NonNull
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.Notes

class NoteRepo(private val dao: NoteDao) {

    val allNote = dao.getAllNotes()

    val allNoteByCat = dao.getAllNoteByCategory()

    val allTrashedNotes = dao.getAllTrashedNotes()


    val allFavorite = dao.getAllFavNotes()


    suspend fun format(){
        dao.deleteAllNotes()
    }

    suspend fun getTID():MutableList<Int>{
        return dao.getTrashNotesIds()
    }

    suspend fun insert(notes: Notes){
        dao.insertNote(notes)
    }

    suspend fun update(notes: Notes){
        dao.updateNote(notes)
    }


    suspend fun getNoteById(@NonNull id: Int):MutableList<Notes>{
        return dao.getNotesById(id)
    }


    suspend fun getAllCategories():MutableList<String>{
        return dao.getAllCategoriess()
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


    suspend fun unTrashed(ids: Int,isTrashed: Boolean){
        dao.unTrashed(ids,isTrashed)
    }

    suspend fun setUnSetfav(id: Int,fav: Boolean){
        dao.setUnsetFavorite(id,fav)
    }



}