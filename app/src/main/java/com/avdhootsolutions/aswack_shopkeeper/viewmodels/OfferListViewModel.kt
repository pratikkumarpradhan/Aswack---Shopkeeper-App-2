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
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class OfferListViewModel : ViewModel() {

    /**
     * Live data for login response
     */
    var offerListLiveData: MutableLiveData<ArrayList<Offer>> = MutableLiveData<ArrayList<Offer>>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error for getting products
     */
    var errorInOfferListMessage = MutableLiveData<String>()

    var companyId = ""

    var packageId = ""

    var mainCatId = ""

    /**
     * vehicle list
     */
    var vehicleSellList = ArrayList<SellVehicle>()

    /**
     * Live data for vehicle list
     */
    var vehicleSellListLiveData: MutableLiveData<ArrayList<SellVehicle>> = MutableLiveData<ArrayList<SellVehicle>>()

    /**
     * Live data for vehicle list
     */
    var productListLiveData: MutableLiveData<ArrayList<ProductList>> = MutableLiveData<ArrayList<ProductList>>()


    var sellerId = ""

    /**
     * vehicle list
     */
    var productList = ArrayList<ProductList>()

    var isBuyerOfVehicle = false

    /**
     * searchable offer list
     */
    var searchableOfferList = ArrayList<Offer>()

    var offerList = ArrayList<Offer>()

    /**
     * Live data for package usage
     */
    var packageUsedLiveData: MutableLiveData<UsagesOfPackage> = MutableLiveData<UsagesOfPackage>()

    /**
     * Live data for usage fetching error
     */
    var errorOfUsageMessage = MutableLiveData<String>()

    init {

    }


    /**
     * Get Seller offer list
     */
     fun getOfferList(offer: Offer, mContext : Context) {
        Log.e("request of getOfferList", Gson().toJson(offer))
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetOfferList(offer)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of getOfferList", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeOfferList = object : TypeToken<ArrayList<Offer>>() {}.type
                         offerList =
                            gson.fromJson(gson.toJson(datas.data), typeOfferList)
                        offerListLiveData.setValue(offerList)

                        val login = Login()
                        login.seller_id = Helper().getLoginData(mContext).id

                        if (offer.seller_category_id == "1"){
                            getSellList(login)
                        }else if (offer.seller_category_id != "12"){
                            val spareParts = SpareParts()
                            spareParts.seller_id = Helper().getLoginData(mContext).id
                            spareParts.company_seller_id = offer.seller_company_id
                            spareParts.package_purchased_id = packageId
                            spareParts.master_category_id = offer.seller_category_id
                            getProductList(spareParts)
                        }



                    } else {
                        errorInOfferListMessage.value = datas.message
                    }
                }

                override fun onFailure(call: Call<Datas?>, t: Throwable) {

                    errorInOfferListMessage.value = t.localizedMessage
                    Log.d("onFailure", "onFailure: " + t.localizedMessage)
                }
            })
        }


    /**
     * Get vehicle sel list
     */
    fun getSellList(login : Login) {
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetVehicleSellList(login)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of sell list", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeVehicleList = object : TypeToken<ArrayList<SellVehicle>>() {}.type
                    vehicleSellList =
                        gson.fromJson(gson.toJson(datas.data), typeVehicleList)
                    vehicleSellListLiveData.setValue(vehicleSellList)
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
     * Get product list
     */
    fun getProductList(spareParts: SpareParts) {

        Log.e("request of product  list", Gson().toJson(spareParts))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetProductList(spareParts)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of product list", Gson().toJson(response.body()))

                if (datas.status == true) {
                    val gson = Gson()
                    val productListType = object : TypeToken<ArrayList<ProductList>>() {}.type
                    productList =
                        gson.fromJson(gson.toJson(datas.data), productListType)
                    productListLiveData.setValue(productList)

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