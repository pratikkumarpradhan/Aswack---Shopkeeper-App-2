package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OfficeTimings: Serializable{
    @SerializedName("day")
    @Expose
    var day: String? = null

    @SerializedName("open")
    @Expose
    var open: String? = null

    @SerializedName("close")
    @Expose
    var close: String? = null
}