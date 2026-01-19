package com.avdhootsolutions.aswack_shopkeeper.utilities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.Gravity
import com.bumptech.glide.Glide
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.Window
import androidx.cardview.widget.CardView
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.Country1Adapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.DialogCountryAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.SellingAdapter
import com.avdhootsolutions.aswack_shopkeeper.adapters.VehicleCategoryAdapter
import com.avdhootsolutions.aswack_shopkeeper.enums.FileTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_company_sign_up.*
import kotlinx.android.synthetic.main.dialog_image.*
import kotlinx.android.synthetic.main.dialog_selling.*
import kotlinx.android.synthetic.main.dialogue_contactus.*
import java.io.IOException
import java.util.ArrayList


class DialogHelper(val context: Context) {


    private var dialog = Dialog(context)

    companion object {
        const val TAG = "DialogHelper"
        const val PACKAGE = "package"
    }



    fun showImageVideoDialog(chatMessage: ChatMessage) {
        // dialog for to show list with title
        dialog = BottomSheetDialog(context, R.style.DialogStyle)
        dialog.window?.setGravity(Gravity.BOTTOM)
        // bug 3065 for landscape dialog not open
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(R.layout.dialog_image)


        dialog.show()

        if (!chatMessage.videoUrl.isEmpty()) {
            dialog.ivImage.visibility = View.GONE
            dialog.frameLayout.visibility = View.VISIBLE
            dialog.ivTransparent.visibility = View.GONE
            dialog.videoView.visibility = View.VISIBLE
            dialog.ivPause.visibility = View.GONE
            dialog.ivPlay.visibility = View.GONE
            Glide.with(context).load(chatMessage.videoUrl).into(dialog.ivVideoImage!!)
            dialog.videoView.setVideoURI(Uri.parse(chatMessage.videoUrl))
            dialog.videoView.start()
        } else {
            dialog.ivImage!!.visibility = View.VISIBLE
            dialog.frameLayout!!.visibility = View.GONE
            Glide.with(context!!).load(chatMessage.imageUrl).placeholder(R.drawable.placeholder)
                .into(
                    dialog.ivImage)
        }
        dialog.videoView.setOnClickListener {
            if (dialog.videoView.isPlaying) {
                if (dialog.ivTransparent.visibility == View.GONE) {
                    dialog.ivTransparent.visibility = View.VISIBLE
                    dialog.ivPause.visibility = View.VISIBLE
                    dialog.ivPlay.visibility = View.GONE
                } else {
                    dialog.ivTransparent.visibility = View.GONE
                    dialog.ivPause.visibility = View.GONE
                    dialog.ivPlay.visibility = View.GONE
                }
            }
        }
        dialog.ivPlay.setOnClickListener {
            if (!dialog.videoView.isPlaying) {
                dialog.ivTransparent.visibility = View.GONE
                dialog.ivPause.visibility = View.GONE
                dialog.ivPlay.visibility = View.GONE
                dialog.videoView.start()
            }
        }
        dialog.ivPause.setOnClickListener {
            if (dialog.videoView.isPlaying) {
                dialog.ivTransparent!!.visibility = View.VISIBLE
                dialog.ivPause.visibility = View.GONE
                dialog.ivPlay.visibility = View.VISIBLE
                dialog.videoView.pause()
            }
        }
    }

