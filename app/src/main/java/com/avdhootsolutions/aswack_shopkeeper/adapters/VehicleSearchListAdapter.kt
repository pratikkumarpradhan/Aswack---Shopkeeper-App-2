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
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class VehicleSearchListAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<VehicleSearchListAdapter.CustomListViewHolder>() {

    private var itemList: List<SellVehicle> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_search_car, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {
        if (!itemList[position].image1.isNullOrEmpty()){
            Glide.with(context).load(itemList[position].image1).placeholder(R.drawable.placeholder).into(holder.rivCar)
        }

        holder.tvTitle.text = itemList[position].title
        holder.tvNameValue.text = itemList[position].vehicle_type_name + ", " + itemList[position].vehicle_model_name
        holder.tvYear.text = itemList[position].vehicle_year_name
        holder.tvPrice.text = Helper().getCurrencySymbol(context) + itemList[position].price
        holder.tvLocation.text = ""
        holder.tvKmDriven.text = itemList[position].driven_km
     //   holder.tvPostedOn.text = itemList[position].

        holder.itemView.setOnClickListener {
            iCustomListListener.onSoldvehicleSelected(itemList[position], position)
        }

    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<SellVehicle>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val rivCar : RoundedImageView = itemView.findViewById(R.id.rivCar)
        val tvTitle: TextView= itemView.findViewById(R.id.tvTitle)
        val tvNameValue : TextView= itemView.findViewById(R.id.tvName)
        val tvPrice : TextView= itemView.findViewById(R.id.tvPrice)
        val tvKmDriven : TextView= itemView.findViewById(R.id.tvKmDriven)
        val tvYear : TextView= itemView.findViewById(R.id.tvYear)
        val tvLocation : TextView= itemView.findViewById(R.id.tvLocation)

    }

    interface ICustomListListener {
        fun onSoldvehicleSelected(sellVehicle: SellVehicle, position: Int)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}