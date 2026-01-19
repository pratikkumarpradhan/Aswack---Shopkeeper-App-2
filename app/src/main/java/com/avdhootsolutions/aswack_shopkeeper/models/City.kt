package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("state_id")
    var state_id: String? = null

    @SerializedName("city_name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

}