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
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class HomeViewModel : ViewModel() {

    /**
     * Live data for category list
     */
    var categoriesLiveData: MutableLiveData<ArrayList<Categories>> = MutableLiveData<ArrayList<Categories>>()

    /**
     * Live data for vehicle list
     */
    var vehicleListLiveData: MutableLiveData<ArrayList<Categories>> = MutableLiveData<ArrayList<Categories>>()

    /**
     * Live data for get company status
     */
    var companyStatusLiveData: MutableLiveData<Login> = MutableLiveData<Login>()

    /**
     * Live data for get company status
     */
    var packageStatusLiveData: MutableLiveData<Login> = MutableLiveData<Login>()

    /**
     * Live data for countries
     */
    var countriesLiveData: MutableLiveData<ArrayList<Country>> = MutableLiveData<ArrayList<Country>>()

    /**
     * Live data for sliders
     */
    var sliderLiveData: MutableLiveData<ArrayList<SlideModel>> = MutableLiveData<ArrayList<SlideModel>>()


    val imageList = ArrayList<SlideModel>()
    /**
     * vehicle list
     */
    var vehicleList = ArrayList<Categories>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error
     */
    var successLogoutMessageLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for main opage image
     */
    var mainPageImageLiveData: MutableLiveData<String> = MutableLiveData<String>()

    init {
        getBanners()
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
     * Get banners
     */
    private fun getBanners() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.getSliders()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of SLIDERS", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeSliderList = object : TypeToken<ArrayList<Sliders>>() {}.type
                   val sliderList : ArrayList<Sliders> =
                        gson.fromJson(gson.toJson(datas.data), typeSliderList)

                    for (i in sliderList.indices){
                        imageList.add(SlideModel(sliderList[i].image,null, ScaleTypes.FIT))
                    }

                    sliderLiveData.setValue(imageList)
                } else {
                    errorMessage.value = datas.message
                }


                getCategories()
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {
                getCategories()
                errorMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Get Seller categories
     */
    private fun getCategories() {
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCategoryList()
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of main catgeory", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeCategoryList = object : TypeToken<ArrayList<Categories>>() {}.type
                        val categoryList: ArrayList<Categories> =
                            gson.fromJson(gson.toJson(datas.data), typeCategoryList)
                        categoriesLiveData.setValue(categoryList)

                        getMainPageImage()
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
     * Get main page image
     */
    private fun getMainPageImage() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.getMainPageImage()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of Main page image", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val mainPageImageListTypeToken = object : TypeToken<ArrayList<Sliders>>() {}.type
                    val mainPageImageList : ArrayList<Sliders> =
                        gson.fromJson(gson.toJson(datas.data), mainPageImageListTypeToken)

                    mainPageImageLiveData.value = mainPageImageList[0].image
                } else {
                    errorMessage.value = datas.message
                }


                getVehicles()
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {
                getCategories()
                errorMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }

    /**
     * Get company status for category
     */
     fun getCompanyRegisterStatusForCategory(login: Login, categoryId : String) {

        Log.e("request of company status", Gson().toJson(login))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetCompanyRegisterStatusForCat(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of status", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCompanyStatus = object : TypeToken<ArrayList<Login>>() {}.type
                    val companyStatusList : ArrayList<Login> =
                        gson.fromJson(gson.toJson(datas.data), typeCompanyStatus)

                    if (companyStatusList.size > 1){
                        companyStatusList[1].category_id = categoryId
                        companyStatusLiveData.value = companyStatusList[1]
                    }else{
                        companyStatusList[0].category_id = categoryId
                        companyStatusLiveData.value = companyStatusList[0]
                    }



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
     * Get company status for category
     */
    fun getPackageStatusForCategory(login: Login, categoryId : String) {

        Log.e("request of package status", Gson().toJson(login))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetPackageStatusForCat(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of package status", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCompanyStatus = object : TypeToken<ArrayList<Login>>() {}.type
                    val packageStatusList : ArrayList<Login> =
                        gson.fromJson(gson.toJson(datas.data), typeCompanyStatus)
                    packageStatusList[0].category_id = categoryId
                    packageStatusList[0].company_id = login.company_id
                    packageStatusLiveData.value = packageStatusList[0]

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
     * Call api when user click on logout
     * @param login request model which contains user id
     */
    fun logoutAndClearFCM(login: Login) {
        Log.e("request of logout", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiLogout(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of logout", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successLogoutMessageLiveData.value = true
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
     * Get all country list
     */
    fun apiGetCountries() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCountryList()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of Countries", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeCountryList = object : TypeToken<ArrayList<Country>>() {}.type
                    val registrationList: ArrayList<Country> =
                        gson.fromJson(gson.toJson(datas.data), typeCountryList)

                    countriesLiveData.setValue(registrationList)
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