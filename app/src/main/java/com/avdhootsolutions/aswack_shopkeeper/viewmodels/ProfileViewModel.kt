package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class ProfileViewModel : ViewModel() {
    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()


    /**
     * Live data for login response
     */
    var profileResponseLiveData: MutableLiveData<ArrayList<Register>> = MutableLiveData<ArrayList<Register>>()


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
    fun apiUpdateProfile(register: Register) {
            Log.e("request of update profile", Gson().toJson(register))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiUserProfileUpdate(register)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of update profile", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        successMessageLiveData.value = true
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
     * Get profile details from user id
     * @param login model class contains user_id
     */
    fun apiGetProfileDetails(login: Login) {
        Log.e("request of get profile details", Gson().toJson(login))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetProfile(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of profile data", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeRegsiterList = object : TypeToken<ArrayList<Register>>() {}.type
                    val regsiterList: ArrayList<Register> =
                        gson.fromJson(gson.toJson(datas.data), typeRegsiterList)

                    profileResponseLiveData.value = regsiterList


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