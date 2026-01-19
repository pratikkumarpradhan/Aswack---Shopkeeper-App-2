package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Categories
import com.bumptech.glide.Glide

class VehicleCategoryAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<VehicleCategoryAdapter.CustomListViewHolder>() {

    private var itemList: List<Categories> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_selling, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].image).placeholder(R.drawable.placeholder).into(holder.ivCategory)
        holder.tvCategoryName.text = itemList[position].name

        holder.itemView.setOnClickListener {
            iCustomListListener.onCatgeorySelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<Categories>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivCategory : ImageView = itemView.findViewById(R.id.ivCategory)
        val tvCategoryName : TextView= itemView.findViewById(R.id.tvCategoryName)

    }

    interface ICustomListListener {
        fun onCatgeorySelected(vehicleCategories: Categories, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}