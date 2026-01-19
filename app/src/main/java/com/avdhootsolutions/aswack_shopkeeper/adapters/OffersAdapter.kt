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
import com.avdhootsolutions.aswack_shopkeeper.models.Offer
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class OffersAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener,

    ) : RecyclerView.Adapter<OffersAdapter.CustomListViewHolder>() {

    private var itemList: List<Offer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_my_offers, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        Glide.with(context).load(itemList[position].image_1).placeholder(R.drawable.placeholder).into(holder.rivProduct)
        holder.tvNameValue.text = itemList[position].name

        if (itemList[position].product_name.isNullOrEmpty()){
            holder.tvProductNameValue.text = "NA"
        }else{
            holder.tvProductNameValue.text = itemList[position].product_name
        }


        holder.tvOfferCodeValue.text = itemList[position].code
        holder.tvOfferSDateValue.text = itemList[position].start_date
        holder.tvOfferEDateValue.text = itemList[position].end_date
//        holder.tvOriginalPriceValue.text = itemList[position].original_price
        holder.tvOfferPriceValue.text = Helper().getCurrencySymbol(context) + itemList[position].offer_price
       // holder.tvSerialNumberValue.text = itemList[position].ser



        holder.itemView.setOnClickListener {
            iCustomListListener.onOfferSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<Offer>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rivProduct : RoundedImageView = itemView.findViewById(R.id.rivProduct)
        val tvNameValue : TextView = itemView.findViewById<TextView?>(R.id.tvNameValue)
        val tvProductNameValue : TextView = itemView.findViewById<TextView?>(R.id.tvProductNameValue)
        val tvOfferCodeValue : TextView = itemView.findViewById<TextView?>(R.id.tvOfferCodeValue)
        val tvOfferSDateValue : TextView = itemView.findViewById<TextView?>(R.id.tvOfferSDateValue)
        val tvOfferEDateValue : TextView = itemView.findViewById<TextView?>(R.id.tvOfferEDateValue)
        val tvOfferPriceValue : TextView = itemView.findViewById<TextView?>(R.id.tvOfferPriceValue)

    }

    interface ICustomListListener {
        fun onOfferSelected(offer: Offer, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}