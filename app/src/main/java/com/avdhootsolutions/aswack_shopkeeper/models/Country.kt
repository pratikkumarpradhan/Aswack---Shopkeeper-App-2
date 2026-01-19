package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Country {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("shortname")
    var shortname: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("code")
    var code: String? = null

    @SerializedName("symbol")
    var symbol: String? = null



}