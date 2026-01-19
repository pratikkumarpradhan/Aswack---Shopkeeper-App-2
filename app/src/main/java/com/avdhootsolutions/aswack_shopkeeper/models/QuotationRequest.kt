package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class QuotationRequest {
    @SerializedName("quotation_code")
    var quotation_code: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_name")
    var user_name: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("master_category_id")
    var master_category_id: String? = null

    @SerializedName("offer_id")
    var offer_id: String? = null

    @SerializedName("product_id")
    var product_id: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("regular_price")
    var regular_price: String? = null

    @SerializedName("discounted_price")
    var discounted_price: String? = null

    @SerializedName("qty")
    var qty: String? = null

    @SerializedName("tax_extra")
    var tax_extra: String? = null

    @SerializedName("grand_total")
    var grand_total: String? = null

    @SerializedName("warranty")
    var warranty: String? = null

    @SerializedName("additional_details")
    var additional_details: String? = null

    @SerializedName("terms_conditions")
    var terms_conditions: String? = null

    @SerializedName("delivery_terms")
    var delivery_terms: String? = null

    @SerializedName("payment_done")
    var payment_done: String? = null

    @SerializedName("seller_name")
    var seller_name: String? = null

    @SerializedName("seller_company_name")
    var seller_company_name: String? = null

    @SerializedName("master_category_name")
    var master_category_name: String? = null

    @SerializedName("product_name")
    var product_name: String? = null

}