package com.avdhootsolutions.aswack_shopkeeper.activities


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.adapters.ChatAdapter1
import com.avdhootsolutions.aswack_shopkeeper.enums.FileTypeEnum
import com.avdhootsolutions.aswack_shopkeeper.enums.IntentKeyEnum
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHandler
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropHelper
import com.avdhootsolutions.aswack_shopkeeper.imagecrop.CropParams
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.utilities.DialogHelper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.avdhootsolutions.aswack_shopkeeper.utilities.Prefrences
import com.avdhootsolutions.aswack_shopkeeper.viewmodels.GroupChatViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class ChatActivity : AppCompatActivity(),
    ChatAdapter1.ICustomListListener,
    DialogHelper.ChatDeleteListener, CropHandler {
    lateinit var mContext: Context
    lateinit var groupChatViewModel: GroupChatViewModel
    lateinit var chatAdapter: ChatAdapter1
    var notificationSendData =  NotificationSendData()
    var userName: String = ""

    lateinit var dialogHelper: DialogHelper

    var sdf = SimpleDateFormat("E, d MMM")
    var cal = Calendar.getInstance()
    var currentDate = sdf.format(cal.time)
    var yesterdayDate = ""

    lateinit var mCropParams: CropParams
    private var imagePath: String = ""
    private var currentPhotoPathName: String = ""
    lateinit var dialogProfilePicture: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mContext = this@ChatActivity

        init()
        initViewModel()
        clickListener()
    }


    /**
     * Click events
     */
    private fun clickListener() {
        ic_home.setOnClickListener {

        }

        tvCourierDetails.setOnClickListener {

            val intent = Intent(mContext, CourierDetailActivity::class.java)

            val courierDetails = CourierDetails()
            courierDetails.id = groupChatViewModel.productDataForSeller.productId
            courierDetails.seller_id = Helper().getLoginData(mContext).id
            courierDetails.seller_company_id = groupChatViewModel.productDataForSeller.companyId

            intent.putExtra(IntentKeyEnum.COURIER_DETAIL.name, Gson().toJson(courierDetails))
            startActivity(intent)


        }

//        icCamera.setOnClickListener {
//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            cameraImageResultLauncher.launch(cameraIntent)
//            // startActivityForResult(cameraIntent, CAMERA_REQUEST)
//        }

        icGallery.setOnClickListener {
            checkPermission()
        }

        ic_send.setOnClickListener {
            if (etMsg.text.toString().isNotEmpty()) {

                var productId = ""

                if (groupChatViewModel.fromRFQ) {
                    productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
                } else {
                    when {
                        groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
                            productId =
                                "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
                        }
                        groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
                            productId = "OFF_" + groupChatViewModel.productDataForSeller.offerId
                        }
                        groupChatViewModel.productDataForSeller.onlyProductDetails -> {
                            productId =
                                groupChatViewModel.productDataForSeller.categoryId + "_" + groupChatViewModel.productDataForSeller.productId
                        }

                        groupChatViewModel.productDataForSeller.onlyRFQDetails -> {
                            productId = "RFQ_" + groupChatViewModel.productDataForSeller.rfqId
                        }
                    }
                }

                groupChatViewModel.addChat(
                    etMsg.text.toString().trim { it <= ' ' }, userName,
                    productId, groupChatViewModel.userId, groupChatViewModel.seller_id
                )
                etMsg.setText("")

                if (notificationSendData.id?.isNotEmpty() == true && !groupChatViewModel.noticationSend){
                    groupChatViewModel.sendNotificationToUser(notificationSendData)

                    groupChatViewModel.noticationSend = true
                }


            }
        }

        iv_back.setOnClickListener {
            finish()
        }
    }


    private fun init() {

        userName = Helper().getLoginData(mContext).name!!

        dialogHelper = DialogHelper(mContext)

        val mLayoutManager = LinearLayoutManager(mContext)
        mLayoutManager.reverseLayout = true
        //        mLayoutManager.setStackFromEnd(true);
        rvChat.layoutManager = mLayoutManager
        rvChat.setHasFixedSize(true)
        rvChat.isNestedScrollingEnabled = true
        rvChat.addOnScrollListener(CustomScrollListener())

        chatAdapter = ChatAdapter1(mContext, this)
        rvChat.adapter = chatAdapter
        cal.add(Calendar.DATE, -1)
        yesterdayDate = sdf.format(cal.time)
    }

    private fun initViewModel() {

        groupChatViewModel = ViewModelProvider(this).get(GroupChatViewModel::class.java)

        val login = Helper().getLoginData(mContext)
        groupChatViewModel.seller_id = login.id!!
        if (intent.hasExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name)) {

            groupChatViewModel.productDataForSeller = Gson().fromJson(
                intent.getStringExtra(IntentKeyEnum.CHAT_PRODUCT_DATA.name).toString(),
                ProductDataForSeller::class.java
            )


            groupChatViewModel.userId = groupChatViewModel.productDataForSeller.userId

            notificationSendData.id = groupChatViewModel.userId
            notificationSendData.type = "U"
            notificationSendData.activity = "CHAT_LIST"
            notificationSendData.title = "ASWACK SHOPKKEPER"
            notificationSendData.body = login.name + " wants to send you a message"


            tvCompanyName.text = groupChatViewModel.productDataForSeller.companyName

            if (groupChatViewModel.productDataForSeller.productName.isEmpty() && groupChatViewModel.productDataForSeller.offerName.isEmpty()) {
                tvProductName.visibility = View.GONE

                when {
                    groupChatViewModel.productDataForSeller.rfqCode.isNotEmpty() -> {
                        notificationSendData.body = login.name + " wants to send you a message for RFQ Code : " + groupChatViewModel.productDataForSeller.rfqCode
                    }
                    groupChatViewModel.productDataForSeller.companyName.isNotEmpty() -> {
                        notificationSendData.body = login.name + " wants to send you a message for company : " + groupChatViewModel.productDataForSeller.companyName
                    }
                    else -> {
                        notificationSendData.body = login.name + " wants to send you a message"
                    }
                }



            } else if (groupChatViewModel.productDataForSeller.offerName.isNotEmpty()){
                tvProductName.text = groupChatViewModel.productDataForSeller.offerName

                notificationSendData.body = login.name + " wants to send you a message for offer " + groupChatViewModel.productDataForSeller.offerName

            }else if (groupChatViewModel.productDataForSeller.productName.isNotEmpty()){
                tvProductName.text = groupChatViewModel.productDataForSeller.productName

                notificationSendData.body = login.name + " wants to send you a message for product" + groupChatViewModel.productDataForSeller.productName

            }

            groupChatViewModel.seller_id = Helper().getLoginData(mContext).id!!

            var productId = ""
            when {
                groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
                    productId =
                        "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
                }
                groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
                    productId = "OFF_" + groupChatViewModel.productDataForSeller.offerId
                }
                groupChatViewModel.productDataForSeller.onlyProductDetails -> {

                    if (groupChatViewModel.productDataForSeller.categoryId == "12"){
                        tvCourierDetails.visibility = View.VISIBLE
                    }

                    productId =
                        groupChatViewModel.productDataForSeller.categoryId + "_" + groupChatViewModel.productDataForSeller.productId
                }
                groupChatViewModel.productDataForSeller.onlyRFQDetails -> {
                    productId = "RFQ_" + groupChatViewModel.productDataForSeller.rfqId
                    tvCompanyName.text = groupChatViewModel.productDataForSeller.rfqCode
                    tvProductName.visibility = View.GONE

                }
            }

            groupChatViewModel.getAllChatDataLive(
                productId,
                groupChatViewModel.userId,
                groupChatViewModel.seller_id
            )

        } else if (intent.hasExtra(IntentKeyEnum.RFQ_DATA.name)) {

            groupChatViewModel.fromRFQ = true

            groupChatViewModel.selectedRFQ = Gson().fromJson(
                intent.getStringExtra(IntentKeyEnum.RFQ_DATA.name).toString(),
                RFQList::class.java
            )

            groupChatViewModel.userId = groupChatViewModel.selectedRFQ.user_id!!

            tvCompanyName.text = groupChatViewModel.selectedRFQ.rfq_code
            tvProductName.visibility = View.GONE


            notificationSendData.id = groupChatViewModel.userId
            notificationSendData.type = "U"
            notificationSendData.activity = "CHAT_LIST"
            notificationSendData.title = "ASWACK SHOPKKEPER"
            notificationSendData.body = login.name + " wants to send you a message for RFQ Code : " + groupChatViewModel.selectedRFQ.rfq_code

            groupChatViewModel.getSellerDataAndAddRFQDetails(
                login.id!!,
                groupChatViewModel.selectedRFQ
            )
        }



        groupChatViewModel.sellerAddedLiveData.observe(this) {

            var productId = ""
            if (groupChatViewModel.fromRFQ) {
                productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!

                groupChatViewModel.getAllChatDataLive(
                    productId,
                    groupChatViewModel.selectedRFQ.user_id!!,
                    groupChatViewModel.seller_id!!
                )

            }
        }


        groupChatViewModel.isGetSellerLiveData.observe(this) { isSellerGet ->

            if (isSellerGet) {
                var productId = ""
                if (groupChatViewModel.fromRFQ) {
                    productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
                    groupChatViewModel.getAllChatDataLive(
                        productId,
                        groupChatViewModel.selectedRFQ.user_id!!,
                        groupChatViewModel.seller_id
                    )
                }
            } else {
                val login = Helper().getLoginData(mContext)
                val sellerDataChat = SellerDataChat()
                sellerDataChat.sellerId = login.id!!
                sellerDataChat.sellerName = login.name!!

                val productDataForSeller = ProductDataForSeller()

                if (groupChatViewModel.fromRFQ) {

                    productDataForSeller.rfqId =
                        groupChatViewModel.selectedRFQ.id!!

                    productDataForSeller.rfqCode =
                        groupChatViewModel.selectedRFQ.rfq_code!!

                    productDataForSeller.categoryId =
                        groupChatViewModel.selectedRFQ.master_category_id!!

                    productDataForSeller.userId = groupChatViewModel.selectedRFQ.user_id!!
                    productDataForSeller.userName = groupChatViewModel.selectedRFQ.user_name!!


                    productDataForSeller.companyId =
                        groupChatViewModel.selectedRFQ.seller_company_id!!

                    productDataForSeller.onlyRFQDetails = true
                }

                sellerDataChat.productDataList.add(productDataForSeller)

                groupChatViewModel.addSellerData(sellerDataChat)
            }
        }



        groupChatViewModel.getChatLiveDta().observe(this) { chatList ->
            chatAdapter.setList(chatList, userName)
            rvChat.scrollToPosition(0)
        }


        groupChatViewModel.getImageUploadLiveData().observe(this) { isUploaded ->
            if (isUploaded) {
                Toast.makeText(mContext, "Successfully uploaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }
        }




    }

//    //method to show file chooser
//    private fun showFileChooser() {
//        val intent = Intent()
//        intent.setType("image/*")
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        imageResultLauncher.launch(intent)
//
//        //   startActivityForResult(Intent.createChooser(intent, "Select Picture"), FileTypeEnum.IMAGE.ordinal());
//    }

//    var imageResultLauncher: ActivityResultLauncher<Intent> =
//        registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult(),
//            object : ActivityResultCallback<ActivityResult?> {
//                override fun onActivityResult(result: ActivityResult?) {
//                    if (result?.resultCode == Activity.RESULT_OK) {
//                        // Here, no request code
//                        val data: Intent? = result.data
//                        filePath = data?.getData()
//                        Log.e("getData ", filePath.toString())
//
//                        // Generate random id
//                        dialogHelper.showImageVideoToSendDialog(filePath,
//                            FileTypeEnum.IMAGE.ordinal,
//                            object : DialogHelper.ImageVideoSenderListener {
//                                override fun onSendButton(
//                                    imageVideoURI: Uri?,
//                                    FiletType: Int,
//                                    text: String?,
//                                ) {
//                                    val randomImageId = UUID.randomUUID().toString()
//                                    if (imageVideoURI != null) {
//                                        if (text != null) {
//                                            var  productId = ""
//
//                                            if (groupChatViewModel.fromRFQ){
//                                                productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
//                                            }else{
//                                                when {
//                                                    groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
//                                                        productId = "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
//                                                        productId = "OFF_" + groupChatViewModel.productDataForSeller.offerId
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyProductDetails -> {
//                                                        productId =groupChatViewModel.productDataForSeller.categoryId + "_" + groupChatViewModel.productDataForSeller.productId
//                                                    }
//                                                }
//                                            }
//
//
//
//                                            groupChatViewModel.addImageOrVide(imageVideoURI,
//                                                randomImageId,
//                                                userName,
//                                                FiletType,
//                                                text,
//                                                mContext,
//                                                productId, groupChatViewModel.userId, groupChatViewModel.seller_id)
//                                        }
//                                    }
//                                }
//                            })
//                        //                                progressbar.setVisibility(View.VISIBLE);
//                    }
//                }
//            })
//
//
//    var cameraImageResultLauncher: ActivityResultLauncher<Intent> =
//        registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult(),
//            object : ActivityResultCallback<ActivityResult?> {
//                override fun onActivityResult(result: ActivityResult?) {
//                    if (result?.resultCode == Activity.RESULT_OK) {
//                        // Here, no request code
//                        val data: Intent? = result.data
//                        filePath = data?.getData()
//                        Log.e("getData ", filePath.toString())
//
//                        // Generate random id
//                        dialogHelper.showImageVideoToSendDialog(filePath,
//                            FileTypeEnum.IMAGE.ordinal,
//                            object : DialogHelper.ImageVideoSenderListener {
//                                override fun onSendButton(
//                                    imageVideoURI: Uri?,
//                                    FiletType: Int,
//                                    text: String?,
//                                ) {
//                                    val randomImageId = UUID.randomUUID().toString()
//                                    if (imageVideoURI != null) {
//                                        if (text != null) {
//
//                                            var  productId = ""
//                                            if (groupChatViewModel.fromRFQ){
//                                                productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
//                                            }else{
//                                                when {
//                                                    groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
//                                                        productId = "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
//                                                        productId = "OFF_" + groupChatViewModel.productDataForSeller.offerId
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyProductDetails -> {
//                                                        productId = groupChatViewModel.productDataForSeller.categoryId + "_" +groupChatViewModel.productDataForSeller.productId
//                                                    }
//                                                }
//                                            }
//                                            groupChatViewModel.addImageOrVide(imageVideoURI,
//                                                randomImageId,
//                                                userName,
//                                                FiletType,
//                                                text,
//                                                mContext,
//                                                productId, groupChatViewModel.userId, groupChatViewModel.seller_id!!)
//                                        }
//                                    }
//                                }
//                            })
//                        //                                progressbar.setVisibility(View.VISIBLE);
//                    }
//                }
//            })

    // choose a video from phone storage
//    private fun choosevideo() {
//        Log.e("choose ", "video")
//        val intent = Intent()
//        intent.setType("video/*")
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        videoResultLauncher.launch(intent)
//        //        startActivityForResult(intent,FileTypeEnum.VIDEO.ordinal());
//    }

//    var videoResultLauncher: ActivityResultLauncher<Intent> =
//        registerForActivityResult<Intent, ActivityResult>(
//            ActivityResultContracts.StartActivityForResult(),
//            object : ActivityResultCallback<ActivityResult?> {
//                override fun onActivityResult(result: ActivityResult?) {
//                    if (result?.resultCode == Activity.RESULT_OK) {
//                        // Here, no request code
//                        val data: Intent? = result.data
//                        filePath = data?.getData()
//                        dialogHelper.showImageVideoToSendDialog(filePath,
//                            FileTypeEnum.VIDEO.ordinal,
//                            object : DialogHelper.ImageVideoSenderListener {
//                                override fun onSendButton(
//                                    imageVideoURI: Uri?,
//                                    FiletType: Int,
//                                    text: String?,
//                                ) {
//                                    val randomImageId = UUID.randomUUID().toString()
//                                    if (imageVideoURI != null) {
//                                        if (text != null) {
//
//                                            var  productId = ""
//                                            if (groupChatViewModel.fromRFQ){
//                                                productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
//                                            }else{
//                                                when {
//                                                    groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
//                                                        productId = "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
//                                                        productId = "OFF_" + groupChatViewModel.productDataForSeller.offerId
//                                                    }
//                                                    groupChatViewModel.productDataForSeller.onlyProductDetails -> {
//                                                        productId = groupChatViewModel.productDataForSeller.categoryId + "_" +groupChatViewModel.productDataForSeller.productId
//                                                    }
//                                                }
//                                            }
//                                            groupChatViewModel.addImageOrVide(imageVideoURI,
//                                                randomImageId,
//                                                userName,
//                                                FiletType,
//                                                text,
//                                                mContext,
//                                                productId, groupChatViewModel.userId, groupChatViewModel.seller_id)
//                                        }
//                                    }
//                                }
//                            })
//                    }
//                }
//            })

//    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == FileTypeEnum.IMAGE.ordinal && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
//            filePath = data.getData()
//
////            dialogHelper.showImageVideoToSendDialog(filePath, FileTypeEnum.IMAGE.ordinal(), this);
//        }
//    }

    override fun onItemClicked(chatMessage: ChatMessage) {
        if (!chatMessage.videoUrl.isEmpty()) {
            val intent = Intent(mContext, VideoViewActivity::class.java)
            intent.putExtra("videoUrl", chatMessage.videoUrl)
            startActivity(intent)
        } else {
            dialogHelper.showImageVideoDialog(chatMessage)
        }
    }

    override fun onItemDelete(chatMessage: ChatMessage) {
        dialogHelper.showDeleteDialog(chatMessage, this)
    }

    override fun onMapClicked(chatMessage: ChatMessage) {
        val intent = Intent(this, ShowLocationOnMapActivity::class.java)
        intent.putExtra(IntentKeyEnum.LATITUDE.name, chatMessage.latitude)
        intent.putExtra(IntentKeyEnum.LONGITUDE.name, chatMessage.longitude)
        intent.putExtra(IntentKeyEnum.FULL_ADDRESS.name, chatMessage.text)
        intent.putExtra(IntentKeyEnum.USER_NAME.name, chatMessage.user)
        startActivity(intent)
    }

    override fun onDeleteButton(chatMessage: ChatMessage) {
        groupChatViewModel.deleteChat(
            chatMessage,
            groupChatViewModel.productDataForSeller.productId,
            groupChatViewModel.userId,
            groupChatViewModel.seller_id
        )
    }

    inner class CustomScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> tvDate.setVisibility(View.GONE)
                RecyclerView.SCROLL_STATE_DRAGGING -> {
                    println("Scrolling now")
                    tvDate.setVisibility(View.VISIBLE)
                }
                RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager: LinearLayoutManager =
                recyclerView.getLayoutManager() as LinearLayoutManager
            val visiblePosition: Int = layoutManager.findLastVisibleItemPosition()
            if (visiblePosition > -1) {
                if (groupChatViewModel.getChatLiveDta().getValue()?.isNotEmpty() == true) {
                    val sdf = SimpleDateFormat("E, d MMM")
                    val chatDate = sdf.format(
                        groupChatViewModel.getChatLiveDta().getValue()!!
                            .get(visiblePosition).timestamp
                    )
                    when (chatDate) {
                        currentDate -> {
                            tvDate.setText(getResources().getString(R.string.today))
                        }
                        yesterdayDate -> {
                            tvDate.setText(getResources().getString(R.string.yesterday))
                        }
                        else -> {
                            tvDate.setText(chatDate)
                        }
                    }
                }
            }
            when {
                dx > 0 -> {
                    println("Scrolled Right")
                }
                dx < 0 -> {
                    println("Scrolled Left")
                }
                else -> {
                    println("No Horizontal Scrolled")
                }
            }
            when {
                dy > 0 -> {
                    println("Scrolled Downwards")
                }
                dy < 0 -> {
                    println("Scrolled Upwards")
                }
                else -> {
                    println("No Vertical Scrolled")
                }
            }
        }
    }

    private fun showProfilePicDialog() {
        mCropParams = CropParams((mContext as Activity?)!!)
        dialogProfilePicture = Dialog(mContext, R.style.picture_dialog_style)
        dialogProfilePicture.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogProfilePicture.setContentView(R.layout.dialog_choose_image)
        val wlmp = dialogProfilePicture.window?.attributes
        wlmp?.gravity = Gravity.BOTTOM
        wlmp?.width = WindowManager.LayoutParams.FILL_PARENT
        wlmp?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialogProfilePicture.window?.attributes = wlmp
        dialogProfilePicture.show()
        val tvCamera = dialogProfilePicture.findViewById<TextView>(R.id.tvCamera)
        val tvGallery = dialogProfilePicture.findViewById<TextView>(R.id.tvGallery)
        val tvCancel = dialogProfilePicture.findViewById<TextView>(R.id.tvCancel)
        tvGallery.setOnClickListener {
            dialogProfilePicture.dismiss()
            openGallery()
        }
        tvCamera.setOnClickListener {
            dialogProfilePicture.dismiss()
            openCamera()
        }
        tvCancel.setOnClickListener { dialogProfilePicture.dismiss() }
    }

    /**
     * Check permission for external storage
     */
    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            showProfilePicDialog()
        } else {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {


                Log.e("permission ", "granted")

                showProfilePicDialog()
            } else {
                ActivityCompat.requestPermissions(
                    (mContext as Activity?)!!,
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    fun openGallery() {
        mCropParams.refreshUri()
        mCropParams.enable = false
        mCropParams.compress = false
        val intent = CropHelper.buildGalleryIntent(mCropParams, mContext as Activity)
        startActivityForResult(intent, CropHelper.REQUEST_CROP)
    }

    private fun openCamera() {
        mCropParams.refreshUri()
        mCropParams.enable = false
        mCropParams.compress = false
        val intent = CropHelper.buildCameraIntent(mCropParams, mContext as Activity)
        startActivityForResult(intent, CropHelper.REQUEST_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropHelper.REQUEST_CROP) {
            CropHelper.handleResult(this, requestCode, resultCode, data)
        } else if (requestCode == CropHelper.REQUEST_CAMERA) {
            CropHelper.handleResult(this, requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showProfilePicDialog()
                } else {
                    // permission denied
                }
            }
        }
    }

    override fun onPhotoCropped(uri: Uri) {
        if (!mCropParams!!.compress) {
            imagePath = uri.path.toString()
            Log.d("onPhotoCropped", "imagePath==$imagePath")
            val tempFile = File(imagePath)
            Log.d("length", "onPhotoCropped: " + tempFile.length())
            if (tempFile.length() >= Prefrences.IMAGE_LIMIT) {
                val builder = AlertDialog.Builder(
                    mContext!!
                )
                builder.setMessage(getString(R.string.image_size_restriction_message))
                builder.setPositiveButton(getString(R.string.ok)) { dialog, which -> dialog.dismiss() }
                builder.show()
                builder.setCancelable(false)
            } else {
                setImage(imagePath)
            }
        }
    }

    private fun setImage(imagePath: String) {
        currentPhotoPathName = imagePath

        //  var mulPart: Part? = null
        if (currentPhotoPathName != null && !TextUtils.isEmpty(currentPhotoPathName)) {
            val compressImg = compressImage(currentPhotoPathName!!)

            compressImg?.let {

                dialogHelper.showImageVideoToSendDialog(Uri.fromFile(File(it)),
                    FileTypeEnum.IMAGE.ordinal,
                    object : DialogHelper.ImageVideoSenderListener {
                        override fun onSendButton(
                            imageVideoURI: Uri?,
                            FiletType: Int,
                            text: String?,
                        ) {
                            val randomImageId = UUID.randomUUID().toString()
                            if (imageVideoURI != null) {
                                if (text != null) {
                                    var productId = ""

                                    if (groupChatViewModel.fromRFQ) {
                                        productId = "RFQ_" + groupChatViewModel.selectedRFQ.id!!
                                    } else {
                                        when {
                                            groupChatViewModel.productDataForSeller.onlyCompanyDetail -> {
                                                productId =
                                                    "COM_" + groupChatViewModel.productDataForSeller.companyId + "_${groupChatViewModel.productDataForSeller.categoryId}"
                                            }
                                            groupChatViewModel.productDataForSeller.onlyOfferDetails -> {
                                                productId =
                                                    "OFF_" + groupChatViewModel.productDataForSeller.offerId
                                            }
                                            groupChatViewModel.productDataForSeller.onlyProductDetails -> {
                                                productId =
                                                    groupChatViewModel.productDataForSeller.categoryId + "_" + groupChatViewModel.productDataForSeller.productId
                                            }
                                        }
                                    }



                                    groupChatViewModel.addImageOrVide(
                                        imageVideoURI,
                                        randomImageId,
                                        userName,
                                        FiletType,
                                        text,
                                        mContext,
                                        productId,
                                        groupChatViewModel.userId,
                                        groupChatViewModel.seller_id
                                    )
                                }
                            }
                        }
                    })

            }


            val compressfile = File(compressImg)
//            val file = File(compressImg)
//            val reqSpecificationImage: RequestBody =
//                create.create(parse.parse("multipart/form-data"), file)
//            mulPart = createFormData.createFormData("file", file.name, reqSpecificationImage)
            // RequestBody req_profile_id = RequestBody.create(MediaType.parse("text/plain"), Utils.getPrefData(Prefrences.USER_ID, mContext));

            // apiUpdateImage(req_profile_id, mulPart);


            //pass it like this
            //pass it like this
            val file = File(compressImg)
//            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
//
//// MultipartBody.Part is used to send also the actual file name
//
//// MultipartBody.Part is used to send also the actual file name
//            val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//// add another part within the multipart request
//
//// add another part within the multipart request
//            val fullName = RequestBody.create(MediaType.parse("multipart/form-data"), "Your Name")
//
//            service.updateProfile(id, fullName, body, other)


        }
    }

    override fun onCompressed(uri: Uri) {
        imagePath = uri.path.toString()
        setImage(imagePath)
    }

    override fun onCancel() {}
    override fun onFailed(message: String) {}
    override fun handleIntent(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

    override fun onDestroy() {
        CropHelper.clearCacheDir()
        super.onDestroy()
    }

    override fun getCropParams(): CropParams {
        return mCropParams!!
    }

    fun compressImage(imageUri: String): String? {
        val u = Uri.fromFile(File(imageUri))
        var imageStream: InputStream? = null
        try {
            imageStream = mContext!!.contentResolver.openInputStream(u)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        var selectedImage = BitmapFactory.decodeStream(imageStream)
        selectedImage =
            getResizedBitmap(selectedImage, 1200) // 400 is for example, replace with desired size
        val exif: ExifInterface
        try {
            exif = ExifInterface(getRealPathFromURI(imageUri)!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION, 0
            )
            Log.d("EXIF", "Exif: $orientation")
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 3) {
                matrix.postRotate(180f)
                Log.d("EXIF", "Exif: $orientation")
            } else if (orientation == 8) {
                matrix.postRotate(270f)
                Log.d("EXIF", "Exif: $orientation")
            }
            selectedImage = Bitmap.createBitmap(
                selectedImage, 0, 0,
                selectedImage.width, selectedImage.height, matrix,
                true
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filename = filename
        try {
            out = FileOutputStream(filename)

//          write the compressed bitmap at the destination specified by filename.
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filename
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    val filename: String?
        get() {
            val mediaStorageDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Android/data/"
                        + mContext!!.applicationContext.packageName
                        + "/MyFolder"
            )
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            val mediaFile: File
            val mImageName = System.currentTimeMillis().toString() + ".jpg"
            mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
            return mediaFile.toString()
        }

    private fun getRealPathFromURI(contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = mContext!!.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }


}