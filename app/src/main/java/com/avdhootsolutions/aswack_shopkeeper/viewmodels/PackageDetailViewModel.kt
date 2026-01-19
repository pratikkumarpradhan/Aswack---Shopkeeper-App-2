package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import okhttp3.MultipartBody

import com.avdhootsolutions.aswack_shopkeeper.utilities.Helper
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File


class PackageDetailViewModel : ViewModel() {


    /**
     * Live data for login response
     */
    var packageDetailLiveData: MutableLiveData<PackageData> = MutableLiveData<PackageData>()



    /**
     * Selected package
     */
    var selectedPackage = PackageData()


    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for success
     */
    var successRegisterLiveData = MutableLiveData<Login>()

    /**
     * Live data for success
     */
    var successCustomPackageRequestMessage = MutableLiveData<Login>()

    /**
     * Live data for successfully purchase package
     */
    var successPackagePurchasedLiveData = MutableLiveData<String>()

    /**
     * Request model class to send register company
     * In this selected package will be send to the server
     */
    var companyRegister = CompanyRegister()

    var filePath : String = ""

    var directPackage = false

    var companyId : String = ""

    var selectedDuration = 0



    /**
     * Seller normal regitration
     * @param register request model class
     */
    fun apiCompanyRegister(companyRegister: CompanyRegister) {
        Log.e("request of company Register", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiCompanyRegister(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of regiter", Gson().toJson(response.body()))
                if (datas.status == true) {
                 //   successMessage.value = datas.getMessage()
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
                Log.d("onFailure", "onFailure: " + t.localizedMessage)
            }
        })
    }


