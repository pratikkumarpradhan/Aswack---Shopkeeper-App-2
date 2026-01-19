package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class States {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("country_id")
    var country_id: String? = null

    @SerializedName("state_name")
    var name: String? = null

    @SerializedName("status")
    var status: String? = null

}