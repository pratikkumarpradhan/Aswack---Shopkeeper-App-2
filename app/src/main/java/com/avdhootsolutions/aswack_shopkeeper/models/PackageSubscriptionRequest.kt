package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class PackageSubscriptionRequest {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("payment_id")
    var payment_id: String? = null

    @SerializedName("company_id")
    var company_id: String? = null

    @SerializedName("type")
    var type: String? = null

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

    @SerializedName("txn_id")
    var txn_id: String? = null

    @SerializedName("txn_date")
    var txn_date: String? = null

    @SerializedName("paid_amount")
    var paid_amount: String? = null

}