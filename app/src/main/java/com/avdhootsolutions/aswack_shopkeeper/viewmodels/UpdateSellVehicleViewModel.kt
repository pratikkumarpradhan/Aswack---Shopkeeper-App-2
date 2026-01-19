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
import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class UpdateSellVehicleViewModel : ViewModel() {

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

    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Owners list
     */
    var ownersList = ArrayList<String>()

    /**
     * images list
     */
    var imageList = ArrayList<String>()

    /**
     * Selected model class from detail page
     */
    var selectedVehicle = SellVehicle()

    /**
     * Live data for success update image
     */
    var successUpdateImageLiveData = MutableLiveData<Boolean>()

    var selectImageNumber = 0

    init {

    }


    /**
     * Get owners list
     * It is a static list
     */
    fun getOwnersList() : List<String>{

        ownersList = ArrayList()

        ownersList.add("First Owner")
        ownersList.add("Second Owner")
        ownersList.add("Third Owner")
        ownersList.add("Fourth Owner")

        return ownersList
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
                    val brandTypeModelsList: ArrayList<BrandsTypesModels> =
                        gson.fromJson(gson.toJson(datas.data), typeBrandTypeModelsList)

                    brandTypeModelsLiveData.setValue(brandTypeModelsList)

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
                    val yearList: ArrayList<Years> =
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
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.getFuelList()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of fuels", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeFuelList = object : TypeToken<ArrayList<Fuels>>() {}.type
                    val fuelList: ArrayList<Fuels> =
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




    /**
     * update sell vehicle
     * @param sellVehicle request model for update vehicle
     */
    fun apiUpdateSellVehicle(updateSellVehicleRequest: UpdateSellVehicleRequest) {
            Log.e("request of update vehicle", Gson().toJson(updateSellVehicleRequest))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiUpdateSellVehicles(updateSellVehicleRequest)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of update vehicle", Gson().toJson(response.body()))
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

    /**
     * Seller image update
     * @param companyRegister request model class
     */
    fun apiUpdateImage(id: String, no: String, image: File) {

        val mFile: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("image",
            image.name, mFile)

        val id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, id)
        val no: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, no)
        val type: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, "2")

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiUpdateImage(fileToUpload, id, no, type)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of image update", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successUpdateImageLiveData.value = true
                } else {
                    errorMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorMessage.value = t.localizedMessage
                Log.e("onFailure", "onFailure: " + t.localizedMessage)
            }
        })

    }
}