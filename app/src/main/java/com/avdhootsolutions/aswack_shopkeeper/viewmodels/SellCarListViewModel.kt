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


class SellCarListViewModel : ViewModel() {


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
    var vehicleSellListLiveData: MutableLiveData<ArrayList<SellVehicle>> = MutableLiveData<ArrayList<SellVehicle>>()


    /**
     * vehicle list
     */
    var vehicleSellList = ArrayList<SellVehicle>()

    /**
     * searchable vehicle list
     */
    var searchableVehicleSellList = ArrayList<SellVehicle>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error for getting products
     */
    var errorInProductListMessage = MutableLiveData<String>()


    var companyId = ""

    var packageId = ""

    var isBuyerOfVehicle = false
    var sellerIdOfVehicle = ""
    var userType = ""

    /**
     * Live data for package usage
     */
    var packageUsedLiveData: MutableLiveData<UsagesOfPackage> = MutableLiveData<UsagesOfPackage>()


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
     * Get vehicle sell list
     */
    fun getSellList(login : Login) {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetVehicleSellList(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of sell list", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeVehicleList = object : TypeToken<ArrayList<SellVehicle>>() {}.type
                    vehicleSellList =
                        gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                    vehicleSellListLiveData.setValue(vehicleSellList)
                } else {
                    errorInProductListMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorInProductListMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Get vehicle sell list from user application
     */
    fun getSellListFromUserApp(login : Login) {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetVehicleSellListFromUserApp(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of sell list", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeVehicleList = object : TypeToken<ArrayList<SellVehicle>>() {}.type
                    vehicleSellList =
                        gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                    vehicleSellListLiveData.setValue(vehicleSellList)
                } else {
                    errorInProductListMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorInProductListMessage.value = t.localizedMessage
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
                    val packageUsageListType = object : TypeToken<ArrayList<UsagesOfPackage>>() {}.type
                    val packageUsageList : ArrayList<UsagesOfPackage> =
                        gson.fromJson(gson.toJson(datas.data), packageUsageListType)
                    packageUsedLiveData.setValue(packageUsageList[0])

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