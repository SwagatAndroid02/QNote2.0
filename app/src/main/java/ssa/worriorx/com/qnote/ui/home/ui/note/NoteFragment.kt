package ssa.worriorx.com.qnote.ui.home.ui.note

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_note.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.NoteViewModel
import ssa.worriorx.com.qnote.ui.home.NoteViewModelFactory
import ssa.worriorx.com.qnote.ui.home.adapter.NoteListAdapter
import ssa.worriorx.com.qnote.ui.home.data.AppConstants
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import ssa.worriorx.com.qnote.ui.home.ui.add_note.AddNoteActivity
import ssa.worriorx.com.qnote.ui.home.utils.GoToViewActivity
import ssa.worriorx.com.qnote.ui.home.utils.SetUnSetFavorite
import ssa.worriorx.com.qnote.ui.home.utils.SwipeGesture


class NoteFragment : Fragment(), GoToViewActivity, SetUnSetFavorite {

    private lateinit var noteViewModel: NoteViewModel
    private var recyclerview: RecyclerView? = null
    private lateinit var adapter: NoteListAdapter

    private var list: MutableList<Notes>? = null

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
             list = it
             //Log.d("TAG", "CHECK MENU LIST ${list}")
             adapter.submitList(list)
             adapter.notifyDataSetChanged()
        }
    }

    private fun initiateView() {
        adapter = NoteListAdapter(this,this)
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview?.setLayoutManager(lm)
        recyclerview!!.adapter = adapter


        val swipeGesture = object : SwipeGesture(requireContext()){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val id  = list?.get(position)?.id
                if (id != null) {
                    onRemoveItemById(id)
                }

            }
        }

        val swipe = ItemTouchHelper(swipeGesture)
        swipe.attachToRecyclerView(recyclerview)
    }

     fun onRemoveItemById(id: Int) {
        noteViewModel.setTrashed(id, true)
        adapter.notifyDataSetChanged()
    }

    override fun onSetUnSet(id: Int, b: Boolean) {
        noteViewModel.setUnSetFavorite(id,b)
        adapter.notifyDataSetChanged()
    }

    override fun onViewActivityPass(id: Int) {
        val intent = Intent(activity,AddNoteActivity::class.java)
        intent.putExtra(AppConstants.ADD_NOTE_INTENT_KEY,id)
        activity?.startActivity(intent)
    }

}