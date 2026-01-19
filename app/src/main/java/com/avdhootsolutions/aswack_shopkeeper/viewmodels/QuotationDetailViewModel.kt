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
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class QuotationDetailViewModel : ViewModel() {




    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for quotation details
     */
    var quotationDetailLiveData = MutableLiveData<QuotationRequest>()


    /**
     * product data for seller
     */
    var productDataForSeller = ProductDataForSeller()


    init {

    }

    /**
     * get quotation detail
     * @param addQuotation request model class
     */
    fun apiGetQuotationDetails(login: Login) {
            Log.e("request of quotation list", Gson().toJson(login))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiQuotationListOrDetail(login)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of quotation list", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeList = object : TypeToken<ArrayList<QuotationRequest>>() {}.type
                        val quotationDataList : ArrayList<QuotationRequest> =
                            gson.fromJson(gson.toJson(datas.data), typeList)
                        quotationDetailLiveData.setValue(quotationDataList[0])

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