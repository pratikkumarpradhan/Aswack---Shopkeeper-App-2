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


class SignUpViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var registerResponseLiveData: MutableLiveData<ArrayList<Register>> = MutableLiveData<ArrayList<Register>>()


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

    init {

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
    fun apiRegister(register: Register) {
            Log.e("request of register", Gson().toJson(register))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiRegister(register)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of regiter", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeRegsiterList = object : TypeToken<ArrayList<Register>>() {}.type
                        val regsiterList: ArrayList<Register> =
                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)

                        registerResponseLiveData.setValue(regsiterList)
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