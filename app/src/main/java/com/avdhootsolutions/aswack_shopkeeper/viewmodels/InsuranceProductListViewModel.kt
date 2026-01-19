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


class InsuranceProductListViewModel : ViewModel() {


    /**
     * Live data for vehicle list
     */
    var vehicleListLiveData: MutableLiveData<ArrayList<Categories>> = MutableLiveData<ArrayList<Categories>>()


    /**
     * vehicle list
     */
    var vehicleList = ArrayList<Categories>()

    /**
     * Live data for vehicle list
     */
    var productListLiveData: MutableLiveData<ArrayList<ProductList>> = MutableLiveData<ArrayList<ProductList>>()


    /**
     * vehicle list
     */
    var productList = ArrayList<ProductList>()

    /**
     * searchable vehicle list
     */
    var searchableProductList = ArrayList<ProductList>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error for getting products
     */
    var errorInProductListMessage = MutableLiveData<String>()

    var companyId = ""

    var packageId = ""

    var mainCatId = ""

    /**
     * Live data for usage fetching error
     */
    var errorOfUsageMessage = MutableLiveData<String>()

    /**
     * Live data for package usage
     */
    var packageUsedLiveData: MutableLiveData<UsagesOfPackage> = MutableLiveData<UsagesOfPackage>()



    /**
     * Get vehicle to buy or sell
     */
     fun getVehicles() {

        val productList = ProductList()
        if (mainCatId == "8"){
            productList.type = "1"

            Log.e("mainCatId ", " mainCatId 8")
        }else{
            productList.type = "0"

            Log.e("mainCatId ", " mainCatId 0")
        }

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().getVehicleList(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of catgeory for heavy ", Gson().toJson(response.body()))
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
     * Get vehicle sel list
     */
    fun getInsuranceProductList(spareParts: SpareParts) {

        Log.e("request of insurance product list", Gson().toJson(spareParts))


        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetProductList(spareParts)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!

                Log.e("response of insurance product list", Gson().toJson(response.body()))

                if (datas.status == true) {
                    val gson = Gson()
                    val productListType = object : TypeToken<ArrayList<ProductList>>() {}.type
                    productList =
                        gson.fromJson(gson.toJson(datas.data), productListType)
                    productListLiveData.setValue(productList)

                } else {
                    errorInProductListMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorInProductListMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Get usage data
     */
    fun getUsageData(productList: ProductList) {

        Log.e("request of offer usage", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetUsageProductOfferNotification(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of offer usage", Gson().toJson(response.body()))

                if (datas.status == true) {
                    val gson = Gson()
                    val packageUsageListType = object : TypeToken<ArrayList<UsagesOfPackage>>() {}.type
                    val packageUsageList : ArrayList<UsagesOfPackage> =
                        gson.fromJson(gson.toJson(datas.data), packageUsageListType)
                    packageUsedLiveData.setValue(packageUsageList[0])

                } else {
                    errorOfUsageMessage.value = datas.message
                }
            }

            override fun onFailure(call: Call<Datas?>, t: Throwable) {

                errorOfUsageMessage.value = t.localizedMessage
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }

}