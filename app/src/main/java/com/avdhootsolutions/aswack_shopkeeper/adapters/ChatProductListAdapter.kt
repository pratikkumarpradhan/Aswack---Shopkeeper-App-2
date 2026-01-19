package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.models.ProductDataForSeller
import com.avdhootsolutions.aswack_shopkeeper.R

class ChatProductListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<ChatProductListAdapter.CustomListViewHolder>() {

    private var itemList: ArrayList<ProductDataForSeller> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_chat_list, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvPersonNameValue.text = itemList[position].userName

        when {
            itemList[position].onlyCompanyDetail -> {
                holder.tvProductName.text = context.resources.getString(R.string.company_name_colun)
                holder.tvProductNameValue.text = itemList[position].companyName

                holder.tvOfferName.visibility = View.GONE
                holder.tvOfferNameValue.visibility = View.GONE

            }
            itemList[position].onlyOfferDetails -> {
                holder.tvProductName.text = context.resources.getString(R.string.product_name_colun)
                if (itemList[position].productName.isNullOrEmpty()){
                    holder.tvProductNameValue.text = "NA"
                }else{
                    holder.tvProductNameValue.text = itemList[position].productName
                }
                holder.tvOfferNameValue.text = itemList[position].offerName

                holder.tvOfferName.visibility = View.VISIBLE
                holder.tvOfferNameValue.visibility = View.VISIBLE



            }
            itemList[position].onlyProductDetails -> {
                holder.tvOfferName.visibility = View.GONE
                holder.tvOfferNameValue.visibility = View.GONE

                holder.tvProductName.text = context.resources.getString(R.string.product_name_colun)
                holder.tvProductNameValue.text = itemList[position].productName
            }
            itemList[position].onlyRFQDetails -> {
                holder.tvProductName.text = context.resources.getString(R.string.rfq_code_colun)
                holder.tvProductNameValue.text = itemList[position].rfqCode

                holder.tvOfferName.visibility = View.GONE
                holder.tvOfferNameValue.visibility = View.GONE

            }
        }


        holder.itemView.setOnClickListener {
            iCustomListListener.onItemSelected(itemList[position])
        }

        if (itemList[position].quotationId.isNullOrEmpty()){
            holder.tvViewQuotation.visibility = View.GONE

            // If company chat then add quotation button will be hide
            if (itemList[position].onlyCompanyDetail){
                holder.tvAddQuotation.visibility = View.GONE
            }else{
                holder.tvAddQuotation.visibility = View.VISIBLE
            }
        }else{
            holder.tvAddQuotation.visibility = View.GONE
            holder.tvViewQuotation.visibility = View.VISIBLE
        }

        holder.tvAddQuotation.setOnClickListener {
            iCustomListListener.onAddQuotationSelected(itemList[position])
        }

        holder.tvViewQuotation.setOnClickListener {
            iCustomListListener.onAddQuotationSelected(itemList[position])
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: ArrayList<ProductDataForSeller>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvProductName : TextView = itemView.findViewById(R.id.tvProductName)
        val tvPersonNameValue : TextView = itemView.findViewById(R.id.tvPersonNameValue)
        val tvProductNameValue : TextView = itemView.findViewById(R.id.tvProductNameValue)
        var tvOfferName: TextView = itemView.findViewById(R.id.tvOfferName)
        var tvOfferNameValue: TextView = itemView.findViewById(R.id.tvOfferNameValue)
        val tvAddQuotation: TextView = itemView.findViewById(R.id.tvAddQuotation)
        val tvViewQuotation: TextView = itemView.findViewById(R.id.tvViewQuotation)


    }

    interface ICustomListListener {
        fun onItemSelected(productDataForSeller: ProductDataForSeller)
        fun onAddQuotationSelected(productDataForSeller: ProductDataForSeller)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}