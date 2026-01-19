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


class GarageServiceListViewModel : ViewModel() {


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


    var companyId = ""

    var packageId = ""

    var mainCatId = ""


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
     * Get vehicle sel list
     */
    fun getGarageServiceList(spareParts: SpareParts) {

        if (spareParts.master_category_id == "3"){
            Log.e("request of garage list", Gson().toJson(spareParts))
        }else if (spareParts.master_category_id == "5"){
            Log.e("request of emergency list", Gson().toJson(spareParts))
        }else if (spareParts.master_category_id == "10"){
            Log.e("request of breakdown list", Gson().toJson(spareParts))
        }


        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetProductList(spareParts)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!

                if (spareParts.master_category_id == "3"){
                    Log.e("response of garage list", Gson().toJson(response.body()))
                }else if (spareParts.master_category_id == "5"){
                    Log.e("response of emergency list", Gson().toJson(response.body()))
                }else if (spareParts.master_category_id == "10"){
                    Log.e("response of breakdown list", Gson().toJson(response.body()))
                }

                if (datas.status == true) {


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