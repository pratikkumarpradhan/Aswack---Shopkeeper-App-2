package com.avdhootsolutions.aswack_shopkeeper.models

import com.google.gson.annotations.SerializedName

class OrderRequest {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("order_code")
    var order_code: String? = null

    @SerializedName("quotation_id")
    var quotation_id: String? = null

    @SerializedName("invoice_id")
    var invoice_id: String? = null

    @SerializedName("invoice_code")
    var invoice_code: String? = null

    @SerializedName("seller_id")
    var seller_id: String? = null

    @SerializedName("seller_name")
    var seller_name: String? = null

    @SerializedName("seller_company_id")
    var seller_company_id: String? = null

    @SerializedName("seller_company_name")
    var seller_company_name: String? = null

    @SerializedName("user_id")
    var user_id: String? = null

    @SerializedName("user_name")
    var user_name: String? = null

    @SerializedName("txn_date")
    var txn_date: String? = null

    @SerializedName("txn_id")
    var txn_id: String? = null

    @SerializedName("payment_status")
    var payment_status: String? = null

    @SerializedName("paid_amount")
    var paid_amount: String? = null

    @SerializedName("remark")
    var remark: String? = null

    @SerializedName("status")
    var status:String? = null

    @SerializedName("created_id")
    var created_id:String? = null

    @SerializedName("created_datetime")
    var created_datetime:String? = null
}