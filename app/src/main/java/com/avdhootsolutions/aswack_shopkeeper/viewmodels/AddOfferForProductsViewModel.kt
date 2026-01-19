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


class AddOfferForProductsViewModel : ViewModel() {


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()


    /**
     * Live data for vehicle list
     */
    var productListLiveData: MutableLiveData<ArrayList<ProductList>> = MutableLiveData<ArrayList<ProductList>>()


    /**
     * products list
     */
    var productList = ArrayList<ProductList>()


    var companyId = ""

    var packageId = ""

    var mainCatId = ""

    /**
     * images list
     */
    var imageList = ArrayList<String>()


    init {

    }

    /**
     * Create offer with image file
     * @param offer request model
     * @param context context of the class
     */
    fun createOfferWithFile(offer: Offer, context: Context) {
        Log.e("request of create offer", Gson().toJson(offer))


        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part?= null
        var image3: MultipartBody.Part?= null
        var image4: MultipartBody.Part?= null
        var image5: MultipartBody.Part?= null
        var image6: MultipartBody.Part?= null
        var image7: MultipartBody.Part?= null

        if (imageList.size> 0){
            var file1 : File = File(imageList[0])
            val mFile1: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file1)
            image1 = MultipartBody.Part.createFormData("image1",
                file1.name, mFile1)

        }


        if (imageList.size> 1){
            var file2 : File = File(imageList[1])
            val mFile2: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file2)
            image2 = MultipartBody.Part.createFormData("image2",
                file2.name, mFile2)

        }


        if (imageList.size> 2) {
            var file3: File = File(imageList[2])
            val mFile3: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file3)
            image3 = MultipartBody.Part.createFormData("image3",
                file3.name, mFile3)
        }

        if (imageList.size> 3) {
            var file4 : File = File(imageList[3])
            val mFile4: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file4)
            image4 = MultipartBody.Part.createFormData("image4",
                file4.name, mFile4)
        }

        if (imageList.size> 4) {
            var file5 : File = File(imageList[4])
            val mFile5: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file5)
            image5 = MultipartBody.Part.createFormData("image5",
                file5.name, mFile5)
        }


        if (imageList.size> 5) {
            var file6 : File = File(imageList[5])
            val mFile6: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file6)
            image6 = MultipartBody.Part.createFormData("image6",
                file6.name, mFile6)
        }

        if (imageList.size> 6) {
            var file7: File = File(imageList[6])
            val mFile7: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
                    file7)
            image7 = MultipartBody.Part.createFormData("image7",
                file7.name, mFile7)
        }



        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val seller_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, Helper().getLoginData(context).id?:"")
        val title: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.title?:"")
        val name: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.name?:"")
        val offer_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.offer_id?:"")
        val offer_code: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.offer_code?:"")
        val product_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.product_id?:"")
        val offer_price: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.offer_price?:"")
        val original_price: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.original_price?:"")
        val start_date: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.start_date?:"")
        val end_date: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.end_date?:"")
        val description: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.description?:"")
        val seller_company_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.seller_company_id?:"")
        val seller_category_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.seller_category_id?:"")

        val package_purchased_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, offer.package_purchased_id?:"")

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCreateOfferWithImage(image1, image2, image3, image4, image5, image6, image7,
            title, seller_id, name,  product_id, offer_price, original_price, start_date, end_date,description,
            seller_company_id, seller_category_id,
            package_purchased_id
        )
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageLiveData.setValue(true)
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



    /**
     * Get product list
     */
    fun getProductList(spareParts: SpareParts) {

        Log.e("request of tyre  list", Gson().toJson(spareParts))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetProductList(spareParts)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of tyre  list list", Gson().toJson(response.body()))

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



}