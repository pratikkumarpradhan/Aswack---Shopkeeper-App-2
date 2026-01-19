package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class ReviewClass {
    @SerializedName("rate_1")
    var rate_1 = ""

    @SerializedName("rate_2")
    var rate_2 = ""

    @SerializedName("rate_3")
    var rate_3 = ""

    @SerializedName("rate_4")
    var rate_4 = ""

    @SerializedName("rate_5")
    var rate_5 = ""

    @SerializedName("average")
    var average = ""

    @SerializedName("total")
    var total = ""
}