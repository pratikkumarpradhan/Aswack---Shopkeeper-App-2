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


class AddGarageViewModel : ViewModel() {

    /**
     * Live data for countries
     */
    var brandTypeModelsLiveData: MutableLiveData<ArrayList<BrandsTypesModels>> =
        MutableLiveData<ArrayList<BrandsTypesModels>>()



    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()


    var vehicleCategoryId = ""

    var mainCatId = ""

    var companyId = ""

    var packageId = ""

    /**
     * images list
     */
    var imageList = ArrayList<String>()


    init {

    }


    /**
     * Get all Brands, Types, Models list
     */
    fun apiGetBrandsTypesModels(sellVehicle: SellVehicle) {

        Log.e("request of BrandsTypesModels", Gson().toJson(sellVehicle))

        val loginCallBackCall: Call<Datas> =
            MyApp.getInstance().getMyApi().apiGetVehicleBrandsTypeModels(sellVehicle)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of BrandsTypesModels", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()

                    val typeBrandTypeModelsList =
                        object : TypeToken<ArrayList<BrandsTypesModels>>() {}.type
                    val brandTypeModelsList: ArrayList<BrandsTypesModels> =
                        gson.fromJson(gson.toJson(datas.data), typeBrandTypeModelsList)

                    brandTypeModelsLiveData.setValue(brandTypeModelsList)

//                    apiGetYears()
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





    fun apiAddGarageWithFile(spareParts: SpareParts, context: Context) {
        Log.e("request of add garage", Gson().toJson(spareParts))


        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var image4: MultipartBody.Part? = null
        var image5: MultipartBody.Part? = null
        var image6: MultipartBody.Part? = null
        var image7: MultipartBody.Part? = null

        if (imageList.size > 0) {
            var file1: File = File(imageList[0])
            val mFile1: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file1)
            image1 = MultipartBody.Part.createFormData("image1",
                file1.name, mFile1)

        }


        if (imageList.size > 1) {
            var file2: File = File(imageList[1])
            val mFile2: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file2)
            image2 = MultipartBody.Part.createFormData("image2",
                file2.name, mFile2)

        }


        if (imageList.size > 2) {
            var file3: File = File(imageList[2])
            val mFile3: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file3)
            image3 = MultipartBody.Part.createFormData("image3",
                file3.name, mFile3)
        }

        if (imageList.size > 3) {
            var file4: File = File(imageList[3])
            val mFile4: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file4)
            image4 = MultipartBody.Part.createFormData("image4",
                file4.name, mFile4)
        }

        if (imageList.size > 4) {
            var file5: File = File(imageList[4])
            val mFile5: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file5)
            image5 = MultipartBody.Part.createFormData("image5",
                file5.name, mFile5)
        }


        if (imageList.size > 5) {
            var file6: File = File(imageList[5])
            val mFile6: okhttp3.RequestBody =
                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file6)
            image6 = MultipartBody.Part.createFormData("image6",
                file6.name, mFile6)
        }

//        if (imageList.size > 6) {
//            var file7: File = File(imageList[6])
//            val mFile7: okhttp3.RequestBody =
//                okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(),
//                    file7)
//            image7 = MultipartBody.Part.createFormData("image7",
//                file7.name, mFile7)
//        }


        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val seller_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, Helper().getLoginData(context).id ?: "")

        val seller_company_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.seller_company_id ?: "")

        val package_purchased_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.package_purchased_id ?: "")

        val master_category_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.master_category_id ?: "")

        val vehicle_category: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.vehicle_category ?: "")
        val vehicle_company: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.vehicle_company ?: "")

        val product_name: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.product_name ?: "")

        val price: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.price ?: "")
        val description: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.description ?: "")

        val specialize_in: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, spareParts.specialize_in ?: "")


        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi()
            .apiAddGarageServiceWithImage(image1,
                image2,
                image3,
                image4,
                image5,
                image6,
                seller_id,
                seller_company_id,
                package_purchased_id,
                master_category_id,
                vehicle_category,
                vehicle_company,
                product_name,
                price,
                description,
                specialize_in)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of garage", Gson().toJson(response.body()))
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


}