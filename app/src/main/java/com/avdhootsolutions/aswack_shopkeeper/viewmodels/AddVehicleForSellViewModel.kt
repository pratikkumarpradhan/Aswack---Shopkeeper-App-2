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


class AddVehicleForSellViewModel : ViewModel() {

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
     * Live data for success
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

    var vehicleCategoryId = ""

    var companyId = ""

    var packageId = ""

    /**
     * images list
     */
    var imageList = ArrayList<String>()


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
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getFuelList()
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
     * Seller sell vehicle
     * @param sellVehicle request model for sell vehicle
     */
    fun apiSellVehicle(sellVehicle: SellVehicle) {
            Log.e("request of sell vehicle", Gson().toJson(sellVehicle))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiSellVehicles(sellVehicle)
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




    fun apiSellVehicleWithFile(sellVehicle: SellVehicle, context: Context) {
        Log.e("request of sell vehicle", Gson().toJson(sellVehicle))


        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part?= null
        var image3: MultipartBody.Part?= null
        var image4: MultipartBody.Part?= null
        var image5: MultipartBody.Part?= null
        var image6: MultipartBody.Part?= null
        var image7: MultipartBody.Part?= null

        if (imageList.size> 0){
            var file1 : File = File(imageList[0])
            val mFile1: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file1)
            image1 = MultipartBody.Part.createFormData("image1",
                file1.name, mFile1)

        }


        if (imageList.size> 1){
            var file2 : File = File(imageList[1])
            val mFile2: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file2)
             image2 = MultipartBody.Part.createFormData("image2",
                file2.name, mFile2)

        }


        if (imageList.size> 2) {
            var file3: File = File(imageList[2])
            val mFile3: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file3)
             image3 = MultipartBody.Part.createFormData("image3",
                file3.name, mFile3)
        }

        if (imageList.size> 3) {
        var file4 : File = File(imageList[3])
        val mFile4: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file4)
         image4 = MultipartBody.Part.createFormData("image4",
            file4.name, mFile4)
        }

            if (imageList.size> 4) {
        var file5 : File = File(imageList[4])
        val mFile5: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file5)
         image5 = MultipartBody.Part.createFormData("image5",
            file5.name, mFile5)
            }


                if (imageList.size> 5) {
        var file6 : File = File(imageList[5])
        val mFile6: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file6)
         image6 = MultipartBody.Part.createFormData("image6",
            file6.name, mFile6)
                }

                    if (imageList.size> 6) {
                        var file7: File = File(imageList[6])
                        val mFile7: okhttp3.RequestBody =
                            okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                                file7)
                         image7 = MultipartBody.Part.createFormData("image7",
                            file7.name, mFile7)
                    }


        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val user_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, Helper().getLoginData(context).id?:"")
        val seller_company_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.seller_company_id?:"")
        val user_type: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.user_type?:"")
        val vehicle_cat: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_cat?:"")
        val vehicle_brand: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_brand?:"")
        val vehicle_type: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_type?:"")
        val vehicle_model: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_model?:"")
        val vehicle_year: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_year?:"")
        val vehicle_fuel: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.vehicle_fuel?:"")
        val transmission: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.transmission?:"")
        val driven_km: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.driven_km?:"")
        val title: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.title?:"")
        val owners: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.owners?:"")
        val location_longitude: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.location_longitude?:"")
        val contact_number: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.contact_number?:"")
        val location_latitude: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.location_latitude?:"")
        val price: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.price?:"")
        val description: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.description?:"")
        val package_purchased_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, sellVehicle.package_purchased_id?:"")


        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiSellVehicleWithImage(image1, image2, image3, image4, image5, image6, image7,
            seller_company_id, user_id, user_type, vehicle_cat,
            vehicle_brand, vehicle_type, vehicle_model, vehicle_year, vehicle_fuel, transmission,driven_km,
            title, owners,location_longitude , contact_number, location_latitude, price, description,package_purchased_id)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageLiveData.setValue(true)
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