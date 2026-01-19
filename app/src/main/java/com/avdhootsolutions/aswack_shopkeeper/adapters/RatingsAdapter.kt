package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.Ratings
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

import java.io.File

class RatingsAdapter(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<RatingsAdapter.CustomListViewHolder>() {

    private var itemList: ArrayList<Ratings> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_rating, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        holder.tvTitle.text = itemList[position].review_title
        holder.ratingBar.rating = itemList[position].review_star.toString().toFloat()
        holder.tvName.text = itemList[position].user_name
        holder.tvDes.text = itemList[position].review_text
        holder.tvDate.text = itemList[position].created_datetime

        holder.tvSubmit.setOnClickListener {
            iCustomListListener.onReplyOfRating(itemList[position], holder.etShopkeeperReply.text.toString())
        }

        if (itemList[position].seller_reply.isNullOrEmpty()) {
            holder.etShopkeeperReply.visibility = View.VISIBLE
            holder.tvSubmit.visibility = View.VISIBLE
            holder.tvShopkeeperReply.visibility = View.GONE

        } else {
            holder.etShopkeeperReply.visibility = View.GONE
            holder.tvSubmit.visibility = View.GONE
            holder.tvShopkeeperReply.visibility = View.VISIBLE

            holder.tvShopkeeperReply.text =
                context.resources.getString(R.string.reply) + " : " + itemList[position].seller_reply
        }
    }

    /**
     * @param itemList list of the item
     */
    fun setList(itemList: ArrayList<Ratings>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvDes: TextView = itemView.findViewById(R.id.tvDes)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        val tvSubmit: TextView = itemView.findViewById(R.id.tvSubmit)
        val etShopkeeperReply: TextView = itemView.findViewById(R.id.etShopkeeperReply)
        val tvShopkeeperReply: TextView = itemView.findViewById(R.id.tvShopkeeperReply)

    }

    interface ICustomListListener {
        fun onReplyOfRating(ratings: Ratings, reply : String)


    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}