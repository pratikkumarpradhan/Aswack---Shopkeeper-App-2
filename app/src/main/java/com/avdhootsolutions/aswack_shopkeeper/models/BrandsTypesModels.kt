package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class BrandsTypesModels {
    @SerializedName("vehicle_company_name")
    var vehicle_brand_name: String? = null

    @SerializedName("vehicle_company_id")
    var vehicle_brand_id: String? = null

    @SerializedName("type_list")
    var type_list: ArrayList<Types> = ArrayList()

    var isChecked = false

    class Types{
        @SerializedName("vehicle_type_name")
        var vehicle_type_name: String? = null

        @SerializedName("vehicle_type_id")
        var vehicle_type_id: String? = null

        var isChecked = false

        @SerializedName("model_list")
        var model_list: ArrayList<Models> = ArrayList()

        class Models{
            @SerializedName("vehicle_model_name")
            var vehicle_model_name: String? = null

            @SerializedName("vehicle_model_id")
            var vehicle_model_id: String? = null

            var isChecked = false
        }
    }


}