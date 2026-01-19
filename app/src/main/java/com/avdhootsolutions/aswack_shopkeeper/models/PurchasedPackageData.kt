package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class PurchasedPackageData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("seller_company_name")
    var seller_company_name: String? = null

    @SerializedName("category_list")
    var categoryList : ArrayList<Categories> = ArrayList()

    @SerializedName("package_id")
    var package_id: String? = null

    @SerializedName("custom_request_id")
    var custom_request_id: String? = null

    @SerializedName("typeid")
    var typeid: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("title")
    var title: String? = null

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
    var duration: String? = null

    @SerializedName("start_date")
    var start_date: String? = null

    @SerializedName("end_date")
    var end_date: String? = null

    @SerializedName("payment_status")
    var payment_status: String? = null

    @SerializedName("txn_date")
    var txn_date: String? = null

    @SerializedName("paid_amount")
    var paid_amount: String? = null

    @SerializedName("package_status")
    var package_status: String? = null

    @SerializedName("created_datetime")
    var created_datetime: String? = null







}