    /**
     * Seller normal regitration
     * @param register request model class
     */
    fun apiSelectPackage(companyRegister: CompanyRegister) {
        Log.e("request of select package", Gson().toJson(companyRegister))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiCompanyPackage(companyRegister)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of select package", Gson().toJson(response.body()))
                if (datas.status == true) {
                    val gson = Gson()
                    val typeCustomPackageResponseList = object : TypeToken<java.util.ArrayList<Login>>() {}.type
                    val customPackageResponseList: java.util.ArrayList<Login> =
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
     * Seller package subscription
     * @param packageSubscriptionRequest request model class
     */
    fun apiSelectPackageSubscription(packageSubscriptionRequest: PackageSubscriptionRequest) {
        Log.e("request of select package subscription", Gson().toJson(packageSubscriptionRequest))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiSelectPackageSubscription(packageSubscriptionRequest)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of select package subscription", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successPackagePurchasedLiveData.value = datas.message
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
     * Get Package Details
     * @param packageRequest request model
     */
    fun getPackageDetail(packageRequest: PackageRequest) {

        Log.e("request of Package detail as per duration", Gson().toJson(packageRequest))

        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiGetPackageList(packageRequest)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of Package detail as per duration", Gson().toJson(response.body()))
                if (datas.status == true) {

                    val gson = Gson()
                    val typeRegularPackageList = object : TypeToken<java.util.ArrayList<PackageData>>() {}.type
                    val regularList: java.util.ArrayList<PackageData> =
                        gson.fromJson(gson.toJson(datas.data), typeRegularPackageList)

                    selectedPackage = regularList[0]

                    packageDetailLiveData.setValue(selectedPackage)
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
     * Seller normal registration
     * @param companyRegister request model class
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

        val times: MultipartBody.Part = MultipartBody.Part.createFormData("times",
            Gson().toJson(companyRegister.times));

        val timesMap: HashMap<String, String> = HashMap()

        for((index, contributor) in companyRegister.times.withIndex()){

            timesMap["times[${index}][day]"] = "${contributor.day}"
            timesMap["times[${index}][open]"] = "${contributor.open}"
            timesMap["times[${index}][close]"] = "${contributor.close}"

        }


        fileToUpload?.let {
           val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiCompanyRegisterWithImage(it, seller_id, name, ownername,
               phone, mobielNo, amobile, email, website, country,state, city, houseno, streetno, landmark, pincode, detail, officetime, package_id, custom_request,
               offer, post, notification, duration, companyRegister.type,
               timesMap, companyRegister.garage_subcategory, companyRegister.emergency_subcategory
               , companyRegister.breakdown_subcategory, companyRegister.insurance_subcategory,
               lat, long,company_type,specialize_in)
           loginCallBackCall.enqueue(object : Callback<Datas?> {
               override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                   val datas: Datas = response.body()!!
                   Log.e("response of regiter", Gson().toJson(response.body()))
                   if (datas.status == true) {
                       //  successMessage.value = datas.getMessage()
                       val gson = Gson()
                       val typeSuccessRegsiterList = object : TypeToken<ArrayList<Login>>() {}.type
                       val regsiterList: ArrayList<Login> =
                           gson.fromJson(gson.toJson(datas.data), typeSuccessRegsiterList)

                       successRegisterLiveData.setValue(regsiterList[0])
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
               offer, post, notification, duration, companyRegister.type,
               timesMap, companyRegister.garage_subcategory, companyRegister.emergency_subcategory
               , companyRegister.breakdown_subcategory, companyRegister.insurance_subcategory,
               lat, long,company_type, specialize_in)
           loginCallBackCall.enqueue(object : Callback<Datas?> {
               override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                   val datas: Datas = response.body()!!
                   Log.e("response of regiter", Gson().toJson(response.body()))
                   if (datas.status == true) {
                       //  successMessage.value = datas.getMessage()
                       val gson = Gson()
                       val typeSuccessRegsiterList = object : TypeToken<ArrayList<Login>>() {}.type
                       val regsiterList: ArrayList<Login> =
                           gson.fromJson(gson.toJson(datas.data), typeSuccessRegsiterList)

                       successRegisterLiveData.setValue(regsiterList[0])
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


//    private fun UploadFile(file: File) {
//        val interfaceFileUpload: InterfaceFileUpload = ApiClient.getRetrofitInstance().create(
//            InterfaceFileUpload::class.java)
//        //        InterfaceFileUpload interfaceFileUpload = retrofit.create(InterfaceFileUpload.class);
//        Log.e("--URL--", "interfaceDesignation: " + interfaceFileUpload.toString())
//        val mFile: okhttp3.RequestBody = okhttp3.RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
//        val fileToUpload: MultipartBody.Part = MultipartBody.Part.createFormData("file",
//            file.name, mFile)
//
//        ///RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//        val CustomerID: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "CIK002")
//        val BackupType: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "Document")
//        val ProductName: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "Retail")
//        val AppVersionAppVersion: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "1.22.3")
//        val AppType: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "Mobile")
//        val IMEINumber: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "23234234")
//        val DeviceInfo: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "Motorola")
//        val Remark: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "@Backup")
//        val FileName: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, "@SHAP_Backup")
//        val Extentsion: okhttp3.RequestBody = okhttp3.RequestBody.create(
//            MultipartBody.FORM, ".zip")
//        val fileUpload: okhttp3.Call<UploadResponse> =
//            interfaceFileUpload.UploadFile(fileToUpload,
//                CustomerID,
//                BackupType,
//                ProductName,
//                AppVersionAppVersion,
//                AppType,
//                IMEINumber,
//                DeviceInfo,
//                Remark,
//                FileName,
//                Extentsion)
//        Log.e("--URL--", "************************  before call : " +
//                fileUpload.request().url())
//        fileUpload.enqueue(object : okhttp3.Callback<UploadResponse?>() {
//            fun onResponse(
//                call: okhttp3.Call<UploadResponse?>?,
//                response: okhttp3.Response<UploadResponse?>?,
//            ) {
//                if (response != null && response.code() === 200) {
//                    Log.e("getResponse", "--Response:-" + response.message())
//                    Log.e("getResponse", "--Response:-" + response.body().toString())
//                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
//                        Toast.makeText(this@MainActivity,
//                            response.body().getMessage(),
//                            Toast.LENGTH_LONG).show()
//                    }
//                }
//            }
//
//            fun onFailure(call: okhttp3.Call<UploadResponse?>?, t: Throwable?) {}
//        })
//    }


}