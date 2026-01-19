package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class SubCategories {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("cat_id")
    var cat_id: String? = null

    @SerializedName("sub_cat_name")
    var sub_cat_name: String? = null

    var isChecked = false

}