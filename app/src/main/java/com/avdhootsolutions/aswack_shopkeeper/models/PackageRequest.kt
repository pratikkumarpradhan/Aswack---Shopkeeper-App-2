package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class PackageRequest {

    @SerializedName("pkd_id")
    var pkd_id: String? = null

    @SerializedName("pkdid")
    var pkdid: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("duration")
    var duration: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null
}