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


class CompanyProfileViewModel : ViewModel() {


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Category which is selected on home page
     */
    var selectedCategoryFromHome = ""

    /**
     * Live data for success
     */
    var successMessage = MutableLiveData<String>()

    var filePath = ""

    var latitude = ""
    var longitude = ""

    var companyId = ""

    var isImageSelected = false



    var companyDetails = CompanyDetails()

    /**
     * Live data for company details
     */
    var companyDetailLiveData = MutableLiveData<CompanyDetails>()

    init {

    }


//    /**
//     * Get all country list
//     */
//    fun apiGetCountries() {
//        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCountryList()
//        loginCallBackCall.enqueue(object : Callback<Datas?> {
//            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
//                val datas: Datas = response.body()!!
//                Log.e("response of Countries", Gson().toJson(response.body()))
//                if (datas.status == true) {
//
//                    val gson = Gson()
//
//                    val typeCountryList = object : TypeToken<ArrayList<Country>>() {}.type
//                    val registrationList: ArrayList<Country> =
//                        gson.fromJson(gson.toJson(datas.data), typeCountryList)
//
//                    countriesLiveData.setValue(registrationList)
//                } else {
//                    errorMessage.value = datas.message
//                }
//            }
//
//            override fun onFailure(call: Call<Datas?>, t: Throwable) {
//
//                errorMessage.value = t.localizedMessage
//                Log.d("onFailure", "onFailure: " + t.localizedMessage)
//            }
//        })
//    }
//
//
//    /**
//     * Get all country list
//     */
//    fun apiGetStateList(register: Register) {
//        Log.e("request of states", Gson().toJson(register))
//
//        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getStateList(register)
//        loginCallBackCall.enqueue(object : Callback<Datas?> {
//            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
//                val datas: Datas = response.body()!!
//                Log.e("response of states", Gson().toJson(response.body()))
//                if (datas.status == true) {
//
//                    val gson = Gson()
//                    val typeStateList = object : TypeToken<ArrayList<States>>() {}.type
//                    val stateList: ArrayList<States> =
//                        gson.fromJson(gson.toJson(datas.data), typeStateList)
//
//                    statesLiveData.setValue(stateList)
//                } else {
//                    errorMessage.value = datas.message
//                }
//            }
//
//            override fun onFailure(call: Call<Datas?>, t: Throwable) {
//
//                errorMessage.value = t.localizedMessage
//                Log.d("onFailure", "onFailure: " + t.localizedMessage)
//            }
//        })
//    }
//
//
//    /**
//     * Get all city list
//     */
//    fun apiGetCityList(register: Register) {
//
//        Log.e("request of cities", Gson().toJson(register))
//
//        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCityList(register)
//        loginCallBackCall.enqueue(object : Callback<Datas?> {
//            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
//                val datas: Datas = response.body()!!
//                Log.e("response of cities", Gson().toJson(response.body()))
//                if (datas.status == true) {
//                    Log.e("response1111 of cities", Gson().toJson(response.body()))
//                    val gson = Gson()
//                    val typeCityList = object : TypeToken<ArrayList<City>>() {}.type
//                    val cityList: ArrayList<City> =
//                        gson.fromJson(gson.toJson(datas.data), typeCityList)
//
//                    citiesLiveData.setValue(cityList)
//
//                } else {
//                    errorMessage.value = datas.message
//                }
//            }
//
//            override fun onFailure(call: Call<Datas?>, t: Throwable) {
//
//                errorMessage.value = t.localizedMessage
//                Log.d("onFailure", "onFailure: " + t.localizedMessage)
//            }
//        })
//    }



    /**
     * Get Seller categories
     */
     fun getCompanyProfile(productList: ProductList) {
        Log.e("request of company profile", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetCompanyDetail(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of company details", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typComanyList = object : TypeToken<ArrayList<CompanyDetails>>() {}.type
                    var companyListDataList : ArrayList<CompanyDetails> =
                        gson.fromJson(gson.toJson(datas.data), typComanyList)

                    companyDetails = companyListDataList[0]

                    companyDetailLiveData.setValue(companyDetails)

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
    fun apiCompanyUpdateWithFile(companyRegister: CompanyRegister, file: File?, context: Context) {
        Log.e("request of company update", Gson().toJson(companyRegister))

        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val company_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.company_id?:"")
        val ownername: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.ownername?:"")
        val phone: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.phone?:"")
        val mobielNo: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.mobile?:"")
        val amobile: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.amobile?:"")
        val email: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.email?:"")
        val website: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.website?:"")
        val houseno: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.houseno?:"")
        val streetno: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.streetno?:"")
        val landmark: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.landmark?:"")
        val pincode: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.pincode?:"")
        val detail: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.detail?:"")

        file?.let {
            val mFile: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("file",
                file.name, mFile)

            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiUpdateCompanyDetail(fileToUpload, company_id, ownername,
                phone, mobielNo, amobile, email, website, houseno, streetno, landmark, pincode, detail)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of company update", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessage.value = datas.message
//                        val gson = Gson()
//                        val typeRegsiterList = object : TypeToken<ArrayList<CompanyRegister>>() {}.type
//                        val regsiterList: ArrayList<CompanyRegister> =
//                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)
//
//                        registerResponseLiveData.setValue(regsiterList)
                    } else {
                        errorMessage.value = datas.message
                    }
                }

                override fun onFailure(call: Call<Datas?>, t: Throwable) {

                    errorMessage.value = t.localizedMessage
                    Log.e("onFailure", "onFailure: " + t.localizedMessage)
                }
            })

        }?: kotlin.run {
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiUpdateCompanyDetailWithoutFile(company_id, ownername,
                phone, mobielNo, amobile, email, website, houseno, streetno, landmark, pincode, detail)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of company update", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessage.value = datas.message
//                        val gson = Gson()
//                        val typeRegsiterList = object : TypeToken<ArrayList<CompanyRegister>>() {}.type
//                        val regsiterList: ArrayList<CompanyRegister> =
//                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)
//
//                        registerResponseLiveData.setValue(regsiterList)
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
}