    /**
     * Show image or video for preview and show edittext for message
     *
     * @param imageOrVideoURI          uri of the image or video
     * @param imageVideoSenderListener
     */
    fun showImageVideoToSendDialog(
        imageOrVideoURI: Uri?,
        fileType: Int,
        imageVideoSenderListener: ImageVideoSenderListener
    ) {
        dialog = BottomSheetDialog(context, R.style.dialog_style)
        dialog.getWindow()!!.setGravity(Gravity.BOTTOM)
        dialog.setContentView(R.layout.dialog_image_video_chat_send)
        val ivImage = dialog.findViewById<ImageView>(R.id.ivImage)
        val videoView = dialog.findViewById<VideoView>(R.id.videoView)
        val frameLayout = dialog.findViewById<FrameLayout>(R.id.frameLayout)
        val ivVideoImage = dialog.findViewById<ImageView>(R.id.ivVideoImage)
        val ivTransparent = dialog.findViewById<ImageView>(R.id.ivTransparent)
        val ivPlay = dialog.findViewById<ImageView>(R.id.ivPlay)
        val ivPause = dialog.findViewById<ImageView>(R.id.ivPause)
        val etMsg = dialog.findViewById<EditText>(R.id.etMsg)
        val ivSend = dialog.findViewById<ImageView>(R.id.ivSend)
        dialog.show()
        if (fileType == FileTypeEnum.VIDEO.ordinal) {
            ivImage!!.visibility = View.GONE
            frameLayout!!.visibility = View.VISIBLE
            ivTransparent!!.visibility = View.GONE
            videoView!!.visibility = View.VISIBLE
            ivPause!!.visibility = View.GONE
            ivPlay!!.visibility = View.VISIBLE
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context!!.contentResolver, imageOrVideoURI)
                ivVideoImage!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            videoView.setVideoURI(imageOrVideoURI)
        } else {
            ivImage!!.visibility = View.VISIBLE
            frameLayout!!.visibility = View.GONE
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(context!!.contentResolver, imageOrVideoURI)
                ivImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        videoView!!.setOnClickListener {
            if (videoView.isPlaying) {
                if (ivTransparent!!.visibility == View.GONE) {
                    ivTransparent.visibility = View.VISIBLE
                    ivPause!!.visibility = View.VISIBLE
                    ivPlay!!.visibility = View.GONE
                } else {
                    ivTransparent.visibility = View.GONE
                    ivPause!!.visibility = View.GONE
                    ivPlay!!.visibility = View.GONE
                }
            }
        }
        ivPlay!!.setOnClickListener {
            if (!videoView.isPlaying) {
                ivTransparent!!.visibility = View.GONE
                ivPause!!.visibility = View.GONE
                ivPlay.visibility = View.GONE
                videoView.start()
            }
        }
        ivPause!!.setOnClickListener {
            if (videoView.isPlaying) {
                ivTransparent!!.visibility = View.VISIBLE
                ivPause.visibility = View.GONE
                ivPlay.visibility = View.VISIBLE
                videoView.pause()
            }
        }
        ivSend!!.setOnClickListener {
            imageVideoSenderListener.onSendButton(imageOrVideoURI,
                fileType,
                etMsg!!.text.toString().trim { it <= ' ' })
            dialog.dismiss()
        }
    }

    interface ImageVideoSenderListener {
        fun onSendButton(imageVideoURI: Uri?, FiletType: Int, text: String?)
    }

