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


class BookingDetailViewModel : ViewModel() {

    var bookingDetails = BookingList()

    /**
     * Live data for success
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    /**
     * change Booking status
     */
    fun changeBookingStatus(bookingStatusRequest: BookingStatusRequest) {

        Log.e("request of booking status change", Gson().toJson(bookingStatusRequest))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiChangeBookingStatus(bookingStatusRequest)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of booking status change", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageLiveData.setValue(true)

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