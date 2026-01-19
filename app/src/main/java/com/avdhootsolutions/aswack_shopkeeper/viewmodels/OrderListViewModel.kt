package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.models.OrderRequest
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class OrderListViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var orderListLiveData: MutableLiveData<ArrayList<OrderRequest>> = MutableLiveData<ArrayList<OrderRequest>>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    var companyId = ""

    var mainCatId = ""


    /**
     * vehicle list
     */
    var productList = ArrayList<ProductList>()

    init {

    }


    /**
     * Get Seller order list
     */
     fun getOrderList(productList: ProductList) {
        Log.e("request of get order list", Gson().toJson(productList))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetOrderList(productList)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of get order list", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeOrderList = object : TypeToken<ArrayList<OrderRequest>>() {}.type
                        val orderList: ArrayList<OrderRequest> =
                            gson.fromJson(gson.toJson(datas.data), typeOrderList)
                        orderListLiveData.setValue(orderList)


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