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


class BuyVehicleViewModel : ViewModel() {

    /**
     * Live data for countries
     */
    var brandTypeModelsLiveData: MutableLiveData<ArrayList<BrandsTypesModels>> = MutableLiveData<ArrayList<BrandsTypesModels>>()

    /**
     * Live data for years
     */
    var yearListLiveData: MutableLiveData<ArrayList<Years>> = MutableLiveData<ArrayList<Years>>()

    /**
     * Live data for Fuels
     */
    var fuelListLiveData: MutableLiveData<ArrayList<Fuels>> = MutableLiveData<ArrayList<Fuels>>()

    var brandsTypeModelList = ArrayList<BrandsTypesModels>()

    var typeList = ArrayList<BrandsTypesModels.Types>()

    var modelList = ArrayList<BrandsTypesModels.Types.Models>()

    var yearList = ArrayList<Years>()

    var fuelList = ArrayList<Fuels>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    var categoryId = ""

    init {

    }


    /**
     * Get all types of selected brands
     */
    fun getAllTytpes() : ArrayList<BrandsTypesModels.Types>{

        typeList = ArrayList()

        for (i in brandsTypeModelList.indices){
            if (brandsTypeModelList[i].isChecked){
                typeList.addAll(brandsTypeModelList[i].type_list)
            }
        }

        Log.e("typeList ", typeList.size.toString() + " jkshdlke")

        return typeList
    }

    /**
     * Get all models of selected types
     */
    fun getAllModels() : ArrayList<BrandsTypesModels.Types.Models>{

        modelList = ArrayList()

        for (i in typeList.indices){
            if (typeList[i].isChecked){
                modelList.addAll(typeList[i].model_list)
            }
        }


        return modelList
    }

    /**
     * Get all Brands, Types, Models list
     */
    fun apiGetBrandsTypesModels(sellVehicle: SellVehicle) {

        Log.e("request of BrandsTypesModels", Gson().toJson(sellVehicle))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetVehicleBrandsTypeModels(sellVehicle)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of BrandsTypesModels", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeBrandTypeModelsList = object : TypeToken<ArrayList<BrandsTypesModels>>() {}.type
                    brandsTypeModelList =
                        gson.fromJson(gson.toJson(datas.data), typeBrandTypeModelsList)

                    brandTypeModelsLiveData.setValue(brandsTypeModelList)
                    apiGetYears()
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
     * Get all years
     */
    fun apiGetYears() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getYearList()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of years", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeYearList = object : TypeToken<ArrayList<Years>>() {}.type
                    yearList =
                        gson.fromJson(gson.toJson(datas.data), typeYearList)

                    yearListLiveData.setValue(yearList)

                    apiGetFuels()
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
     * Get all years
     */
    fun apiGetFuels() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getFuelList()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of years", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeFuelList = object : TypeToken<ArrayList<Fuels>>() {}.type
                    fuelList =
                        gson.fromJson(gson.toJson(datas.data), typeFuelList)

                    fuelListLiveData.setValue(fuelList)
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