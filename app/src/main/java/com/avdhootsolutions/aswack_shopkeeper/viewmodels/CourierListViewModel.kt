package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
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


class CourierListViewModel : ViewModel() {


    /**
     * Live data for courier list
     */
    var courierListLiveData: MutableLiveData<ArrayList<CourierDetails>> = MutableLiveData<ArrayList<CourierDetails>>()


    /**
     * courier list
     */
    var courierList = ArrayList<CourierDetails>()

    var companyId = ""


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()



    init {


    }


    /**
     * Get Courier list
     */
     fun getCourierInquiryList(productList: ProductList) {

        Log.e("request of courier list", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetCourierList(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of courier list", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCourierList = object : TypeToken<ArrayList<CourierDetails>>() {}.type
                    courierList =
                        gson.fromJson(gson.toJson(datas.data), typeCourierList)
                    courierListLiveData.setValue(courierList)
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