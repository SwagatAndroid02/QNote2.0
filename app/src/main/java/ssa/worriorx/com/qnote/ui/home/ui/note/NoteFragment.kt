package ssa.worriorx.com.qnote.ui.home.ui.note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_note.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.NoteViewModel
import ssa.worriorx.com.qnote.ui.home.NoteViewModelFactory
import ssa.worriorx.com.qnote.ui.home.adapter.NoteListAdapter
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase

class NoteFragment : Fragment(),NoteListAdapter.OnRemoveItem {

    private lateinit var noteViewModel: NoteViewModel
    private var recyclerview: RecyclerView? = null
    private lateinit var adapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_note, container, false)
        val dao: NoteDao = NoteDatabase.getInstanse(requireContext()).noteDao
        val repository = NoteRepo(dao)
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this@NoteFragment, factory).get(NoteViewModel::class.java)
        recyclerview = root.recyclerview_notes
        initiateView()

        return root
    }

    override fun onResume() {
        super.onResume()
         noteViewModel.getAllNotes.observe({ lifecycle }){
             adapter.submitList(it)
             adapter.notifyDataSetChanged()
        }
    }

    private fun initiateView() {
        adapter = NoteListAdapter(this)
        recyclerview!!.layoutManager = LinearLayoutManager(context)
        recyclerview!!.adapter = adapter
    }

    override fun onRemoveItemById(id: Int) {
        noteViewModel.setTrashed(id,true)
        adapter.notifyDataSetChanged()
    }

}