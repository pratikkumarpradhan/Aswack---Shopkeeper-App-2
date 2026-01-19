package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
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


class PackageListViewModel : ViewModel() {

    /**
     * Live data for regular packages
     */
    var regularPackageDataListLiveData: MutableLiveData<ArrayList<PackageData>> = MutableLiveData<ArrayList<PackageData>>()

    /**
     * Live data for custom packages
     */
    var customPackageDataListLiveData: MutableLiveData<ArrayList<CustomPackageData>> = MutableLiveData<ArrayList<CustomPackageData>>()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for success
     */
    var successMessage = MutableLiveData<String>()

    /**
     * Request model class to send register company
     * In this selected package will be send to the server
     */
    var companyRegister = CompanyRegister()

    var filePath = ""

    var directPackage = false

    var companyid = ""

    /**
     * Live data for success
     */
    var successCustomPackageRequestMessage = MutableLiveData<Login>()

    /**
     * Live data for successfully purchase package
     */
    var successCustomPackagePurchasedLiveData = MutableLiveData<String>()


    /**
     * Get Regular Packages List
     */
     fun getRegularPackageList(packageRequest : PackageRequest) {

        Log.e("request of regular Packages", Gson().toJson(packageRequest))

            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetPackageList(packageRequest)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of regular Packages", Gson().toJson(response.body()))
                    if (datas.status == true) {

                        val gson = Gson()
                        val typeRegularPackageList = object : TypeToken<ArrayList<PackageData>>() {}.type
                        val regularList: ArrayList<PackageData> =
                            gson.fromJson(gson.toJson(datas.data), typeRegularPackageList)
                        regularPackageDataListLiveData.setValue(regularList)
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
     * Get Custom Packages List
     */
    fun getCustomPackageList(packageRequest : PackageRequest) {

        Log.e("request of custom Packages", Gson().toJson(packageRequest))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetCustomPackageList(packageRequest)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of custom Packages", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCustomPackageList = object : TypeToken<ArrayList<CustomPackageData>>() {}.type
                    val customPackageList: ArrayList<CustomPackageData> =
                        gson.fromJson(gson.toJson(datas.data), typeCustomPackageList)
                    customPackageDataListLiveData.setValue(customPackageList)
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
     * Seller normal regitration
     * @param register request model class
     */
    fun apiInsertCustomPackage(companyRegister: CompanyRegister) {
        Log.e("request of insert custom package", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiCompanyPackage(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of insert custom package", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeCustomPackageResponseList = object : TypeToken<ArrayList<Login>>() {}.type
                    val customPackageResponseList: ArrayList<Login> =
                        gson.fromJson(gson.toJson(datas.data), typeCustomPackageResponseList)
                    successCustomPackageRequestMessage.setValue(customPackageResponseList[0])

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
     * Seller normal regitration
     * @param register request model class
     */
    fun apiCompanyRegisterWithFile(companyRegister: CompanyRegister, file: File?, context: Context) {
        Log.e("request of company Register", Gson().toJson(companyRegister))

        var fileToUpload: MultipartBody.Part? = null

        file?.let {
            val mFile: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
             fileToUpload = MultipartBody.Part.createFormData("file",
                file.name, mFile)
        }


        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        val seller_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, Helper().getLoginData(context).id!!)
        val type: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, Gson().toJson(companyRegister.type))

        val name: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.name?:"")
        val ownername: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.ownername?:"")
        val phone: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.phone?:"")
        val mobielNo: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.mobile?:"")
        val amobile: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.amobile?:"")
        val email: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.email?:"")
        val website: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.website?:"")
        val country: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.country?:"")
        val state: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.state?:"")
        val city: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.city?:"")
        val houseno: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.houseno?:"")
        val streetno: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.streetno?:"")
        val landmark: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.landmark?:"")
        val pincode: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.pincode?:"")
        val detail: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.detail?:"")
        val officetime: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.officetime?:"")
//        val times: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, Gson().toJson(companyRegister.times))
        val package_id: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.package_id?:"")
        val custom_request: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.custom_request?:"")
        val offer: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.offer?:"")
        val post: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.post?:"")
        val notification: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.notification?:"")

        val duration: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.duration?:"")

        val lat: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.lat?:"")

        val long: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.long?:"")

        val company_type: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.company_type?:"")

        val specialize_in: okhttp3.RequestBody = okhttp3.RequestBody.create(
            MultipartBody.FORM, companyRegister.specialize_in?:"")

        val timesMap: HashMap<String, String> = HashMap()

        for((index, contributor) in companyRegister.times.withIndex()){

            timesMap["times[${index}][day]"] = "${contributor.day}"
            timesMap["times[${index}][open]"] = "${contributor.open}"
            timesMap["times[${index}][close]"] = "${contributor.close}"

        }


        fileToUpload?.let {
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiCompanyRegisterWithImage(it, seller_id, name, ownername,
                phone, mobielNo, amobile, email, website, country,state, city, houseno, streetno, landmark, pincode, detail, officetime, package_id, custom_request,
                offer, post, notification, duration, companyRegister.type,timesMap, companyRegister.garage_subcategory, companyRegister.emergency_subcategory,
                companyRegister.breakdown_subcategory, companyRegister.insurance_subcategory, lat, long,
            company_type, specialize_in)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of regiter", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessage.value = datas.message
//                        val gson = Gson()
//                        val typeRegsiterList = object : TypeToken<ArrayList<CompanyRegister>>() {}.type
//                        val regsiterList: ArrayList<CompanyRegister> =
//                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)
//
//                        registerResponseLiveData.setValue(regsiterList)
                    } else {
                        errorMessage.value = datas.message
                    }
                }

                override fun onFailure(call: Call<Datas?>, t: Throwable) {

                    errorMessage.value = t.localizedMessage
                    Log.e("onFailure", "onFailure: " + t.localizedMessage)
                }
            })
        }?: kotlin.run {
            val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiCompanyRegisterWithoutImage(seller_id, name, ownername,
                phone, mobielNo, amobile, email, website, country,state, city, houseno, streetno, landmark, pincode, detail, officetime, package_id, custom_request,
                offer, post, notification, duration, companyRegister.type,timesMap, companyRegister.garage_subcategory, companyRegister.emergency_subcategory,
                companyRegister.breakdown_subcategory, companyRegister.insurance_subcategory, lat, long,company_type,specialize_in)
            loginCallBackCall.enqueue(object : Callback<Datas?> {
                override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                    val datas: Datas = response.body()!!
                    Log.e("response of regiter", Gson().toJson(response.body()))
                    if (datas.status == true) {
                        successMessage.value = datas.message
//                        val gson = Gson()
//                        val typeRegsiterList = object : TypeToken<ArrayList<CompanyRegister>>() {}.type
//                        val regsiterList: ArrayList<CompanyRegister> =
//                            gson.fromJson(gson.toJson(datas.data), typeRegsiterList)
//
//                        registerResponseLiveData.setValue(regsiterList)
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



    /**
     * Seller custom package subscription
     * @param packageSubscriptionRequest request model class
     */
    fun apiSelectCustomPackageSubscription(packageSubscriptionRequest: PackageSubscriptionRequest) {
        Log.e("request of select package subscription", Gson().toJson(packageSubscriptionRequest))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiSelectCustomPackageSubscription(packageSubscriptionRequest)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of select package subscription", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successCustomPackagePurchasedLiveData.value = datas.message
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