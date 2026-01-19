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
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_company_sign_up.*
import java.io.File

class ProductsImagesAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<ProductsImagesAdapter.CustomListViewHolder>() {

    private var itemList: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_product_slider, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        Glide.with(context).load(itemList[position]).placeholder(R.drawable.placeholder).into(holder.ivCategory)
        holder.itemView.setOnClickListener {
            iCustomListListener.onImageSelected(itemList[position])
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

        val ivCategory : RoundedImageView = itemView.findViewById(R.id.ivCategory)


    }

    interface ICustomListListener {
        fun onImageSelected(imagePath: String)
    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}