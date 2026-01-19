package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class CompanyDetails {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("company_code")
    var company_code: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("master_category_id")
    var master_category_id: String? = null

    @SerializedName("type")
    var type: ArrayList<String> = ArrayList()

    @SerializedName("company_name")
    var company_name: String? = null

    @SerializedName("owner_name")
    var owner_name: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("company_phone")
    var company_phone: String? = null

    @SerializedName("company_mobile")
    var company_mobile: String? = null

    @SerializedName("company_alt_mobile")
    var company_alt_mobile: String? = null

    @SerializedName("company_email_id")
    var company_email_id: String? = null

    @SerializedName("company_website")
    var company_website: String? = null

    @SerializedName("desciption")
    var desciption: String? = null

    @SerializedName("is_wishlist")
    var is_wishlist: String? = null

    @SerializedName("building_number")
    var building_number: String? = null

    @SerializedName("street_number")
    var street_number: String? = null

    @SerializedName("landmark")
    var landmark: String? = null

    @SerializedName("pincode")
    var pincode: String? = null

    @SerializedName("city_id")
    var city_id: String? = null

    @SerializedName("state_id")
    var state_id: String? = null

    @SerializedName("country_id")
    var country_id: String? = null

    @SerializedName("lat")
    var lat: String? = null

    @SerializedName("long")
    var long: String? = null

    @SerializedName("review")
    var reviewClass = ReviewClass()


}