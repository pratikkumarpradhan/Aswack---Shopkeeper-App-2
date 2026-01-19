package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class NotificationSendData {

    @SerializedName("type")
    var type: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("body")
    var body: String? = null

    @SerializedName("activity")
    var activity: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("booking_id")
    var booking_id: String? = null

    @SerializedName("rfq_id")
    var rfq_id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("company_id")
    var company_id: String? = null

}