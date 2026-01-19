package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.Datas
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.SellVehicle
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import okhttp3.MultipartBody
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


class SearchVehicleDetailViewModel : ViewModel() {

    /**
     * Live data for success delete
     */
    var successMessageOfDeleteLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    /**
     * Selected vehicle
     */
    var selectedVehcile = SellVehicle()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()



    /**
     * Seller sell vehicle
     * @param sellVehicle request model for sell vehicle
     */
//    fun apiAddRemoveInWishList(wishListRequest: WishListRequest) {
//        Log.e("request of add or remove wish list", Gson().toJson(wishListRequest))
//        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiAddRemoveWishList(wishListRequest)
//        loginCallBackCall.enqueue(object : Callback<Datas?> {
//            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
//                val datas: Datas = response.body()!!
//                Log.e("response of sell vehicle", Gson().toJson(response.body()))
//                if (datas.status == true) {
//                    successMessageLiveData.setValue(true)
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
     * delete product
     * @param spareParts request model for product delete
     */
    fun apiDeleteProduct(productList: ProductList) {
        Log.e("request of delete product", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> =
            MyApp.getInstance().getMyApi().apiDeleteProductOffer(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of delete product", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageOfDeleteLiveData.setValue(true)
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