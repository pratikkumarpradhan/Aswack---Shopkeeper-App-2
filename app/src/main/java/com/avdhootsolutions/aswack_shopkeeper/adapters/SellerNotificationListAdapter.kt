package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.AdminNotificationList
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class SellerNotificationListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<SellerNotificationListAdapter.CustomListViewHolder>() {

    private var itemListSeller: ArrayList<AdminNotificationList> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_notification_list, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {


        if (itemListSeller[position].image!!.isNotEmpty()){
            holder.ivNotification.visibility = View.VISIBLE
            Glide.with(context).load(itemListSeller[position].image).placeholder(R.drawable.placeholder).into(holder.ivNotification)

        }else{
            holder.ivNotification.visibility = View.GONE
        }

        holder.tvMessage.text = itemListSeller[position].message
      //  holder.tvDesc.text = itemList[position].description
     //   holder.tvDateTime.text = itemList[position].date + " " +  itemList[position].time
    }

    /**
     * @param itemListSeller list of the item
     */
    fun setList(itemListSeller: ArrayList<AdminNotificationList>) {
        this.itemListSeller = itemListSeller
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemListSeller.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage : TextView = itemView.findViewById(R.id.tvMessage)
        val tvDateTime: TextView = itemView.findViewById(R.id.tvDateTime)
        val  ivNotification: RoundedImageView = itemView.findViewById(R.id.ivNotification)
    }

    interface ICustomListListener {
//        fun onTypesSelected()


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}