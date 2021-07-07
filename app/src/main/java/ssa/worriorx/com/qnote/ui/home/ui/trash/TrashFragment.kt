package ssa.worriorx.com.qnote.ui.home.ui.trash

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_trash.*
import kotlinx.android.synthetic.main.fragment_trash.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.NoteViewModel
import ssa.worriorx.com.qnote.ui.home.NoteViewModelFactory
import ssa.worriorx.com.qnote.ui.home.adapter.NoteListAdapter
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase

class TrashFragment: Fragment(),NoteListAdapter.OnRemoveItem {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: NoteListAdapter
    private var recyclerview: RecyclerView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {
        val root = inflater.inflate(R.layout.fragment_trash, container, false)
        val dao: NoteDao = NoteDatabase.getInstanse(requireContext()).noteDao
        val repository = NoteRepo(dao)
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this@TrashFragment, factory).get(NoteViewModel::class.java)
        recyclerview = root.trash_frag_recyclerview
        initiateView()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.getAllTrashedNotes.observe({ lifecycle }){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initiateView() {
        adapter = NoteListAdapter(this)
        recyclerview!!.layoutManager = LinearLayoutManager(context)
        recyclerview!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_trash, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onRemoveItemById(id: Int) {
        noteViewModel.deleteSingl(id)
        adapter.notifyDataSetChanged()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete_all){
            noteViewModel.getTID()
            noteViewModel.ids.observe({lifecycle}){
               noteViewModel.deleteMultiple(it)
                adapter.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}