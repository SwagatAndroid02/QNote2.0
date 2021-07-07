package ssa.worriorx.com.qnote.ui.home.ui.add_note

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.Notes

class AddNoteViewModel(private val repo: NoteRepo): ViewModel() {

    private val _notes = MutableLiveData<List<Notes>>()
    val notes: LiveData<List<Notes>> = _notes

    fun insert(note: Notes): Job = viewModelScope.launch {
        repo.insert(note)
    }

    fun update(note: Notes): Job = viewModelScope.launch {
        repo.update(note)
    }

    fun setCatg(ids: List<Int>,catagory: String): Job = viewModelScope.launch {
        repo.setCatg(ids,catagory)
    }

    fun getNoteById(@NonNull id: Int): Job = viewModelScope.launch {
        repo.getNoteById(id).let {
            _notes.postValue(it)
        }
    }
}