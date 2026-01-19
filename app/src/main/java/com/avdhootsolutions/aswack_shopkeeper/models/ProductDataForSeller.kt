package com.avdhootsolutions.aswack_shopkeeper.models

import java.util.*

class ProductDataForSeller{

    var rfqId = ""
    var rfqCode = ""

    var productId = ""
    var productName = ""

    var categoryId = ""

    var userId: String = ""
    var userName : String = ""

    var offerId: String = ""
    var offerName : String = ""

    var companyId = ""
    var companyName = ""

    var quotationId = ""
    var paymentDone = false
    var quotationRequest = QuotationRequest()

    var onlyCompanyDetail = false
    var onlyOfferDetails = false
    var onlyProductDetails = false
    var onlyRFQDetails = false

    val timestamp: Date = Calendar.getInstance().time
}
