package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class RFQList {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_name")
    var seller_name: String? = null

    @SerializedName("rfq_code")
    var rfq_code: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_name")
    var user_name: String? = null

    @SerializedName("user_mobile")
    var user_mobile: String? = null

    @SerializedName("master_category_id")
    var master_category_id: String? = null

    @SerializedName("vehicle_cat")
    var vehicle_cat: String? = null

    @SerializedName("vehicle_cat_name")
    var vehicle_cat_name: String? = null

    @SerializedName("vehicle_brand")
    var vehicle_brand: String? = null

    @SerializedName("vehicle_brand_name")
    var vehicle_brand_name: String? = null

    @SerializedName("vehicle_type")
    var vehicle_type: String? = null

    @SerializedName("vehicle_type_name")
    var vehicle_type_name: String? = null

    @SerializedName("vehicle_model")
    var vehicle_model: String? = null

    @SerializedName("vehicle_model_name")
    var vehicle_model_name: String? = null

    @SerializedName("vehicle_year")
    var vehicle_year: String? = null

    @SerializedName("vehicle_year_name")
    var vehicle_year_name: String? = null

    @SerializedName("vehicle_fuel")
    var vehicle_fuel: String? = null

    @SerializedName("vehicle_fuel_name")
    var vehicle_fuel_name: String? = null

    @SerializedName("transmission")
    var transmission: String? = null

    @SerializedName("product_name")
    var product_name: String? = null

    @SerializedName("qty")
    var qty: String? = null

    @SerializedName("serial_number")
    var serial_number: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("product_condition")
    var product_condition: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("created_datetime")
    var created_datetime: String? = null

    var isChatInitiated = false;

    var productDataForSeller = ProductDataForSeller()

}