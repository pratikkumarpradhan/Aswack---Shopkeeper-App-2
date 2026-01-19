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
import com.avdhootsolutions.aswack_shopkeeper.models.PurchasedPackageData
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper

class PurchasedPackageListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener,

    ) : RecyclerView.Adapter<PurchasedPackageListAdapter.CustomListViewHolder>() {

    private var itemList: List<PurchasedPackageData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_purchased_package, parent, false)
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
        holder.tvDurationValue.text = itemList[position].duration
        holder.tvDateValue.text = itemList[position].start_date
        holder.tvEndDateValue.text = itemList[position].end_date
        holder.tvCompanyNameValue.text = itemList[position].seller_company_name

        var categories = ""

        for (i in itemList[position].categoryList.indices){
            categories = if(categories.isEmpty()){
                itemList[position].categoryList[i].category_name.toString()
            }else{
                categories + ", " + itemList[position].categoryList[i].category_name
            }
        }

        holder.tvCategoryValue.text = categories

        if(itemList[position].package_status == "1"){
            holder.tvStatusValue.text = context.getString(R.string.expired)
        }else if(itemList[position].package_status == "2"){
            holder.tvStatusValue.text = context.getString(R.string.running)
        }



        holder.itemView.setOnClickListener {
            iCustomListListener.onPackageSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<PurchasedPackageData>) {
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
        val tvDurationValue: TextView = itemView.findViewById<TextView?>(R.id.tvDurationValue)
        val tvDateValue: TextView = itemView.findViewById<TextView?>(R.id.tvDateValue)
        val tvEndDateValue: TextView = itemView.findViewById<TextView?>(R.id.tvEndDateValue)
        val tvStatusValue: TextView = itemView.findViewById<TextView?>(R.id.tvStatusValue)
        val tvCategoryValue: TextView = itemView.findViewById<TextView?>(R.id.tvCategoryValue)
        val tvCompanyNameValue: TextView = itemView.findViewById<TextView?>(R.id.tvCompanyNameValue)

    }

    interface ICustomListListener {
        fun onPackageSelected(packageDataItem: PurchasedPackageData, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}