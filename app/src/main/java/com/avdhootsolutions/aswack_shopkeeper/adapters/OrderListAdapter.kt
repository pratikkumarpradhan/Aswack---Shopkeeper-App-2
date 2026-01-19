package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.models.OrderRequest
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper

class OrderListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<OrderListAdapter.CustomListViewHolder>() {

    private var itemList: List<OrderRequest> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_order, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        holder.tvName.text = itemList[position].order_code
        holder.tvUserNameValue.text = itemList[position].user_name
        holder.tvCompanyName.text = itemList[position].seller_company_name
        holder.tvInvoiceCodeValue.text = itemList[position].invoice_code
        holder.tvDate.text = itemList[position].txn_date
        holder.tvPaidAmountValue.text = Helper().getCurrencySymbol(context) + itemList[position].paid_amount

        holder.itemView.setOnClickListener {
            iCustomListListener.onOrderSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<OrderRequest>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName : TextView = itemView.findViewById(R.id.tvName)
        val tvUserNameValue: TextView = itemView.findViewById(R.id.tvUserNameValue)
        val tvCompanyName : TextView= itemView.findViewById(R.id.tvCompanyName)
        val tvInvoiceCodeValue : TextView= itemView.findViewById(R.id.tvInvoiceCodeValue)
        val tvPaidAmountValue : TextView= itemView.findViewById(R.id.tvPaidAmountValue)
        val tvDate : TextView= itemView.findViewById(R.id.tvDate)
    }

    interface ICustomListListener {
        fun onOrderSelected(orderRequest: OrderRequest, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}