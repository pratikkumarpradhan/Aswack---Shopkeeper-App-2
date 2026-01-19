package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class UpdateSellVehicleRequest {

    @SerializedName("sell_vehicle_id")
    var sell_vehicle_id: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("vehicle_category")
    var vehicle_category: String? = null

    @SerializedName("vehicle_company")
    var vehicle_company: String? = null

    @SerializedName("vehicle_model_name")
    var vehicle_model_name: String? = null

    @SerializedName("vehicle_model_type")
    var vehicle_model_type: String? = null

    @SerializedName("vehicle_year")
    var vehicle_year: String? = null

    @SerializedName("vehicle_fuel")
    var vehicle_fuel: String? = null

    @SerializedName("transmission")
    var transmission: String? = null

    @SerializedName("driven_km")
    var driven_km: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("owners")
    var owners: String? = null

    @SerializedName("location_longitude")
    var location_longitude: String? = null

    @SerializedName("location_latitude")
    var location_latitude: String? = null

    @SerializedName("contact_number")
    var contact_number: String? = null

    @SerializedName("price")
    var price: String? = null

    @SerializedName("description")
    var description: String? = null


}