    fun showDeleteDialog(chatMessage: ChatMessage, chatDeleteListener: ChatDeleteListener) {
        dialog = BottomSheetDialog(context!!, R.style.dialog_style)
        dialog.getWindow()!!.setGravity(Gravity.BOTTOM)
        dialog.setContentView(R.layout.dialog_image_video_chat_delete)
        val ivImage = dialog.findViewById<ImageView>(R.id.ivImage)
        val videoView = dialog.findViewById<VideoView>(R.id.videoView)
        val frameLayout = dialog.findViewById<FrameLayout>(R.id.frameLayout)
        val ivVideoImage = dialog.findViewById<ImageView>(R.id.ivVideoImage)
        val ivTransparent = dialog.findViewById<ImageView>(R.id.ivTransparent)
        val ivPlay = dialog.findViewById<ImageView>(R.id.ivPlay)
        val ivPause = dialog.findViewById<ImageView>(R.id.ivPause)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)
        val tvChat = dialog.findViewById<TextView>(R.id.tvChat)
        val cvImage: CardView = dialog.findViewById(R.id.cvImage)
        dialog.show()
        if (!chatMessage.videoId.isEmpty()) {
            ivImage!!.visibility = View.GONE
            frameLayout!!.visibility = View.VISIBLE
            ivTransparent!!.visibility = View.GONE
            //            videoView.setVisibility(View.VISIBLE);
            ivPause!!.visibility = View.GONE
            ivPlay!!.visibility = View.VISIBLE
            Glide.with(context!!).load(chatMessage.videoUrl).placeholder(R.drawable.placeholder)
                .into(
                    ivVideoImage!!)
            //            videoView.setVideoURI(Uri.parse(chatMessage.getVideoUrl()));
        } else if (!chatMessage.imageId.isEmpty()) {
            ivImage!!.visibility = View.VISIBLE
            frameLayout!!.visibility = View.GONE
            Glide.with(context!!).load(chatMessage.imageUrl).placeholder(R.drawable.placeholder)
                .into(
                    ivImage)
        } else {
            cvImage.visibility = View.GONE
            tvChat!!.visibility = View.VISIBLE
            tvChat.text = chatMessage.text
        }
        tvDelete!!.setOnClickListener {
            chatDeleteListener.onDeleteButton(chatMessage)
            dialog.dismiss()
        }
    }

    interface ChatDeleteListener {
        fun onDeleteButton(chatMessage: ChatMessage)
    }

    interface RestTimeListener {
        fun getRestTime(restTime: Int)
    }

    /**
     * full height for dialog
     */
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    /**
     * Open buy or sell dialog
     */
     fun openBuySellDialog(vehicleFor: Int, title: String,vehicleList : ArrayList<Categories>,
                                  iCustomListListener: VehicleCategoryAdapter.ICustomListListener) {
        dialog = Dialog(context, R.style.picture_dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_selling)
        val wlmp = dialog.window?.attributes
        wlmp?.gravity = Gravity.CENTER
        wlmp?.width = WindowManager.LayoutParams.FILL_PARENT
        wlmp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = wlmp
        dialog.show()
        dialog.tvTitle.text = title
        dialog.icClose.setOnClickListener { dialog.dismiss() }

        dialog.rvSelling.layoutManager = GridLayoutManager(context, 2)
        dialog.rvSelling.setHasFixedSize(true)

        val adapter = VehicleCategoryAdapter(context, iCustomListListener)
        dialog.rvSelling.adapter = adapter

        adapter.setList(vehicleList)
    }

    /**
     * Show alert dialog for delete
     * @param deleteDialogListener listener
     */
    fun showDeleteProductDialog(deleteDialogListener : DeleteDialogListener){

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(R.string.dialogTitle)
        //set message for alert dialog
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes"){dialogInterface, which ->

            deleteDialogListener.isDeleteItem()
            dismissDialog()
        }


        //performing negative action
        builder.setNegativeButton("No"){dialogInterface, which ->
            dismissDialog()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    interface DeleteDialogListener{
        fun isDeleteItem()

    }


    /**
     * Show country list in dialg
     * @param countryList list of countries
     */
    fun showCountryListDialog(countryList : ArrayList<Country>, isCLick : Boolean){

        var isCLickOn = isCLick

        dialog = Dialog(context, R.style.picture_dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_countries)
        val wlmp = dialog.window?.attributes
        wlmp?.gravity = Gravity.CENTER
        wlmp?.width = WindowManager.LayoutParams.FILL_PARENT
        wlmp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = wlmp
        dialog.show()
        dialog.icClose.setOnClickListener { dialog.dismiss() }

        val adapter = DialogCountryAdapter(
            context,
            countryList
        )

        dialog.spCountry.adapter = adapter

        dialog.spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long,
            ) {

                if (isCLickOn){
                    dialog.dismiss()
                }else{
                    isCLickOn = true
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    /**
     * Show full image of the selected image
     */
    fun showFullImageDialog(imageUrl: String) {
        // dialog for to show list with title
        dialog = BottomSheetDialog(context, R.style.DialogStyle)
        dialog.window?.setGravity(Gravity.BOTTOM)
        // bug 3065 for landscape dialog not open
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.setContentView(R.layout.dialog_full_image)

        dialog.show()

        Glide.with(context).load(imageUrl).placeholder(R.drawable.placeholder).into(dialog.ivImage)
    }


    /**
     * Show alert dialog to show that shopekeeper's payment is still pending
     */
    fun showPaymentPendingDialog(){

        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle(R.string.alert)
        //set message for alert dialog
        builder.setMessage(R.string.payment_pending_message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Ok"){dialogInterface, which ->
            dismissDialog()
        }

        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    /**
     * Open contact us dialog
     */
    fun openContactUsDialogue() {
        val dialog = Dialog(context, R.style.picture_dialog_style)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialogue_contactus)
        val wlmp = dialog.window!!.attributes
        wlmp.gravity = Gravity.CENTER
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = wlmp

        dialog.show()
        val ivclose = dialog.findViewById<ImageView>(R.id.ivclose)
        ivclose.setOnClickListener { dialog.dismiss() }
    }

    fun dismissDialog(){
        dialog.dismiss()
    }
}