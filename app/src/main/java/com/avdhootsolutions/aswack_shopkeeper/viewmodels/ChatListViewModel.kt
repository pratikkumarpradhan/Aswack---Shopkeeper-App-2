package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.avdhootsolutions.aswack_shopkeeper.models.ProductDataForSeller
import com.avdhootsolutions.aswack_shopkeeper.models.SellerDataChat
import com.avdhootsolutions.aswack_shopkeeper.models.ChatMessage
import com.avdhootsolutions.aswack_shopkeeper.service.FirebaseChatService
import kotlin.collections.ArrayList


/**
 * ViewModel for menu list page
 */
class ChatListViewModel(application: Application) : AndroidViewModel(application),
    FirebaseChatService.ChatServiceListener, FirebaseChatService.UserServiceListener {


    var errorMessage = MutableLiveData<String>()


    /**
     * service class in which Firebase authentication operations are perform
     */
    val firebaseChatServices = FirebaseChatService()


    var productDataListLiveData = MutableLiveData<ArrayList<ProductDataForSeller>>()

    /**
     * chat list to show on page
     */
    private var productDataList = ArrayList<ProductDataForSeller>()

    var sellerId = ""

    var companyId = ""

    init {
        firebaseChatServices.setChatListner(this, this)

    }


    /**
     * Inser quotation id in seller and user table
     */
    fun addQuotationDetailsInSellerAndUser(productDataForSeller: ProductDataForSeller){
        firebaseChatServices.getUserDataAndAddQuotationDetails(productDataForSeller)
    }

    fun getSellerData(sellerId : String){
        firebaseChatServices.getUserDataFromSellerId(sellerId)
    }



    override fun getUserDetails(sellerDataChat: SellerDataChat?) {

        sellerDataChat?.let {
            val  productList = ArrayList<ProductDataForSeller>()

            for (i in it.productDataList.indices){
                if (it.productDataList[i].companyId == companyId){
                    productList.add(it.productDataList[i])
                }
            }

            productDataListLiveData.value = productList

        }?: kotlin.run {
            productDataListLiveData.value = ArrayList<ProductDataForSeller>()
        }


    }

    override fun isQuotationAddedInSellers(isAdd: Boolean,sellerId: String) {

        getSellerData(sellerId)
    }

    override fun isQuotationAddedInUser(isAdd: Boolean,productDataForSeller: ProductDataForSeller) {
        firebaseChatServices.getSellerDataAndAddQuotationDetails(sellerId,productDataForSeller)
    }
    override fun fetchChatList(categoryList: List<ChatMessage>) {

    }

    override fun imageUploaded(isSuccess: Boolean) {
        TODO("Not yet implemented")
    }


}
