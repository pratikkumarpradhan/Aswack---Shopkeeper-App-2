package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Categories
import com.avdhootsolutions.aswack_shopkeeper.models.SubCategories
import com.bumptech.glide.Glide

class SubCategoryAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<SubCategoryAdapter.CustomListViewHolder>() {

    private var itemList: List<SubCategories> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_checkbox, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        holder.checkBox.text = itemList[position].sub_cat_name
        holder.checkBox.isChecked = itemList[position].isChecked

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            itemList[position].isChecked = isChecked
            iCustomListListener.onCatgeorySelected(itemList[position], position)
        }




    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<SubCategories>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val checkBox : CheckBox = itemView.findViewById(R.id.checkBox)

    }

    interface ICustomListListener {
        fun onCatgeorySelected(subCategories: SubCategories, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}