package com.avdhootsolutions.aswack_shopkeeper.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.ChatMessage
import com.bumptech.glide.Glide
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter1(
    private val context: Context,
    private val iCustomListListener: ICustomListListener

) : RecyclerView.Adapter<ChatAdapter1.CustomListViewHolder>() {

    private var itemList: List<ChatMessage> = mutableListOf()
    private var userName: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomListViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.row_chat, parent, false)
        return CustomListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CustomListViewHolder, position: Int) {

        val chatMessage = itemList[position]

//        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val sdf = SimpleDateFormat("E, d MMM")
        val cal = Calendar.getInstance()
        val currentDate: String = sdf.format(cal.time)
        cal.add(Calendar.DATE, -1);
        val yesterdayDate: String = sdf.format(cal.time);


       val chatDate : String =  sdf.format(chatMessage.timestamp)


        Log.e("currentDate ", currentDate)

        if (position == itemList.size-1)
        Log.e("chatDate ", chatDate)

        val dateFormat: DateFormat = SimpleDateFormat("h:mm a")
        var strDate: String = dateFormat.format(chatMessage.timestamp)

//        if (currentDate != chatDate){
//            strDate = "$chatDate  $strDate"
//        }


        if (chatMessage.showDate){
            holder.tvDate.visibility = View.VISIBLE

            if (chatDate == currentDate){
                holder.tvDate.text = context.resources.getString(R.string.today)
            }else if(chatDate == yesterdayDate){
                holder.tvDate.text = context.resources.getString(R.string.yesterday)
            } else
            {
                holder.tvDate.text = chatDate
            }

        }else{
            holder.tvDate.visibility = View.GONE
        }


        println("Converted String: $strDate")


            if (chatMessage.user != userName) {

                holder.clReceiver.visibility = View.GONE
                holder.clSender.visibility = View.VISIBLE

                if (chatMessage.imageUrl.isNotEmpty()) {
                    holder.clSenderImages.visibility = View.VISIBLE
                    holder.clSenderMsg.visibility = View.GONE

                    Glide.with(context).load(chatMessage.imageUrl).placeholder(R.drawable.placeholder).into(holder.ivSenderMsg)
                    holder.tvSenderTimeWithImage.text = strDate

                }  else if (chatMessage.text.isNotEmpty()) {
                    holder.clSenderImages.visibility = View.GONE
                    holder.clSenderMsg.visibility = View.VISIBLE

                    holder.tvSenderMsg.text = chatMessage.text
                    holder.tvSenderTime.text = strDate
                }

            } else {
                Log.e("userName hi", userName)
                holder.clReceiver.visibility = View.VISIBLE
                holder.clSender.visibility = View.GONE

                if (chatMessage.imageUrl.isNotEmpty()) {
                    holder.clReceiverImages.visibility = View.VISIBLE
                    holder.clReceiverMsg.visibility = View.GONE

                    Glide.with(context).load(chatMessage.imageUrl).placeholder(R.drawable.placeholder).into(holder.ivReceiverMsg)
                    holder.tvReceiverTimeWithImage.text = strDate

                }  else if (chatMessage.text.isNotEmpty()) {
                    holder.clReceiverImages.visibility = View.GONE
                    holder.clReceiverMsg.visibility = View.VISIBLE

                    holder.tvReceiverMsg.text = chatMessage.text
                    holder.tvReceiverTime.text = strDate
                }


            }

        holder.itemView.setOnClickListener {
            if (chatMessage.imageId.isNotEmpty() || chatMessage.videoId.isNotEmpty()){
                iCustomListListener.onItemClicked(chatMessage)
            }else if (chatMessage.latitude.isNotEmpty()){
                iCustomListListener.onMapClicked(chatMessage)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (chatMessage.user == userName){
                iCustomListListener.onItemDelete(chatMessage)
            }

            return@setOnLongClickListener true
        }




    }


    /**
     * @param itemList list of the item
     */
    fun setList(itemList: List<ChatMessage>, userName: String) {
        this.itemList = itemList
        this.userName = userName
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class CustomListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clSender: ConstraintLayout = itemView.findViewById(R.id.clSender)

        val clSenderMsg: ConstraintLayout = itemView.findViewById(R.id.clSenderMsg)
        val tvSenderMsg: TextView = itemView.findViewById(R.id.tvSenderMsg)
        val tvSenderTime: TextView= itemView.findViewById(R.id.tvSenderTime)

        val clSenderImages: CardView = itemView.findViewById(R.id.clSenderImages)
        val ivSenderMsg: ImageView = itemView.findViewById(R.id.ivSender)
        val tvSenderTimeWithImage: TextView= itemView.findViewById(R.id.tvSenderTimeWithImage)

        val clReceiver: ConstraintLayout = itemView.findViewById(R.id.clReceiver)

        val clReceiverMsg: ConstraintLayout = itemView.findViewById(R.id.clReceiverMsg)
        val tvReceiverTime: TextView = itemView.findViewById(R.id.tvReceiverTime)
        val tvReceiverMsg: TextView = itemView.findViewById(R.id.tvReceiverMsg)

        val clReceiverImages: CardView = itemView.findViewById(R.id.clReceiverImages)
        val ivReceiverMsg: ImageView = itemView.findViewById(R.id.ivReceiver)
        val tvReceiverTimeWithImage: TextView= itemView.findViewById(R.id.tvReceiverTimeWithImage)

        val tvDate: TextView= itemView.findViewById(R.id.tvDate)

    }

    interface ICustomListListener {
        fun onItemClicked(chatMessage: ChatMessage)
        fun onItemDelete(chatMessage: ChatMessage)
        fun onMapClicked(chatMessage: ChatMessage)
    }

    override fun getItemViewType(position: Int): Int {

        // example
        return position
    }
}