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


class SparePartsCarAccesoriesDetailViewModel : ViewModel() {

    /**
     * Selected vehicle
     */
    var productList = ProductList()

    /**
     * Live data for success delete
     */
    var successMessageOfDeleteLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    /**
     * delete product
     * @param productList request model for product delete
     */
    fun apiDeleteProduct(productList: ProductList) {
        Log.e("request of delete product", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> =
            MyApp.getInstance().getMyApi().apiDeleteProductOffer(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of delete product", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageOfDeleteLiveData.setValue(true)
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