package ssa.worriorx.com.qnote.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.note_list_item.view.*
import kotlinx.android.synthetic.main.trash_note_list_item.view.*
import kotlinx.android.synthetic.main.trash_note_list_item.view.notes_item_parent
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.db.room.Notes

class TrashNoteAdapter (val onRemoveUnRemove: OnDeleteUndeleteItem) : ListAdapter<Notes, TrashNoteAdapter.NotesViewHolder>(
        object : DiffUtil.ItemCallback<Notes>(){
            override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
                return oldItem.toString() == newItem.toString()
            }

        }
) {

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotesViewHolder(inflater.inflate(R.layout.trash_note_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val itemView = getItem(position)
        val iv_thumbnail: ImageView = holder.itemView.notes_trash_item_iv_avatar
        val tv_title: TextView = holder.itemView.notes_trash_item_tv_title
        val tv_description: TextView = holder.itemView.notes_trash_item_tv_description
        val tv_id: TextView = holder.itemView.notes_trash_item_txt_id
        val iv_restore: ImageView = holder.itemView.notes_trash_item_iv_restore

        tv_id.text = itemView.id.toString()
        tv_title.text = itemView.title
        tv_description.text = itemView.description

        if (!itemView.image_path?.isEmpty()!!){
            iv_thumbnail.visibility = View.VISIBLE
            Glide.with(holder.itemView.getContext())
                    .load(itemView.image_path)
                    .into(iv_thumbnail)
        }

        iv_restore.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onRemoveUnRemove.onRestoreItemById(itemView.id)
            }
        })

       /* holder.itemView.notes_item_parent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                deleteItem(itemView.id)
            }

        })*/
    }

    fun deleteItem(id: Int) {
        onRemoveUnRemove.onRemoveItemById(id)
    }

    override fun submitList(list: MutableList<Notes>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    interface OnDeleteUndeleteItem{
        fun onRemoveItemById(id: Int)
        fun onRestoreItemById(id: Int)
    }
}