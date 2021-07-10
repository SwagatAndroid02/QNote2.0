package ssa.worriorx.com.qnote.ui.home.ui.trash

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_trash.*
import kotlinx.android.synthetic.main.fragment_trash.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.MainActivity
import ssa.worriorx.com.qnote.ui.home.NoteViewModel
import ssa.worriorx.com.qnote.ui.home.NoteViewModelFactory
import ssa.worriorx.com.qnote.ui.home.adapter.TrashNoteAdapter
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import ssa.worriorx.com.qnote.ui.home.utils.SwipeGesture

class TrashFragment: Fragment(),TrashNoteAdapter.OnDeleteUndeleteItem {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var adapter: TrashNoteAdapter
    private var recyclerview: RecyclerView? = null

    private var list: MutableList<Notes>? = null
    private var delete_list: List<Int>? = null

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
            list = it
            adapter.submitList(list)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initiateView() {
        adapter = TrashNoteAdapter(this)
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        recyclerview?.setLayoutManager(lm)
        recyclerview!!.adapter = adapter

        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id  = list?.get(position)?.id
                if (id != null) {
                    showDeleteSingleDialog(viewHolder,id)
                }

            }
        }

        val swipe = ItemTouchHelper(swipeGesture)
        swipe.attachToRecyclerView(recyclerview)
    }

    private fun showDeleteSingleDialog(viewHolder: RecyclerView.ViewHolder, id: Int) {
        val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Permanent!")
                builder.setMessage("Are you sure you want delete this item permanently?")
                builder.setPositiveButton("Confirm"){dialog, which ->
                    onRemoveItemById(id)
                }
                builder.setNegativeButton("Cancel"){dialog, which ->
                    val position = viewHolder.adapterPosition
                    adapter.notifyItemChanged(position)
                }
                builder.show()
    }

    private fun showDeleteAllDialog(id_list: List<Int>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete All!")
        builder.setMessage("Are you sure you want empty the trash?")
        builder.setPositiveButton("Confirm"){dialog, which ->
            noteViewModel.deleteMultiple(id_list)
            adapter.notifyDataSetChanged()
        }
        builder.setNegativeButton("Cancel"){dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.activity_main_trash, menu)
        noteViewModel.getTID()
        noteViewModel.ids.observe({ lifecycle }){
            delete_list = it
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onRemoveItemById(id: Int) {
        noteViewModel.deleteSingl(id)
        adapter.notifyDataSetChanged()

    }

    override fun onRestoreItemById(id: Int) {
        noteViewModel.unTrashed(id, false)
        adapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete_all){
            delete_list?.let { showDeleteAllDialog(it) }
        }
        return super.onOptionsItemSelected(item)
    }
}