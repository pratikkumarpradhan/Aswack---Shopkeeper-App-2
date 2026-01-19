package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class BuyVehicle {

    @SerializedName("category")
    var category: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_type")
    var user_type: String? = null

    @SerializedName("vehicle_cat")
    var vehicle_cat: String? = null

    @SerializedName("vehicle_brand")
    var vehicle_brand: ArrayList<String> = ArrayList<String>()

    @SerializedName("vehicle_type")
    var vehicle_type: ArrayList<String> = ArrayList<String>()

    @SerializedName("vehicle_model")
    var vehicle_model: ArrayList<String> = ArrayList<String>()

    @SerializedName("vehicle_year")
    var vehicle_year: ArrayList<String> = ArrayList<String>()

    @SerializedName("vehicle_fuel")
    var vehicle_fuel: ArrayList<String> = ArrayList<String>()

    @SerializedName("transmission")
    var transmission: String? = null



}