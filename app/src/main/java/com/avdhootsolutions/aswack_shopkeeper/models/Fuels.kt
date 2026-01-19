package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Fuels {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("fuel")
    var fuel: String? = null

    var isChecked = false


}