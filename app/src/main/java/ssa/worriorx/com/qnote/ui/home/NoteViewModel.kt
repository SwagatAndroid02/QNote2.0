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


    private val _notes = MutableLiveData<MutableList<Notes>>()
    val notes: LiveData<MutableList<Notes>> = _notes

    private val _categories = MutableLiveData<MutableList<String>>()
    val categories: LiveData<MutableList<String>> = _categories

    private val _ids = MutableLiveData<List<Int>>()
    val ids: LiveData<List<Int>> = _ids

    fun insert(note: Notes):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.insert(note)
    }

    fun update(note: Notes):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.update(note)
    }


    fun format():Job = viewModelScope.launch(Dispatchers.IO) {
        repo.format()
    }

    val getAllNotes = repo.allNote

    val getAllNoteByCat = repo.allNoteByCat


    val getAllTrashedNotes = repo.allTrashedNotes


    val favoriteNotes = repo.allFavorite


    fun getAllCategories():Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllCategories().let {
            _categories.postValue(it)
        }
    }

    fun getTID(): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.getTID().let {
            _ids.postValue(it)
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

    fun unTrashed(ids: Int,isTrashed: Boolean):Job = viewModelScope.launch(Dispatchers.IO) {
        repo.unTrashed(ids,isTrashed)
    }

    fun setUnSetFavorite(id: Int,fav: Boolean): Job = viewModelScope.launch(Dispatchers.IO) {
        repo.setUnSetfav(id,fav)
    }


}