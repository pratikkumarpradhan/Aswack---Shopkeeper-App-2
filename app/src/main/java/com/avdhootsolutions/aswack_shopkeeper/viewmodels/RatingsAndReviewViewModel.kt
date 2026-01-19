package com.avdhootsolutions.aswack_shopkeeper.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.avdhootsolutions.aswack_shopkeeper.MyApp
import com.avdhootsolutions.aswack_shopkeeper.models.Datas
import com.avdhootsolutions.aswack_shopkeeper.models.ProductList
import com.avdhootsolutions.aswack_shopkeeper.models.Ratings
import com.avdhootsolutions.aswack_shopkeeper.models.ReviewClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RatingsAndReviewViewModel : ViewModel() {

    /**
     * Live data for rating list
     */
    var ratingListLiveData: MutableLiveData<ArrayList<Ratings>> = MutableLiveData<ArrayList<Ratings>>()

    /**
     * Live data for review
     */
    var reviewsLiveData: MutableLiveData<ReviewClass> = MutableLiveData<ReviewClass>()

    /**
     * Live data for error
     */
    var errorMessage = MutableLiveData<String>()

    /**
     * Live data for error
     */
    var successMessageLiveData = MutableLiveData<Boolean>()

    var filePath = ""

    var companyId = ""

    init {
    }


    /**
     * get ratings
     * @param productList request model class
     */
    fun apiGetReview(productList: ProductList) {

        Log.e("request of get ratings", Gson().toJson(productList))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().getMyApi().apiGetRatings(productList)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of get ratings", Gson().toJson(response.body()))
                if (datas.status == true) {
                    val gson = Gson()

                    val reviews : ReviewClass = gson.fromJson(gson.toJson(datas.reviewClass), ReviewClass::class.java)
                    reviewsLiveData.value = reviews

                    val typeRatingList = object : TypeToken<ArrayList<Ratings>>() {}.type
                    val ratingList : ArrayList<Ratings> =
                        gson.fromJson(gson.toJson(datas.data), typeRatingList)
                    ratingListLiveData.setValue(ratingList)

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
     * Send reply to the customer
     * @param ratings request model class
     */
    fun apiSendReply(ratings: Ratings) {

        Log.e("request of submit rating reply", Gson().toJson(ratings))
        val loginCallBackCall: Call<Datas> = MyApp.getInstance().myApi.apiSubmitRatingReply(ratings)
        loginCallBackCall.enqueue(object : Callback<Datas?> {
            override fun onResponse(call: Call<Datas?>, response: Response<Datas?>) {
                val datas: Datas = response.body()!!
                Log.e("response of submit rating reply", Gson().toJson(response.body()))
                if (datas.status == true) {
                    successMessageLiveData.setValue(true)

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