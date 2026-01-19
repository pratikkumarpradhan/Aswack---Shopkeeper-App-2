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


class SearchCarListViewModel : ViewModel() {


    /**
     * Live data for vehicle list
     */
    var vehicleListLiveData: MutableLiveData<ArrayList<Categories>> = MutableLiveData<ArrayList<Categories>>()


    /**
     * vehicle list
     */
    var vehicleList = ArrayList<Categories>()

    /**
     * Live data for vehicle list
     */
    var buyVehicleListLiveData: MutableLiveData<ArrayList<SellVehicle>> = MutableLiveData<ArrayList<SellVehicle>>()


    /**
     * vehicle list
     */
    var buyVehicleList = ArrayList<SellVehicle>()

    /**
     * searchable vehicle list
     */
    var searchableBuyVehicleList = ArrayList<SellVehicle>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    /**
     * Model class for filter
     */
    var searchFilter = BuyVehicle()


    init {
        getVehicles()
    }


    /**
     * Get vehicle to buy or sell
     */
    private fun getVehicles() {
        val productList = ProductList()
        productList.type = "0"
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getVehicleList(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of catgeory", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeVehicleList = object : TypeToken<ArrayList<Categories>>() {}.type
                    vehicleList =
                        gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                    vehicleListLiveData.setValue(vehicleList)
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
     * Seller buy vehicle
     * @param buy request model for sell vehicle
     */
    fun getBuyVehicle(buyVehicle: BuyVehicle) {
        Log.e("request of buyVehicle", Gson().toJson(buyVehicle))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiBuyVehicles(buyVehicle)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                    val gson = Gson()
                    val typeVehicleList = object : TypeToken<ArrayList<SellVehicle>>() {}.type
                    buyVehicleList =
                        gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                    buyVehicleListLiveData.setValue(buyVehicleList)

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