package ssa.worriorx.com.qnote.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.group_category_item.view.*
import ssa.worriorx.com.qnote.R
import ssa.worriorx.com.qnote.ui.home.db.room.Notes
import ssa.worriorx.com.qnote.ui.home.utils.GoToViewActivity
import ssa.worriorx.com.qnote.ui.home.utils.Utils


class CategoryAdapter(val goToViewAct: GoToViewActivity): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


    private var groupData: MutableList<Int> = ArrayList()
    private var menuDetails: MutableList<Notes> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_category_item, parent, false)

        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val note: Notes = menuDetails[position]

        val tv_goup_name: TextView? = holder.itemView.note_category_item_tv_category
        val tv_title: TextView? = holder.itemView.note_category_item_tv_title
        val tv_date: TextView? = holder.itemView.note_category_item_tv_datetime
        val tv_description: TextView? = holder.itemView.note_category_item_tv_description
        val iv_image: ImageView? = holder.itemView.note_category_item_iv_avatar
        val iv_star: ImageView? = holder.itemView.note_category_item_iv_fav

        if (groupData[position] == 0){
            tv_goup_name?.visibility = View.VISIBLE
            tv_goup_name?.text = note.category
        }else{
            tv_goup_name?.visibility = View.GONE
        }

        tv_title?.text = note.title
        tv_description?.text = note.description

        if (note.starred == 1) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_star_selected)
                    .into(iv_star!!)
        }else{
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_star_unselected)
                    .into(iv_star!!)
        }

        if (!note.image_path?.isEmpty()!!){
            iv_image?.visibility = View.VISIBLE
            Glide.with(holder.itemView.getContext())
                    .load(note.image_path)
                    .into(iv_image!!)
        }

        tv_date?.text = note.date_time_added?.let { Utils.dateTimeCovertor(it) }

        holder.itemView.note_category_itemparent.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                goToViewAct.onViewActivityPass(note.id)
            }
        })
    }

    override fun getItemCount(): Int {
        return menuDetails.size
    }

    fun setDataList(menuList: MutableList<Notes>?, groupData: MutableList<Int>) {
        menuDetails=menuList!!;
        this.groupData = groupData;
        notifyDataSetChanged();
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}