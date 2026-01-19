package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.Datas
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.PageDetails
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import okhttp3.MultipartBody
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.util.ArrayList


class PageDetailsViewModel : ViewModel() {


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for page details
     */
    var pageDetailsLiveData = MutableLiveData<PageDetails>()

    /**
     * Page no comes from navigation drawer
     */
    var pageNo = ""

    /**
     * get page details
     * @param login request model for get page details
     */
    fun getPageDetails(login: Login) {
        Log.e("request of get page details", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetPageDetails(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of get page detail", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typePageDetailsList = object : TypeToken<ArrayList<PageDetails>>() {}.type
                    val pageDetailList : ArrayList<PageDetails> =
                        gson.fromJson(gson.toJson(datas.data), typePageDetailsList)
                    pageDetailsLiveData.setValue(pageDetailList[0])

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