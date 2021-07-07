package ssa.worriorx.com.qnote.ui.home

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.Notes

class NoteViewModel(private val repo: NoteRepo): ViewModel() {


    private val _notes = MutableLiveData<List<Notes>>()
    val notes: LiveData<List<Notes>> = _notes

    private val _ids = MutableLiveData<List<Int>>()
    val ids: LiveData<List<Int>> = _ids

    fun insert(note: Notes):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(note)
    }

    fun update(note: Notes):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.update(note)
    }

    fun delete(note: Notes):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.delete(note)
    }

    val getAllNotes = repo.allNote

    val getUpdateNotes = repo.getUpdatedNotes

    val getAllTrashedNotes = repo.allTrashedNotes

    val categoryNames = repo.allCategory

    val favoriteNotes = repo.allFavorite

    val unCategorized = repo.allUncategorized

    //val getTrashId = repo.getTrashNoteIds

    fun setCatg(ids: List<Int>,catagory: String): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.setCatg(ids,catagory)
    }

    fun getTID(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getTID().let {
            _ids.postValue(it)
        }
    }

    fun getNoteByCat(@NonNull catagory: String): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getNotesByCatg(catagory).let {
            _notes.postValue(it)
        }
    }

    fun getNoteById(@NonNull id: Int): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getNoteById(id).let {
            _notes.postValue(it)
        }
    }

    fun deleteSingl(@NonNull id: Int):Job = viewModelScope.launch(Dispatchers.IO){
        repo.deleteSingl(id)
    }

    fun deleteMultiple(@NonNull ids: List<Int>):Job = viewModelScope.launch(Dispatchers.IO){
        repo.deleteMultiple(ids)
    }

    fun setTrashed(ids: Int,isTrashed: Boolean):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.setTrashed(ids,isTrashed)
    }

    fun renameCategory(@NonNull oldName: String,@NonNull newName: String):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.renameCatg(oldName, newName)
    }

    fun deleteByCategory(@NonNull categoryName: String):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.deleteByCatg(categoryName)
    }

}