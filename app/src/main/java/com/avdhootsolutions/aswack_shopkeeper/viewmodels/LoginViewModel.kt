package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.Datas
import com.avdhootsolutions.aswack_shopkeeper.models.Login
import com.avdhootsolutions.aswack_shopkeeper.models.Register
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class LoginViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var loginResponseLiveData: MutableLiveData<ArrayList<Register>> = MutableLiveData<ArrayList<Register>>()


    var errorMessage = MutableLiveData<String>()


    init {

    }


    /**
     * Seller normal login
     * @param login request model class
     */
    fun apiLogin(login: Login) {
            Log.e("request of Login", Gson().toJson(login))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiLogin(login)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of regiter", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeRegsiterList = object : TypeToken<ArrayList<Register>>() {}.type
                        val regsiterList: ArrayList<Register> =
                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)
                        loginResponseLiveData.setValue(regsiterList)
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