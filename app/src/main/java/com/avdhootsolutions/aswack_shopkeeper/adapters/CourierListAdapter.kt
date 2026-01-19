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
import com.avdhootsolutions.aswack_shopkeeper.models.CourierDetails

class CourierListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<CourierListAdapter.CustomListViewHolder>() {

    private var itemList: List<CourierDetails> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_courier_list, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvCustomerName.text = itemList[position].from_person_name
        holder.tvItemName.text = context.resources.getString(R.string.item_column) + " " +  itemList[position].item_name
        holder.tvWeight.text = context.resources.getString(R.string.weight_column) + " " +   itemList[position].weight
        holder.tvDimension.text = context.resources.getString(R.string.dimensions_column) +  " " +  itemList[position].dimensions
        holder.tvWeight.text = context.resources.getString(R.string.weight_column) +   " " + itemList[position].weight
        holder.tvFrom.text = context.resources.getString(R.string.from_column) +  " " +  itemList[position].from_city_name +
                ", " +  itemList[position].from_state_name + ", " + itemList[position].from_country_name

        holder.tvTo.text = context.resources.getString(R.string.to_column) + " " +   itemList[position].to_city_name +
                ", " +  itemList[position].to_state_name + ", " + itemList[position].to_country_name

        holder.tvDateTime.text = itemList[position].created_datetime
//
//        if(itemList[position].status == BookingStatusEnum.PENDING.ordinal.toString()){
//            holder.tvStatus.text = context.resources.getString(R.string.pending)
//            holder.tvStatus.setTextColor(context.getColor(R.color.yellow))
//        }else if (itemList[position].status == BookingStatusEnum.ACCEPTED.ordinal.toString()){
//            holder.tvStatus.text = context.resources.getString(R.string.accepted)
//            holder.tvStatus.setTextColor(context.getColor(R.color.green))
//
//        }else if (itemList[position].status == BookingStatusEnum.REJECT.ordinal.toString()){
//            holder.tvStatus.text = context.resources.getString(R.string.rejected)
//            holder.tvStatus.setTextColor(context.getColor(R.color.red))
//
//        }

        holder.itemView.setOnClickListener {
            iCustomListListener.onCourierSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<CourierDetails>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvCustomerName: TextView = itemView.findViewById(R.id.tvCustomerName)
        val tvItemName : TextView = itemView.findViewById(R.id.tvItemName)
        val tvWeight : TextView= itemView.findViewById(R.id.tvWeight)
        val tvDimension : TextView= itemView.findViewById(R.id.tvDimension)
        val tvDateTime : TextView= itemView.findViewById(R.id.tvDateTime)
        val tvFrom: TextView= itemView.findViewById(R.id.tvFrom)
        val tvTo: TextView= itemView.findViewById(R.id.tvTo)
    }

    interface ICustomListListener {
        fun onCourierSelected(courierDetails: CourierDetails, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}