package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class CompanyRegister {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("payment_id")
    var payment_id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("company_id")
    var company_id: String? = null

    @SerializedName("specialize_in")
    var specialize_in: String? = null

    @SerializedName("company_type")
    var company_type: String? = null

    @SerializedName("type")
    var type: ArrayList<String> = ArrayList()

    @SerializedName("garage_subcategory")
    var garage_subcategory: ArrayList<String> = ArrayList()

    @SerializedName("emergency_subcategory")
    var emergency_subcategory: ArrayList<String> = ArrayList()

    @SerializedName("breakdown_subcategory")
    var breakdown_subcategory: ArrayList<String> = ArrayList()

    @SerializedName("insurance_subcategory")
    var insurance_subcategory: ArrayList<String> = ArrayList()

    @SerializedName("name")
    var name: String? = null

    @SerializedName("ownername")
    var ownername: String? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("amobile")
    var amobile: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("website")
    var website: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("state")
    var state: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("houseno")
    var houseno: String? = null

    @SerializedName("streetno")
    var streetno: String? = null

    @SerializedName("landmark")
    var landmark: String? = null

    @SerializedName("pincode")
    var pincode: String? = null

    @SerializedName("detail")
    var detail: String? = null

    @SerializedName("officetime")
    var officetime: String? = null

    @SerializedName("package_id")
    var package_id: String? = null

    @SerializedName("custom_request")
    var custom_request: String? = null

    @SerializedName("offer")
    var offer: String? = null

    @SerializedName("post")
    var post: String? = null

    @SerializedName("notification")
    var notification: String? = null

    @SerializedName("duration")
    var duration: String? = null

    @SerializedName("start_date")
    var start_date: String? = null

    @SerializedName("end_date")
    var end_date: String? = null

    @SerializedName("lat")
    var lat: String? = null

    @SerializedName("long")
    var long: String? = null

    @SerializedName("times")
    var times: ArrayList<OfficeTimings> = ArrayList()



}