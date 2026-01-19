package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.CustomPackageData
import com.avdhootsolutions.aswack_shopkeeper.models.PackageData
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper

class CustomPackageListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener,

    ) : RecyclerView.Adapter<CustomPackageListAdapter.CustomListViewHolder>() {

    private var itemList: List<CustomPackageData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_custom_package, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvRequestCodeValue.text = itemList[position].request_code
        holder.tvPriceValue.text = Helper().getCurrencySymbol(context) + itemList[position].price

        val privillage = itemList[position].post + " ${context.resources.getString(R.string.posts)}," +
                " ${itemList[position].offer} ${context.resources.getString(R.string.offers)}" +
                ", ${itemList[position].notification} ${context.resources.getString(R.string.notification)}"

        holder.tvPrivillageValue.text = privillage
        holder.tvDurationValue.text = itemList[position].duration

        holder.tvBuyNow.setOnClickListener {
            iCustomListListener.onCustomPackageBuy(itemList[position], position)
        }
    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<CustomPackageData>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvRequestCodeValue : TextView = itemView.findViewById<TextView?>(R.id.tvRequestCodeValue)
        val tvPriceValue : TextView = itemView.findViewById<TextView?>(R.id.tvPriceValue)
        val tvPrivillageValue : TextView = itemView.findViewById<TextView?>(R.id.tvPrivillageValue)
        val tvDurationValue : TextView = itemView.findViewById<TextView?>(R.id.tvDurationValue)
        val tvBuyNow: TextView = itemView.findViewById<TextView?>(R.id.tvBuyNow)


    }

    interface ICustomListListener {

        // On click of buy now button
        fun onCustomPackageBuy(customPackageData: CustomPackageData, position: Int)

    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}