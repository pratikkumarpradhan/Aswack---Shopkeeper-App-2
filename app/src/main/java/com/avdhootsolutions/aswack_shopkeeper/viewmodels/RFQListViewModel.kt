package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.R
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.avdhootsolutions.aswack_shopkeeper.service.FirebaseAuthServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class RFQListViewModel : ViewModel() {

    /**
     * Live data for success
     */
    var successMessageLiveData = MutableLiveData<Boolean>()


    /**
     * Live data for vehicle list
     */
    var rfqListLiveData: MutableLiveData<ArrayList<RFQList>> = MutableLiveData<ArrayList<RFQList>>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    var companyId = ""


    var mainCatId = ""

    var rfqList = ArrayList<RFQList>()


    init {

    }


    /**
     * Get RFQ list
     */
    fun getRFQList(productList: ProductList) {
        Log.e("request of rfq list", Gson().toJson(productList))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiRFQList(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
              //  Log.e("response of RFQ list", Gson().toJson(response.body()))


                if (datas.status == true) {
                    val gson = Gson()
                    val rfqListType = object : TypeToken<ArrayList<RFQList>>() {}.type
                    rfqList =
                        gson.fromJson(gson.toJson(datas.data), rfqListType)
                    rfqListLiveData.setValue(rfqList)

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


    /**
     * Send notication to user for RFQ response
     */
    fun sendNotificationToUserForRFQResponse(notificationSendData: NotificationSendData) {
        Log.e("request of notification send data", Gson().toJson(notificationSendData))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiSendNotificationForRFQResponse(notificationSendData)
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