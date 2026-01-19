package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class PackageData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("pkd_id")
    var pkd_id: String? = null

    @SerializedName("typeid")
    var typeid: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("country")
    var country: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("offer")
    var offer: String? = null

    @SerializedName("post")
    var post: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("notification")
    var notification: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("duration")
    var duration: ArrayList<String> = ArrayList()

    @SerializedName("status")
    var status: String? = null




}