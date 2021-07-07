package ssa.worriorx.com.qnote.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import java.lang.IllegalArgumentException

class NoteViewModelFactory(private val repo: NoteRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)){
            return NoteViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}