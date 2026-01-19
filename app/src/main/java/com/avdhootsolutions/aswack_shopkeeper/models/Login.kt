package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Login {

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_type")
    var user_type: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("master_category_id")
    var master_category_id: String? = null

    @SerializedName("package_purchased_id")
    var package_purchased_id: String? = null

    @SerializedName("cat_id")
    var category_id: String? = null


    @SerializedName("category")
    var categoryList: ArrayList<String> = ArrayList()

    @SerializedName("company_id")
    var company_id: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("custom_request_code")
    var custom_request_code: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("device_token")
    var device_token:String? = null


}