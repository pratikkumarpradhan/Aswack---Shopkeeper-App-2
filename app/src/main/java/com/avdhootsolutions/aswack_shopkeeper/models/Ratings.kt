package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class Ratings {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("rating_id")
    var rating_id: String? = null

    @SerializedName("seller_reply")
    var seller_reply: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_name")
    var user_name: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("master_category_id")
    var master_category_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("review_star")
    var review_star: String? = null

    @SerializedName("review_text")
    var review_text: String? = null

    @SerializedName("review_title")
    var review_title: String? = null

    @SerializedName("created_datetime")
    var created_datetime: String? = null

}