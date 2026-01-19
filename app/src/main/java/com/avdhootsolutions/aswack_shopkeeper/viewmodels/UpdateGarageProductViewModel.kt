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


class UpdateGarageProductViewModel : ViewModel() {

    /**
     * Live data for countries
     */
    var brandTypeModelsLiveData: MutableLiveData<ArrayList<BrandsTypesModels>> =
        MutableLiveData<ArrayList<BrandsTypesModels>>()



    /**
     * Live data for success update
     */
    var successMessageLiveData = MutableLiveData<Boolean>()



    /**
     * Live data for success update image
     */
    var successUpdateImageLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    var categoryId = ""

    var mainCatId = ""

    var companyId = ""

    var packageId = ""

    /**
     * images list
     */
    var imageList = ArrayList<String>()

    /**
     * Selected product
     */
    var productList = ProductList()

    var selectImageNumber = 0


    init {

    }


    /**
     * Get all Brands, Types, Models list
     */
    fun apiGetBrandsTypesModels(sellVehicle: SellVehicle) {

        Log.e("request of BrandsTypesModels", Gson().toJson(sellVehicle))

        val loginCallBackCall: Call<Datas> =
            MyApp.getInstance().getMyApi().apiGetVehicleBrandsTypeModels(sellVehicle)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of BrandsTypesModels", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeBrandTypeModelsList =
                        object : TypeToken<ArrayList<BrandsTypesModels>>() {}.type
                    val brandTypeModelsList: ArrayList<BrandsTypesModels> =
                        gson.fromJson(gson.toJson(datas.data), typeBrandTypeModelsList)

                    brandTypeModelsLiveData.setValue(brandTypeModelsList)

//                    apiGetYears()
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
     * update product
     * @param sellVehicle request model for product update
     */
    fun apiUpdateProduct(spareParts: SpareParts) {
        Log.e("request of update product", Gson().toJson(spareParts))
        val loginCallBackCall: Call<Datas> =
            MyApp.getInstance().getMyApi().apiUpdateProduct(spareParts)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of sell vehicle", Gson().toJson(response.body()))
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
     * Seller company update
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
            MultipartBody.FORM, "0")

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
