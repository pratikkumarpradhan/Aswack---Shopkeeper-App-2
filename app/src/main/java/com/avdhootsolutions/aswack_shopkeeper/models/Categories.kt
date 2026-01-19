package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Categories {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("sub_text")
    var sub_text: String? = null

    @SerializedName("category_name")
    var category_name: String? = null

    @SerializedName("category")
    var category: String? = null

    @SerializedName("image")
    var image: String? = null

    var isChecked = false

}