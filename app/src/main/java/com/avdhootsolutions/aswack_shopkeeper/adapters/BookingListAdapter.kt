package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.enums.BookingStatusEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.CompanyStatusEnum
import com.avdhootsolutions.aswack_shopkeeper.models.BookingList

class BookingListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<BookingListAdapter.CustomListViewHolder>() {

    private var itemList: List<BookingList> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_booking_list, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvCustomerName.text = itemList[position].user_name
        holder.tvTitle.text = itemList[position].vehicle_model_name
        holder.tvCategoryType.text = itemList[position].vehicle_company_name + ", " + itemList[position].vehicle_type_name
        holder.tvVehicleNum.text = itemList[position].vehicle_number
        holder.tvDateTime.text = itemList[position].appointment_date

        if(itemList[position].status == BookingStatusEnum.PENDING.ordinal.toString()){
            holder.tvStatus.text = context.resources.getString(R.string.pending)
            holder.tvStatus.setTextColor(context.getColor(R.color.yellow))
        }else if (itemList[position].status == BookingStatusEnum.ACCEPTED.ordinal.toString()){
            holder.tvStatus.text = context.resources.getString(R.string.accepted)
            holder.tvStatus.setTextColor(context.getColor(R.color.green))

        }else if (itemList[position].status == BookingStatusEnum.REJECT.ordinal.toString()){
            holder.tvStatus.text = context.resources.getString(R.string.rejected)
            holder.tvStatus.setTextColor(context.getColor(R.color.red))

        }

        holder.itemView.setOnClickListener {
            iCustomListListener.onBookingSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<BookingList>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val tvCategoryType : TextView= itemView.findViewById(R.id.tvCategoryType)
        val tvVehicleNum : TextView= itemView.findViewById(R.id.tvVehicleNum)
        val tvDateTime : TextView= itemView.findViewById(R.id.tvDateTime)
        val tvStatus: TextView= itemView.findViewById(R.id.tvStatus)
    }

    interface ICustomListListener {
        fun onBookingSelected(bookingList: BookingList, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}