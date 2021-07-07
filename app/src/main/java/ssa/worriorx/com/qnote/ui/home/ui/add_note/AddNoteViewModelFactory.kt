package ssa.worriorx.com.qnote.ui.home.ui.add_note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import java.lang.IllegalArgumentException

class AddNoteViewModelFactory(private val repo: NoteRepo):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNoteViewModel::class.java)){
            return AddNoteViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}