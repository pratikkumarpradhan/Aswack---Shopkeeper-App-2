package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class BookingStatusRequest {
    @SerializedName("appointment_id")
    var appointment_id: String? = null

    @SerializedName("rejected_reason")
    var rejected_reason: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("seller_name")
    var seller_name: String? = null

    @SerializedName("code")
    var code: String? = null
}