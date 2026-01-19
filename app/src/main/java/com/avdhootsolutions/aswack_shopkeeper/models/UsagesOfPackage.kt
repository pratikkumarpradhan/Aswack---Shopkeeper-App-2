package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class UsagesOfPackage {
    @SerializedName("package_total")
    var package_total: String? = null

    @SerializedName("package_used")
    var package_used: String? = null

    @SerializedName("package_pending")
    var package_pending: String? = null

    @SerializedName("start_date")
    var start_date: String? = null

    @SerializedName("end_date")
    var end_date: String? = null
}