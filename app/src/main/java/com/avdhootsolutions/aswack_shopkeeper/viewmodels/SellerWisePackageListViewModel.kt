package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
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


class SellerWisePackageListViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var regularPackageDataListLiveData: MutableLiveData<ArrayList<PackageData>> = MutableLiveData<ArrayList<PackageData>>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for success
     */
    var successMessage = MutableLiveData<String>()

    /**
     * Request model class to send register company
     * In this selected package will be send to the server
     */
    var companyRegister = CompanyRegister()

    var filePath = ""

    var directPackage = false

    var companyid = ""

    /**
     * Live data for success
     */
    var successPackageMessage = MutableLiveData<String>()


    /**
     * Get Seller Packages List
     */
     fun getPackageList(packageRequest : PackageRequest) {

        Log.e("request of Seller Package", Gson().toJson(packageRequest))

            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetSellerPackageList(packageRequest)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of Seller Package", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeRegularPackageList = object : TypeToken<ArrayList<PackageData>>() {}.type
                        val regularList: ArrayList<PackageData> =
                            gson.fromJson(gson.toJson(datas.data), typeRegularPackageList)
                        regularPackageDataListLiveData.setValue(regularList)
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
     * Seller normal regitration
     * @param register request model class
     */
    fun apiCompanyRegister(companyRegister: CompanyRegister) {
        Log.e("request of company Register", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCompanyRegister(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
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
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Seller normal regitration
     * @param register request model class
     */
    fun apiSelectPackage(companyRegister: CompanyRegister) {
        Log.e("request of company Register", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCompanyPackage(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successPackageMessage.value = datas.message
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
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Seller normal regitration
     * @param register request model class
     */
    fun apiSelectPackageByStatus(companyRegister: CompanyRegister) {
        Log.e("request of company Register", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCompanyPackage(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successPackageMessage.value = datas.message
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
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


}