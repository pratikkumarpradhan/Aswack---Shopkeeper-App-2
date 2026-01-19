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
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class TyreServiceListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<TyreServiceListAdapter.CustomListViewHolder>() {

    private var itemList: List<ProductList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_my_products_tyre_service, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        if (!itemList[position].image1.isNullOrEmpty()){
            Glide.with(context).load(itemList[position].image1).placeholder(R.drawable.placeholder).into(holder.rivProduct)
        }

        if (itemList[position].product_condition == "0"){
            holder.tvNew.text = "Used"
        }else{
            holder.tvNew.text = "New"
        }

        if (itemList[position].is_tubeless == "0"){
            holder.tvIsTubeLessValue.text = "Yes"
        }else{
            holder.tvIsTubeLessValue.text = "No"
        }

        holder.tvNameValue.text = itemList[position].product_name
        holder.tvSerialNumberValue.text = itemList[position].serial_number
        holder.tvPriceValue.text = Helper().getCurrencySymbol(context) + itemList[position].price
        holder.tvPostedOn.text = "Posted On: "+ itemList[position].created_datetime

        holder.itemView.setOnClickListener {
            iCustomListListener.onSoldvehicleSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<ProductList>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rivProduct : RoundedImageView = itemView.findViewById(R.id.rivProduct)
        val tvNameValue : TextView= itemView.findViewById(R.id.tvNameValue)
        val tvSerialNumberValue : TextView= itemView.findViewById(R.id.tvSerialNumberValue)
        val tvIsTubeLessValue : TextView= itemView.findViewById(R.id.tvIsTubeLessValue)
        val tvPriceValue : TextView= itemView.findViewById(R.id.tvPriceValue)
        val tvPostedOn : TextView= itemView.findViewById(R.id.tvPostedOn)
        val tvNew : TextView= itemView.findViewById(R.id.tvNew)


    }

    interface ICustomListListener {
        fun onSoldvehicleSelected(productList: ProductList, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}