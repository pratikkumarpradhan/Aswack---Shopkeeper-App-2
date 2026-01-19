package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class AdminNotificationList {

    @SerializedName("notification_id")
    var notification_id: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("delivery_status")
    var delivery_status: String? = null
}