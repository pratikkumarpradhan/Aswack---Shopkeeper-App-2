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
import java.util.ArrayList


class PurchasePackageListViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var regularPackageDataListLiveData: MutableLiveData<ArrayList<PurchasedPackageData>> = MutableLiveData<ArrayList<PurchasedPackageData>>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    var companyid = ""


    /**
     * Get Purchased Packages List
     */
     fun getPurchasedPackageList(login : Login) {

        Log.e("request of purchased Package", Gson().toJson(login))

            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetPurchasedPackageList(login)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of Purchased Package", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeRegularPackageList = object : TypeToken<ArrayList<PurchasedPackageData>>() {}.type
                        val regularList: ArrayList<PurchasedPackageData> =
                            gson.fromJson(gson.toJson(datas.data), typeRegularPackageList)
                        regularPackageDataListLiveData.setValue(regularList)
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
