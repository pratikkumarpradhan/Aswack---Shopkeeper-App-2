package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.RFQList
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.makeramen.roundedimageview.RoundedImageView

class RFQListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<RFQListAdapter.CustomListViewHolder>() {

    private var itemList: List<RFQList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_rfq, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

//        if (!itemList[position].image1.isNullOrEmpty()){
//            Glide.with(context).load(itemList[position].image1).placeholder(R.drawable.placeholder).into(holder.rivProduct)
//        }

        holder.tvName.text = itemList[position].user_name
        holder.tvMobile.text = itemList[position].user_mobile

        holder.tvProductName.text = itemList[position].product_name
        holder.tvQty.text = context.getString(R.string.qty_column) +  itemList[position].qty
        if (itemList[position].vehicle_brand_name.isNullOrEmpty()){
            holder.tvSerialNumber.visibility = View.GONE
        }else{
            holder.tvSerialNumber.visibility = View.VISIBLE
            holder.tvSerialNumber.text = context.getString(R.string.serial_number_colun) + " ${itemList[position].serial_number}"
        }


        if (itemList[position].status == Helper.RFQ_CLOSE){
            holder.tvChatWithCustomer.visibility = View.GONE
            holder.tvStatus.visibility = View.VISIBLE
            holder.tvStatus.text = context.resources.getString(R.string.closed_rfq)
        }else{
            holder.tvChatWithCustomer.visibility = View.VISIBLE
            holder.tvStatus.visibility = View.GONE
        }


        holder.tvDesInfo.text = itemList[position].description
        holder.tvPostedOn.text = "Posted On: "+ itemList[position].created_datetime

        holder.itemView.setOnClickListener {
            iCustomListListener.onRFQSelected(itemList[position], position)
        }


        if (itemList[position].isChatInitiated){
           holder.tvCanAddQuotationAfterChat.visibility = View.GONE

            if (itemList[position].productDataForSeller.quotationId.isNullOrEmpty()){
                holder.tvAddQuotation.visibility = View.VISIBLE
                holder.tvViewQuotation.visibility = View.GONE
            }else{
                holder.tvAddQuotation.visibility = View.GONE
                holder.tvViewQuotation.visibility = View.VISIBLE
            }

        }else{
            holder.tvCanAddQuotationAfterChat.visibility = View.VISIBLE
            holder.tvAddQuotation.visibility = View.GONE
            holder.tvViewQuotation.visibility = View.GONE
        }

        holder.tvAddQuotation.setOnClickListener {
            iCustomListListener.onAddQuotationSelected(itemList[position], position)
        }

        holder.tvViewQuotation.setOnClickListener {
            iCustomListListener.onViewQuotationSelected(itemList[position], position)
        }

        holder.tvChatWithCustomer.setOnClickListener {
            iCustomListListener.onRFQChatSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<RFQList>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName : TextView= itemView.findViewById(R.id.tvName)
        val tvMobile : TextView= itemView.findViewById(R.id.tvMobile)
        val tvDesInfo : TextView= itemView.findViewById(R.id.tvDesInfo)
        val tvPostedOn : TextView= itemView.findViewById(R.id.tvPostedOn)
        val tvChatWithCustomer : TextView= itemView.findViewById(R.id.tvChatWithCustomer)
        val tvProductName : TextView= itemView.findViewById(R.id.tvProductName)
        val tvSerialNumber : TextView= itemView.findViewById(R.id.tvSerialNumber)
        val tvQty : TextView= itemView.findViewById(R.id.tvQty)
        val tvStatus: TextView= itemView.findViewById(R.id.tvStatus)
        val tvAddQuotation: TextView= itemView.findViewById(R.id.tvAddQuotation)
        val tvViewQuotation: TextView= itemView.findViewById(R.id.tvViewQuotation)
        val tvCanAddQuotationAfterChat: TextView= itemView.findViewById(R.id.tvCanAddQuotationAfterChat)

    }

    interface ICustomListListener {
        fun onRFQSelected(rfqList: RFQList, position: Int)
        fun onRFQChatSelected(rfqList: RFQList, position: Int)
        fun onAddQuotationSelected(rfqList: RFQList, position: Int)
        fun onViewQuotationSelected(rfqList: RFQList, position: Int)

    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}