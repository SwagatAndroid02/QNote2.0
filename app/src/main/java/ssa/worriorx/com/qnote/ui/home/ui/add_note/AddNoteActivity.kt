package ssa.worriorx.com.qnote.ui.home.ui.add_note

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.MainActivity
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity(),AddNoteBottomSheetFragment.ItemClickListener {

    private var mMenu: Menu? = null

    private var et_title: EditText? = null
    private var et_body: EditText? = null
    private var iv_image: ImageView? = null

    private var imagePath: String = ""
    private var starred: Int = 0

    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(findViewById(R.id.toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        val dao: NoteDao = NoteDatabase.getInstanse(application).noteDao
        val repository = NoteRepo(dao)
        val factory = AddNoteViewModelFactory(repository)
        addNoteViewModel = ViewModelProvider(this,factory).get(AddNoteViewModel::class.java)

        et_title = findViewById(R.id.et_title)
        et_body = findViewById(R.id.et_body)
        iv_image = findViewById(R.id.iv_image)
        et_title?.requestFocus()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            supportFragmentManager.let {
                AddNoteBottomSheetFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
        invalidateOptionsMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
           onBackPressed()
        }
        if (item.itemId === R.id.action_favorite){
            if (starred == 0) {
                mMenu?.getItem(0)?.setIcon(R.drawable.ic_star_selected)
                starred = 1
            } else {
                mMenu?.getItem(0)?.setIcon(R.drawable.ic_star_unselected)
                starred = 0
            }
        }
        if (item.itemId === R.id.action_delete){
            Toast.makeText(applicationContext,"DELETE",Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (starred == 0)
        menuInflater.inflate(R.menu.add_note_menu_unstarred, menu)
        if (starred == 1)
            menuInflater.inflate(R.menu.add_note_menu_starred, menu)
        mMenu = menu
        return true
    }

    override fun invalidateOptionsMenu() {
        super.invalidateOptionsMenu()
    }

    override fun onPause() {
        super.onPause()

        if (!et_title?.text.toString().isEmpty() || !et_body?.text.toString().isEmpty() || !imagePath.isEmpty())
        addNoteViewModel.insert(Notes(
            id = 0,
            title =  et_title?.text.toString(),
            description = et_body?.text.toString(),
            date_time_added = getCurrentDateTime(),
            web_link = null,
            date_time_modified = null,
            image_path = imagePath,
            category = null,
            color = null,
            starred = starred,
            trashed = false
        ))
    }

    override fun onItemClick(param: String) {
        when(param){
            "Camera" -> {
                Toast.makeText(applicationContext, "Camera clicked", Toast.LENGTH_SHORT).show()
            }
            "Gallery" -> {
                Toast.makeText(applicationContext, "Camera clicked", Toast.LENGTH_SHORT).show()
            }
            else->{
                //Handle data
            }
        }
    }


    fun getCurrentDateTime():String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}