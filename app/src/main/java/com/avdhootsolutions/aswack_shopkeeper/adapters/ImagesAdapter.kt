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
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_sign_up.*
import java.io.File

class ImagesAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<ImagesAdapter.CustomListViewHolder>() {

    private var itemList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {


        Picasso.get().load(File(itemList[position]))
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher)
            .into(holder.ivImage);

        holder.ivClose.setOnClickListener {
            itemList.removeAt(position)
            notifyDataSetChanged()
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: ArrayList<String>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage : ImageView = itemView.findViewById(R.id.ivImage)
        val ivClose : ImageView = itemView.findViewById(R.id.ivClose)


    }

    interface ICustomListListener {
        fun onTypesSelected()


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}