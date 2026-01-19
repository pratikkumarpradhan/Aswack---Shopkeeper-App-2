package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Register {
    @SerializedName("seller_id")
    var seller_id : String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("email")
    var email: String? = null

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

    @SerializedName("pincode")
    var pincode: String? = null

    @SerializedName("dob")
    var dob: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("device_token")
    var device_token:String? = null

}