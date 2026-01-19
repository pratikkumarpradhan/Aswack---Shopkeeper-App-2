package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationListViewModel : ViewModel() {


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    var companyId = ""

    var packageId = ""

    var mainCatId = ""


    /**
     * Live data for admin notification
     */
    var adminNotificationListLiveData = MutableLiveData<ArrayList<AdminNotificationList>>()

    /**
     * Live data for seller's category wise notification
     */
    var sellerNotificationListLiveData = MutableLiveData<ArrayList<AdminNotificationList>>()

    /**
     * Live data for package usage
     */
    var packageUsedLiveData: MutableLiveData<UsagesOfPackage> = MutableLiveData<UsagesOfPackage>()

    /**
     * Live data for usage fetching error
     */
    var errorOfUsageMessage = MutableLiveData<String>()
    init {
    }



    /**
     * get category notification list
     */
    fun apiGetNotificationList(login: Login) {

        Log.e("request of get category notification", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetCategoryNotification(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of get category notification", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeNotificationList = object : TypeToken<java.util.ArrayList<AdminNotificationList>>() {}.type
                  val adminNotificationList : ArrayList<AdminNotificationList> =
                        gson.fromJson(gson.toJson(datas.data), typeNotificationList)
                    sellerNotificationListLiveData.setValue(adminNotificationList)

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
     * get admin notification list
     */
    fun apiGetAdminNotificationList(login: Login) {

        Log.e("request of get admin notification", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetAdminNotification(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of admin notification", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeNotificationList = object : TypeToken<java.util.ArrayList<AdminNotificationList>>() {}.type
                    val adminNotificationList : ArrayList<AdminNotificationList> =
                        gson.fromJson(gson.toJson(datas.data), typeNotificationList)
                    adminNotificationListLiveData.setValue(adminNotificationList)


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
     * Get usage data
     */
    fun getUsageData(productList: ProductList) {

        Log.e("request of offer usage", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetUsageProductOfferNotification(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of offer usage", Gson().toJson(response.body()))

                if (datas.status == true) {
                    val gson = Gson()
                    val packageUsageListType = object : TypeToken<java.util.ArrayList<UsagesOfPackage>>() {}.type
                    val packageUsageList : java.util.ArrayList<UsagesOfPackage> =
                        gson.fromJson(gson.toJson(datas.data), packageUsageListType)
                    packageUsedLiveData.setValue(packageUsageList[0])

                } else {
                    errorOfUsageMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorOfUsageMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }

}