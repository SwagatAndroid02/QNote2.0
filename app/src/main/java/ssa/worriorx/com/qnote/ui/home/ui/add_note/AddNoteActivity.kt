package ssa.worriorx.com.qnote.ui.home.ui.add_note

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.content_add_note.*
import kotlinx.android.synthetic.main.create_categoey_dialog.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.data.AppConstants
import ssa.worriorx.com.qnote.ui.home.data.AppConstants.CAMERA_IMAGE_REQ_CODE
import ssa.worriorx.com.qnote.ui.home.data.AppConstants.GALLERY_IMAGE_REQ_CODE
import ssa.worriorx.com.qnote.ui.home.data.NoteRepo
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDao
import ssa.worriorx.com.qnote.ui.home.db.room.NoteDatabase
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddNoteActivity : AppCompatActivity(), AddNoteBottomSheetFragment.ItemClickListener {

    private var mMenu: Menu? = null
    private var note: Notes? = null //for update note
    private var category_list: MutableList<String>? = null

    private var txt_title: EditText? = null
    private var txt_body: EditText? = null
    private var view_image: ImageView? = null
    private var iv_close_image: ImageView? = null

    private var imagePath: String = ""
    private var starred: Int = 0
    private var sessionId: Int = -1
    private var isForUpdate = false

    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null

    private var category: String = "Uncategorized"

    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(findViewById(R.id.add_note_toolbar))
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setTitle(category)

        val dao: NoteDao = NoteDatabase.getInstanse(application).noteDao
        val repository = NoteRepo(dao)
        val factory = AddNoteViewModelFactory(repository)
        addNoteViewModel = ViewModelProvider(this, factory).get(AddNoteViewModel::class.java)


        txt_title = et_title
        txt_body = et_body
        view_image = iv_image
        iv_close_image = add_note_image_close
        txt_title?.requestFocus()

        fab.setOnClickListener { view ->
            supportFragmentManager.let {
                AddNoteBottomSheetFragment.newInstance(Bundle()).apply {
                    show(it, tag)
                }
            }
        }
        invalidateOptionsMenu()

        iv_close_image?.setOnClickListener {
            view_image?.setImageBitmap(null)
            imagePath = ""
            iv_close_image?.visibility = View.GONE
        }

        val intent = intent
        if (intent.hasExtra(AppConstants.ADD_NOTE_INTENT_KEY)) {
            getSupportActionBar()?.setTitle("Edit Note")
            sessionId = intent.extras!!.getInt(AppConstants.ADD_NOTE_INTENT_KEY)
            populateNote(sessionId)
        }
    }

    override fun onStart() {
        super.onStart()
        addNoteViewModel.getAllCategories()
        addNoteViewModel.categories.observe({ lifecycle }) {
            category_list = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            onBackPressed()
        }
        if (item.itemId === R.id.action_favorite) {
            if (starred == 0) {
                mMenu?.getItem(0)?.setIcon(R.drawable.ic_star_selected)
                starred = 1
            } else {
                mMenu?.getItem(0)?.setIcon(R.drawable.ic_star_unselected)
                starred = 0
            }
        }
        if (item.itemId === R.id.action_add_category) {
            showCategoryDialog(category_list!!)
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
        if (!isForUpdate) {
            if (!txt_title?.text.toString().isEmpty() || !txt_body?.text.toString()
                    .isEmpty() || !imagePath.isEmpty()
            )
                addNoteViewModel.insert(
                    Notes(
                        id = 0,
                        title = txt_title?.text.toString(),
                        description = txt_body?.text.toString(),
                        date_time_added = getCurrentDateTime(),
                        web_link = null,
                        date_time_modified = null,
                        image_path = imagePath,
                        category = category,
                        color = null,
                        starred = starred,
                        trashed = false
                    )
                )
        } else {
            addNoteViewModel.update(
                Notes(
                    id = sessionId,
                    title = txt_title?.text.toString(),
                    description = txt_body?.text.toString(),
                    date_time_added = note?.date_time_added,
                    web_link = null,
                    date_time_modified = getCurrentDateTime(),
                    image_path = imagePath,
                    category = category,
                    color = null,
                    starred = starred,
                    trashed = false
                )
            )
        }
    }

    override fun onItemClick(param: String) {
        when (param) {
            "Camera" -> {
                ImagePicker.Companion.with(this)
                    .compress(1024)
                    .cameraOnly()
                    .maxResultSize(1020, 1020)
                    .start(CAMERA_IMAGE_REQ_CODE)
            }
            "Gallery" -> {
                ImagePicker.Companion.with(this)
                    .compress(1024)
                    .galleryOnly()
                    .maxResultSize(1020, 1020)
                    .start(GALLERY_IMAGE_REQ_CODE)
            }
            else -> {
                //Handle data
            }
        }
    }


    fun getCurrentDateTime(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_IMAGE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = data?.data!!
                try {
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
                    imagePath = uri.path.toString()
                    view_image!!.setImageBitmap(bitmap)
                    iv_close_image?.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.d("TAG", "onActivityResult Camera Image Error : ${e.localizedMessage}")
                }
            }
        }
        if (requestCode == GALLERY_IMAGE_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri = data?.data!!
                try {
                    val bitmap = MediaStore.Images.Media
                        .getBitmap(contentResolver, uri)
                    val bytes = ByteArrayOutputStream()
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 50, bytes)
                    imagePath = uri.path.toString()
                    view_image!!.setImageBitmap(bitmap)
                    iv_close_image?.visibility = View.VISIBLE
                } catch (e: Exception) {
                    Log.d("TAG", "onActivityResult Camera Image Error : ${e.localizedMessage}")
                }
            }
        }
    }

    private fun showCategoryDialog(list: MutableList<String>) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a category")

        val cats: Array<String> = list.toTypedArray()
        val item = cats
        builder.setItems(item) { dialog, which ->
            category = item[which]
            getSupportActionBar()?.setTitle(category)
        }
        builder.setPositiveButton("Create New") { dialog, which ->
            dialog.dismiss()
            openCreateCategoryDialog()
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun openCreateCategoryDialog() {
        Log.d("TAG", "CHEKC WHAT openCreateCategoryDialog")
        //val inputEditTextField = EditText(this)
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Create Category")
        //dialog.setView(inputEditTextField)
        val view: View = LayoutInflater.from(this).inflate(R.layout.create_categoey_dialog, null)
        val et_input: AppCompatEditText = view.et_add_category
        et_input.hint = "enter category name"
        dialog.setView(view)
        dialog.setPositiveButton("create") { dialog, which ->
            val editTextInput = et_input.text.toString()
            if (editTextInput.isNotEmpty()) {
                category = editTextInput
                supportActionBar?.title = category
            }
            dialog.dismiss()
        }
        dialog.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        dialog.create()
        dialog.show()
    }

    //update note
    private fun populateNote(sessionId: Int) {
        addNoteViewModel.getNoteById(sessionId)
        addNoteViewModel.notes.observe({ lifecycle }) {
            note = it[0]
            isForUpdate = true
            if (note?.title != null) {
                txt_title?.setText(note?.title)
                val position: Int? = txt_title?.length()
                if (position != null) {
                    txt_title?.setSelection(position)
                }
            }
            if (note?.description != null)
                txt_body?.setText(note?.description)
            if (note?.image_path?.isEmpty() == false) {
                imagePath = note?.image_path!!
                val imgFile = File(imagePath)
                if (imgFile.exists()) {
                    val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                    view_image?.setImageBitmap(myBitmap)
                    iv_close_image?.visibility = View.VISIBLE
                }
            }
            if (note?.starred != null) {
                starred = note?.starred!!
            }
            if (note?.category != null) {
                category = note?.category!!
                supportActionBar?.title = category
            }

        }
    }

}