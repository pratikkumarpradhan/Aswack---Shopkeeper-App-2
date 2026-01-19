package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.BrandsTypesModels
import com.avdhootsolutions.aswack_shopkeeper.models.Categories
import com.avdhootsolutions.aswack_shopkeeper.models.Fuels
import com.bumptech.glide.Glide

class FuelCheckBoxAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<FuelCheckBoxAdapter.CustomListViewHolder>() {

    private var itemList: List<Fuels> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_checkbox, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        holder.checkBox.text = itemList[position].fuel

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            itemList[position].isChecked = isChecked
            iCustomListListener.onFuelSelected()
        }



    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<Fuels>) {
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
        fun onFuelSelected()


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}