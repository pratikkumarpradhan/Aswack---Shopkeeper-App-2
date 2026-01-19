package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class CourierDetails {

    /**
     * images list
     */
    var imageList = ArrayList<String>()

    @SerializedName("id")
    var id: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_name")
    var seller_name: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("seller_company_name")
    var seller_company_name: String? = null

    @SerializedName("item_name")
    var item_name: String? = null

    @SerializedName("weight")
    var weight: String? = null

    @SerializedName("dimensions")
    var dimensions: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("from_person_name")
    var from_person_name: String? = null

    @SerializedName("from_mobile")
    var from_mobile: String? = null

    @SerializedName("from_country_id")
    var from_country_id: String? = null

    @SerializedName("from_country_name")
    var from_country_name: String? = null

    @SerializedName("from_state_id")
    var from_state_id: String? = null

    @SerializedName("from_state_name")
    var from_state_name: String? = null

    @SerializedName("from_city_id")
    var from_city_id: String? = null

    @SerializedName("from_city_name")
    var from_city_name: String? = null

    @SerializedName("from_street_name")
    var from_street_name: String? = null

    @SerializedName("from_house_no")
    var from_house_no: String? = null

    @SerializedName("from_pincode")
    var from_pincode: String? = null

    @SerializedName("to_person_name")
    var to_person_name: String? = null

    @SerializedName("to_mobile")
    var to_mobile: String? = null

    @SerializedName("to_country_id")
    var to_country_id: String? = null

    @SerializedName("to_country_name")
    var to_country_name: String? = null

    @SerializedName("to_state_id")
    var to_state_id: String? = null

    @SerializedName("to_state_name")
    var to_state_name: String? = null

    @SerializedName("to_city_id")
    var to_city_id: String? = null

    @SerializedName("to_city_name")
    var to_city_name: String? = null

    @SerializedName("to_street_name")
    var to_street_name: String? = null

    @SerializedName("to_house_no")
    var to_house_no: String? = null

    @SerializedName("to_pincode")
    var to_pincode: String? = null


    @SerializedName("image1")
    var image1: String? = null

    @SerializedName("image2")
    var image2: String? = null

    @SerializedName("image3")
    var image3: String? = null

    @SerializedName("image4")
    var image4: String? = null

    @SerializedName("image5")
    var image5: String? = null

    @SerializedName("image6")
    var image6: String? = null

    @SerializedName("image7")
    var image7: String? = null

    @SerializedName("created_datetime")
    var created_datetime: String? = null

}