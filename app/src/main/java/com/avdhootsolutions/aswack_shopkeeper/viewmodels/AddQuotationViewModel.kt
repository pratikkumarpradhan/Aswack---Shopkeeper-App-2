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


class AddQuotationViewModel : ViewModel() {


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error
     */
    var quotationAddedLiveData = MutableLiveData<Login>()


    /**
     * product data for seller
     */
    var productDataForSeller = ProductDataForSeller()

    var addedQuotationRequest = QuotationRequest()


    init {

    }

    /**
     * craete quotation
     * @param addQuotation request model class
     */
    fun apiCreateQuotation(quotationRequest: QuotationRequest) {
            Log.e("request of add quotation", Gson().toJson(quotationRequest))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiAddQuotation(quotationRequest)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of add quotation", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        addedQuotationRequest = quotationRequest

                        val gson = Gson()
                        val typeList = object : TypeToken<ArrayList<Login>>() {}.type
                       val quotationDataList : ArrayList<Login> =
                            gson.fromJson(gson.toJson(datas.data), typeList)
                        quotationAddedLiveData.setValue(quotationDataList[0])

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