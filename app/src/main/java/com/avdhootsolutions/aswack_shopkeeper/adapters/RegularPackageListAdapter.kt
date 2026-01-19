package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.PackageData
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper

class RegularPackageListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener,

    ) : RecyclerView.Adapter<RegularPackageListAdapter.CustomListViewHolder>() {

    private var itemList: List<PackageData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_regular_package, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvPnameValue.text = itemList[position].type
        holder.tvtaglinevalue.text = itemList[position].description
        holder.tvPacpriceValue.text = Helper().getCurrencySymbol(context) + itemList[position].price

        val privillage = itemList[position].post + " ${context.resources.getString(R.string.posts)}," +
                " ${itemList[position].offer} ${context.resources.getString(R.string.offers)}" +
                ", ${itemList[position].notification} ${context.resources.getString(R.string.notification)}"

        holder.tvprivilegevalue.text = privillage
        holder.tvdescvalue.text = itemList[position].description
//        holder.tvOriginalPriceValue.text = itemList[position].original_price
//        holder.tvOfferPriceValue.text = itemList[position].offer_price
       // holder.tvSerialNumberValue.text = itemList[position].ser



        holder.itemView.setOnClickListener {
            iCustomListListener.onPackageSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<PackageData>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivpackage : ImageView = itemView.findViewById(R.id.ivpackage)
        val tvPnameValue : TextView = itemView.findViewById<TextView?>(R.id.tvPnameValue)
        val tvtaglinevalue : TextView = itemView.findViewById<TextView?>(R.id.tvtaglinevalue)
        val tvPacpriceValue : TextView = itemView.findViewById<TextView?>(R.id.tvPacpriceValue)
        val tvprivilegevalue : TextView = itemView.findViewById<TextView?>(R.id.tvprivilegevalue)
        val tvdescvalue : TextView = itemView.findViewById<TextView?>(R.id.tvdescvalue)


    }

    interface ICustomListListener {
        fun onPackageSelected(packageDataItem: PackageData, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}