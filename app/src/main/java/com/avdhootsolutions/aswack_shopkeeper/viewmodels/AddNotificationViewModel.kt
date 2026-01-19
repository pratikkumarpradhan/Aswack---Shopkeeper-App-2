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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class AddNotificationViewModel : ViewModel() {


//    /**
//     * Live data for vehicle list
//     */
//    var rfqListLiveData: MutableLiveData<ArrayList<RFQList>> = MutableLiveData<ArrayList<RFQList>>()

    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<String>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    var companyId = ""
    var packageId = ""

    var mainCatId = ""

    var filePath = ""


    init {

    }



    /**
     * Add Notification
     */
    fun sendNotificationWithFile(productList: ProductList, file: File?, context: Context) {
        Log.e("request of send Notification", Gson().toJson(productList))

        var fileToUpload: MultipartBody.Part? = null
        file?.let {
            val mFile: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
             fileToUpload = MultipartBody.Part.createFormData("file",
                file.name, mFile)
        }

        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val seller_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM,productList.seller_id?:"")
        val seller_company_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, productList.seller_company_id?:"")
        val package_purchased_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, productList.package_purchased_id?:"")
        val master_category_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, productList.master_category_id?:"")
        val message: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, productList.message?:"")

        if (fileToUpload != null){
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.
            apiAddNotificationWithFile(fileToUpload, seller_id,
                seller_company_id, package_purchased_id,master_category_id,message
            )
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of send Notification", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessageLiveData.value = datas.message
                    } else {
                        errorMessage.value = datas.message
                    }
                }

                override fun onFailure(call: Call<Datas?>, t: Throwable) {

                    errorMessage.value = t.localizedMessage
                    Log.e("onFailure", "onFailure: " + t.localizedMessage)
                }
            })
        }else{

            Log.e("seller_id 1", productList.seller_id!!)
            Log.e("seller_company_id 1", productList.seller_company_id!!)
            Log.e("package_purchased_id 1", productList.package_purchased_id!!)
            Log.e("master_category_id 1", productList.master_category_id!!)
            Log.e("message 1", productList.message!!)

            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.
            apiAddNotificationWithoutFile(seller_id,
                seller_company_id, package_purchased_id,master_category_id,message
            )
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of send Notification", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessageLiveData.value = datas.message
                    } else {
                        errorMessage.value = datas.message
                    }
                }

                override fun onFailure(call: Call<Datas?>, t: Throwable) {

                    errorMessage.value = t.localizedMessage
                    Log.e("onFailure", "onFailure: " + t.localizedMessage)
                }
            })
        }


    }
}