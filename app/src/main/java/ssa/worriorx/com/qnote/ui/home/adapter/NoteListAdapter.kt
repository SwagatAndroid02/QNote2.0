package ssa.worriorx.com.qnote.ui.home.adapter

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
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
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import ssa.worriorx.com.qnote.ui.home.utils.GoToViewActivity
import ssa.worriorx.com.qnote.ui.home.utils.SetUnSetFavorite
import ssa.worriorx.com.qnote.ui.home.utils.Utils

class NoteListAdapter(val goToViewAct: GoToViewActivity, val setUnSetFav: SetUnSetFavorite) : ListAdapter<Notes, NoteListAdapter.NotesViewHolder>(
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
        return NotesViewHolder(inflater.inflate(R.layout.note_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val itemView = getItem(position)
        val iv_thumbnail: ImageView = holder.itemView.notes_item_iv_avatar
        val tv_title: TextView = holder.itemView.notes_item_tv_title
        val tv_description: TextView = holder.itemView.notes_item_tv_description
        val tv_dateTime: TextView = holder.itemView.notes_item_tv_datetime
        val tv_id: TextView = holder.itemView.notes_item_txt_id
        val iv_star: ImageView = holder.itemView.notes_item_iv_fav

        tv_id.text = itemView.id.toString()
        tv_title.text = itemView.title
        tv_description.text = itemView.description

        if (itemView.starred == 1) {
            Glide.with(holder.itemView.getContext())
                .load(R.drawable.ic_star_selected)
                .into(iv_star)
        }else{
            Glide.with(holder.itemView.getContext())
                .load(R.drawable.ic_star_unselected)
                .into(iv_star)
        }

        if (!itemView.image_path?.isEmpty()!!){
            iv_thumbnail.visibility = View.VISIBLE
            Glide.with(holder.itemView.getContext())
                .load(itemView.image_path)
                .into(iv_thumbnail)
        }

        tv_dateTime.text = itemView.date_time_added?.let { Utils.dateTimeCovertor(it) }

        iv_star.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
               if (itemView.starred == 0)
                   setUnSetFav.onSetUnSet(itemView.id,true)
                if (itemView.starred == 1)
                    setUnSetFav.onSetUnSet(itemView.id,false)
            }

        })


        holder.itemView.notes_item_parent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
               goToViewAct.onViewActivityPass(itemView.id)
            }
        })
    }


    override fun submitList(list: MutableList<Notes>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }

    interface OnRemoveItem{
        fun onRemoveItemById(id: Int)
    }

    /*interface SetUnSetFavorite{
        fun onSetUnSet(id: Int,b: Boolean)
    }*/

   /* interface GoToViewActivity{
        fun onViewActivityPass(id: Int)
    }*/
}