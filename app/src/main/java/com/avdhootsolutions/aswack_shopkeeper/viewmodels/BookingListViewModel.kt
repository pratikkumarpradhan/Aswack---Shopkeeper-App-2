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


class BookingListViewModel : ViewModel() {


    /**
     * Live data for vehicle list
     */
    var bookingListLiveData: MutableLiveData<ArrayList<BookingList>> = MutableLiveData<ArrayList<BookingList>>()


    /**
     * vehicle list
     */
    var bookingList = ArrayList<BookingList>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()



    init {


    }


    /**
     * Get Booking list
     */
     fun getBookingList(login: Login) {

        Log.e("request of booking list", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetBookingList(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                if (response.body() != null) {
                    val datas: Datas = response.body()!!
                    Log.e("response of booking list", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeVehicleList = object : TypeToken<ArrayList<BookingList>>() {}.type
                        bookingList =
                            gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                        // Ensure we always set a list, even if empty
                        if (bookingList == null) {
                            bookingList = ArrayList()
                        }
                        bookingListLiveData.setValue(bookingList)
                    } else {
                        // If status is false but no error message, set empty list
                        if (datas.message.isNullOrEmpty()) {
                            bookingList = ArrayList()
                            bookingListLiveData.setValue(bookingList)
                        } else {
                            errorMessage.value = datas.message
                        }
                    }
                } else {
                    // Handle null response body
                    bookingList = ArrayList()
                    bookingListLiveData.setValue(bookingList)
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


}