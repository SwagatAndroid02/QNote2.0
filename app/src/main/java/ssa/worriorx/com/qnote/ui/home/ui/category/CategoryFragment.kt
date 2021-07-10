package ssa.worriorx.com.qnote.ui.home.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_category.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.NoteViewModel
import ssa.worriorx.com.qnote.ui.home.NoteViewModelFactory
import ssa.worriorx.com.qnote.ui.home.adapter.CategoryAdapter
import ssa.worriorx.com.qnote.ui.home.data.AppConstants
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import ssa.worriorx.com.qnote.ui.home.ui.add_note.AddNoteActivity
import ssa.worriorx.com.qnote.ui.home.utils.GoToViewActivity


class CategoryFragment : Fragment(), GoToViewActivity {

    private lateinit var noteViewModel: NoteViewModel
    private var recyclerview: RecyclerView? = null
    //private lateinit var adapter: CategoryListAdapter
    private lateinit var adapter: CategoryAdapter

    private var menuList:MutableList<Notes>? = null
    val groupData: MutableList<Int> = ArrayList()

    var c = 0
    var j = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_category, container, false)
        val dao: NoteDao = NoteDatabase.getInstanse(requireContext()).noteDao
        val repository = NoteRepo(dao)
        val factory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this@CategoryFragment, factory).get(NoteViewModel::class.java)
        recyclerview = root.recyclerview_categories
        initiateView()
        return root
    }

    override fun onResume() {
        super.onResume()
      /*  noteViewModel.getAllCategories()
        noteViewModel.categories.observe({ lifecycle }){
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }*/

        noteViewModel.getAllNoteByCat.observe({ lifecycle }){
            menuList = it
            menuList?.forEachIndexed{index, item ->
                Log.d("TAG", "CHECK MENU LIST $item")
                var note: Notes = item
                if (groupData.size == 0){
                    groupData.add(c, j);
                    c++;
                    j++;
                }else {
                    if (item.category.equals(menuList!!.get(index - 1).category)) {
                        groupData.add(c, j);
                        c++;
                        j++;
                    } else {
                        j = 0;
                        groupData.add(c, j);
                        c++;
                        j++;
                    }
                }
            }
            adapter.setDataList(menuList,groupData);
        }

    }

    private fun initiateView() {
        adapter = CategoryAdapter(this)
        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerview?.setLayoutManager(lm)
        recyclerview!!.adapter = adapter
    }

    override fun onViewActivityPass(id: Int) {
        val intent = Intent(activity,AddNoteActivity::class.java)
        intent.putExtra(AppConstants.ADD_NOTE_INTENT_KEY,id)
        activity?.startActivity(intent)
    }
}