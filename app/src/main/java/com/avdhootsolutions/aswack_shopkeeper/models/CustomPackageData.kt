package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class CustomPackageData {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("request_code")
    var request_code: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("offer")
    var offer: String? = null

    @SerializedName("post")
    var post: String? = null

    @SerializedName("notification")
    var notification: String? = null

    @SerializedName("duration")
    var duration: String? = null

    @SerializedName("status")
    var status: String? = null
}