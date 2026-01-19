package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import okhttp3.MultipartBody

import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.ArrayList


class CourierDetailViewModel : ViewModel() {

    var courierDetails = CourierDetails()

    /**
     * Live data for courier details
     */
    var courierDetailsLiveData: MutableLiveData<CourierDetails> = MutableLiveData<CourierDetails>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    /**
     * Get Courier list
     */
    fun getCourierInquiryDetails(productList: ProductList) {

        Log.e("request of courier details", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetCourierList(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of courier details", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCourierList = object : TypeToken<ArrayList<CourierDetails>>() {}.type
                    var courierList : ArrayList<CourierDetails> =
                        gson.fromJson(gson.toJson(datas.data), typeCourierList)

                    courierDetails  = courierList[0]
                    courierDetailsLiveData.setValue(courierList[0])
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