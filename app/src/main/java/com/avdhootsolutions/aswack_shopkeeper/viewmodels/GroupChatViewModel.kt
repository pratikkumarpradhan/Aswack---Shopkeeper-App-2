package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.service.FirebaseChatService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


/**
 * ViewModel for menu list page
 */
class GroupChatViewModel(application: Application) : AndroidViewModel(application),
    FirebaseChatService.ChatServiceListener, FirebaseChatService.UserServiceListener,
    FirebaseChatService.SellerRFQServiceListener {

    var noticationSend = false


    /**
     * Live data for success
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    var errorMessage = MutableLiveData<String>()

    /**
     * live data for check seller added or not
     */
    var isGetSellerLiveData = MutableLiveData<Boolean>()



    /**
     * service class in which Firebase authentication operations are perform
     */
    val firebaseChatServices = FirebaseChatService()


    /**
     * chat list to show on page
     */
    private var chatList = ArrayList<ChatMessage>()



    /**
     * live data for item list
     */
    var chatListLiveData = MutableLiveData<List<ChatMessage>>()

    /**
     * live data for image uploading
     */
    var imageUploadedLiveData = MutableLiveData<Boolean>()


    /**
     * live data for user added
     */
    var sellerAddedLiveData = MutableLiveData<Boolean>()

    /**
     * live data for user added
     */
    var userDataLiveData = MutableLiveData<UserDataChat>()

    var productDataForSeller = ProductDataForSeller()

    var seller_id = ""

    var userId = ""

    var fromRFQ = false

    /**
     * Selected rfq details from rfq list page or rfq detail page
     */
    var selectedRFQ = RFQList()

    var companyId = ""
    var companyName = ""



    init {
        firebaseChatServices.setChatListner(this, this)
        firebaseChatServices.setSellerRFQListner(this)
    }


    fun getAllChatDataLive(productId: String,userId: String, sellerId : String){
        firebaseChatServices.getAllChatDataLive(productId,userId,sellerId)
    }

    /**
     * Get seller data from firestore database
     * If seller found from the data base, Then see that RFQ is available or not in that seller data
     * If RFQ is not available then add
     * And if RFQ is available then return
     */
    fun getSellerDataAndAddRFQDetails(userId : String, rfqList: RFQList){
        firebaseChatServices.getSellerAndAddUpdateRFQ(userId,rfqList)
    }


    /**
     * Get user data from firestore database
     * If user found from the data base, Then see that RFQ is available or not in that user data
     * If RFQ is not available then add
     * And if RFQ is available then return that data
     */
    fun getUserDataAndAddUpdateRFQ(userId: String, productData: ProductData){
        firebaseChatServices.getUserDataAndAddUpdateRFQ(userId,productData)
    }


    fun getChatLiveDta() : MutableLiveData<List<ChatMessage>>{
        return chatListLiveData
    }


    fun getImageUploadLiveData() : MutableLiveData<Boolean>{
        return imageUploadedLiveData
    }

    fun addChat(chat : String, userName : String, productId: String, userId: String, sellerId: String){

        firebaseChatServices.addChat(chat,userName,productId, userId, sellerId, getApplication() )
    }

    fun updateProductDataForUser(userDataChat: UserDataChat){
        firebaseChatServices.updateUserData(userDataChat)
    }





    fun addSellerData(sellerDataChat: SellerDataChat){
        firebaseChatServices.addSellerForChat(sellerDataChat)
    }


    fun getUserData(userId : String, productList: ProductList){
       // firebaseChatServices.getUserDataFromUserId(userId,productList)
    }


    fun deleteChat(chatMessage: ChatMessage, productId: String, userId: String, sellerId: String){
        firebaseChatServices.deleteChat(chatMessage, productId,userId, sellerId)
    }


    fun addImageOrVide(uri: Uri, randomImageId : String, userName : String, fileType : Int, chatText : String, context: Context, productId: String, userId: String, sellerId: String){
        firebaseChatServices.uploadImageOrVideo(chatText,userName, uri, randomImageId,context, fileType,this, productId,userId, sellerId)
    }


    override fun fetchChatList(chatList: List<ChatMessage>) {

        var dateToShow = ""

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        // val currentDate: String = sdf.format(Calendar.getInstance().time)

        for (i in chatList.size - 1 downTo 0) {
            var chatDate : String = sdf.format(chatList[i].timestamp)

            if (dateToShow != chatDate){
                dateToShow = chatDate
                chatList[i].showDate = true
            }
        }

        chatListLiveData.value = chatList
        this.chatList = chatList as ArrayList<ChatMessage>
    }

    override fun imageUploaded(isSuccess: Boolean) {
        imageUploadedLiveData.value = isSuccess
    }

    override fun addSellerdata(isSuccess: Boolean) {
        sellerAddedLiveData.value = isSuccess

      //  isUserAdded = isSuccess

    }

    override fun getUserDetailsAndAddedProduct(
        userId: String,
        userDataChat: UserDataChat?,
        productData: ProductData
    ) {

        userDataChat?.let {
            sellerAddedLiveData.value = true
        }?: kotlin.run {
            val userDataChat = UserDataChat()
            userDataChat.userId = userId
            userDataChat.productDataList.add(productData)

            firebaseChatServices.addUserForChat(userDataChat)

        }

    }

    override fun getSellerDetailsAndAddedProduct(
        sellerDataChat: SellerDataChat?,
        productDataForSeller: ProductDataForSeller?
    ) {
        sellerDataChat?.let {

            Log.e("comes from ", "Make product data for seller")

            val productData = ProductData()
            productData.rfqId = productDataForSeller!!.rfqId
            productData.rfqCode = productDataForSeller!!.rfqCode

            productData.categoryId = productDataForSeller!!.categoryId

            productData.companyId = productDataForSeller!!.companyId

            productData.sellerId = sellerDataChat.sellerId
            productData.sellerName = sellerDataChat.sellerName

            productData.onlyRFQDetails = productDataForSeller.onlyRFQDetails


            getUserDataAndAddUpdateRFQ(productDataForSeller.userId, productData)


        }?: kotlin.run {
            isGetSellerLiveData.value = false
        }
    }

    override fun getUserDetails(userDataChat: SellerDataChat?) {
     //   userDataLiveData.value = userDataChat
    }

    override fun isQuotationAddedInSellers(isAdd: Boolean, userId: String) {
        TODO("Not yet implemented")
    }

    override fun isQuotationAddedInUser(
        isAdd: Boolean,
        productDataForSeller: ProductDataForSeller
    ) {

    }



    /**
     * Send notication to user
     */
    fun sendNotificationToUser(notificationSendData: NotificationSendData) {
        Log.e("request of notification send data", Gson().toJson(notificationSendData))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiSendNotificationToUser(notificationSendData)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of notification", Gson().toJson(response.body()))


                if (datas.status == true) {
                    successMessageLiveData.value = true
                } else {
                    errorMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


}
