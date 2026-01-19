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


class CompanyRegistrationViewModel : ViewModel() {


    var categoryList: ArrayList<Categories> = ArrayList()

    /**
     * Live data for categories response
     */
    var categoriesLiveData: MutableLiveData<ArrayList<Categories>> = MutableLiveData<ArrayList<Categories>>()

    var garageSubCategoryList: ArrayList<SubCategories> = ArrayList()

    var emeragncySubCategoryList: ArrayList<SubCategories> = ArrayList()

    var breakdownSubCategoryList: ArrayList<SubCategories> = ArrayList()

    var insuranceSubCategoryList: ArrayList<SubCategories> = ArrayList()

    /**
     * Live data for categories response
     */
    var subCategoryListLiveData: MutableLiveData<ArrayList<SubCategories>> = MutableLiveData<ArrayList<SubCategories>>()


    /**
     * Live data for countries
     */
    var countriesLiveData: MutableLiveData<ArrayList<Country>> = MutableLiveData<ArrayList<Country>>()

    /**
     * Live data for states
     */
    var statesLiveData: MutableLiveData<ArrayList<States>> = MutableLiveData<ArrayList<States>>()

    /**
     * Live data for cities
     */
    var citiesLiveData: MutableLiveData<ArrayList<City>> = MutableLiveData<ArrayList<City>>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Category which is selected on home page
     */
    var selectedCategoryFromHome = "0"

    /**
     * Live data for success
     */
    var checkCategoryLiveData = MutableLiveData<Boolean>()

    var timesList = ArrayList<String>()

    var filePath = ""

    var latitude = ""
    var longitude = ""

    var isLocationButtonClicked = false

    init {
        getTimeList()
    }


    fun getTimeList(){
        timesList.add("1 am")
        timesList.add("2 am")
        timesList.add("3 am")
        timesList.add("4 am")
        timesList.add("5 am")
        timesList.add("6 am")
        timesList.add("7 am")
        timesList.add("8 am")
        timesList.add("9 am")
        timesList.add("10 am")
        timesList.add("11 am")
        timesList.add("12 am")

        timesList.add("1 pm")
        timesList.add("2 pm")
        timesList.add("3 pm")
        timesList.add("4 pm")
        timesList.add("5 pm")
        timesList.add("6 pm")
        timesList.add("7 pm")
        timesList.add("8 pm")
        timesList.add("9 pm")
        timesList.add("10 pm")
        timesList.add("11 pm")
        timesList.add("12 pm")

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


    /**
     * Get all country list
     */
    fun apiGetStateList(register: Register) {
        Log.e("request of states", Gson().toJson(register))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getStateList(register)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of states", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeStateList = object : TypeToken<ArrayList<States>>() {}.type
                    val stateList: ArrayList<States> =
                        gson.fromJson(gson.toJson(datas.data), typeStateList)

                    statesLiveData.setValue(stateList)
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
     * Get all city list
     */
    fun apiGetCityList(register: Register) {

        Log.e("request of cities", Gson().toJson(register))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCityList(register)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of cities", Gson().toJson(response.body()))
                if (datas.status == true) {
                    Log.e("response1111 of cities", Gson().toJson(response.body()))
                    val gson = Gson()
                    val typeCityList = object : TypeToken<ArrayList<City>>() {}.type
                    val cityList: ArrayList<City> =
                        gson.fromJson(gson.toJson(datas.data), typeCityList)

                    citiesLiveData.setValue(cityList)

                    getCategories()
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
     * Get Seller categories
     */
    private fun getCategories() {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getCategoryList()
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of catgeory", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCategoryList = object : TypeToken<ArrayList<Categories>>() {}.type
                    categoryList =
                        gson.fromJson(gson.toJson(datas.data), typeCategoryList)

                    categoryList.removeAt(1)

                    for (i in categoryList.indices){
                        if (categoryList[i].id == selectedCategoryFromHome){
                            categoryList[i].isChecked = true
                        }
                    }



                    categoriesLiveData.setValue(categoryList)

                    val categories = Categories()
                    categories.category = "3"
                    getSubCategories(categories)

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
     * Get Seller categories
     */
    private fun getSubCategories(categories: Categories) {
        Log.e("request of sub Categories", Gson().toJson(categories))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetSubcategoryList(categories)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of sub Categories", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCategoryList = object : TypeToken<ArrayList<SubCategories>>() {}.type

//                    garageSubCategoryList.removeAt(1)



                    if (categories.category == "3"){

                        garageSubCategoryList =
                            gson.fromJson(gson.toJson(datas.data), typeCategoryList)

//                    garageSubCategoryList.removeAt(1)

                        subCategoryListLiveData.setValue(garageSubCategoryList)

                        val categories = Categories()
                        categories.category = "4"
                        getSubCategories(categories)
                    }else if (categories.category == "4"){

                        insuranceSubCategoryList =
                            gson.fromJson(gson.toJson(datas.data), typeCategoryList)

//                    garageSubCategoryList.removeAt(1)

                        subCategoryListLiveData.setValue(insuranceSubCategoryList)

                        val categories = Categories()
                        categories.category = "5"
                        getSubCategories(categories)
                    }else if (categories.category == "5"){

                        emeragncySubCategoryList =
                            gson.fromJson(gson.toJson(datas.data), typeCategoryList)

//                    garageSubCategoryList.removeAt(1)

                        subCategoryListLiveData.setValue(emeragncySubCategoryList)

                        val categories = Categories()
                        categories.category = "10"
                        getSubCategories(categories)
                    }else if (categories.category == "10"){

                        breakdownSubCategoryList =
                            gson.fromJson(gson.toJson(datas.data), typeCategoryList)

//                    garageSubCategoryList.removeAt(1)

                        subCategoryListLiveData.setValue(breakdownSubCategoryList)

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
     * check that categories are register
     */
     fun checkRegisteredCategories(login: Login) {
        Log.e("request of check catgeory is registered", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCheckCategoryIsRegistered(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of check catgeory is registered", Gson().toJson(response.body()))
                if (datas.status == true) {
                    errorMessage.value = datas.message
                } else {

                    checkCategoryLiveData.value = true
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }









}