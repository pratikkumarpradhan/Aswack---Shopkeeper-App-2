package com.avdhootsolutions.aswack_shopkeeper.models

class ProductData{

    var rfqId = ""
    var rfqCode = ""

    var productId = ""
    var productName = ""

    var categoryId = ""

    var sellerId: String = ""
    var sellerName : String = ""

    var companyId = ""
    var companyName = ""

    var offerId: String = ""
    var offerName : String = ""

    var quotationId = ""
    var paymentDone = false
    var quotationRequest = QuotationRequest()

    var onlyCompanyDetail = false
    var onlyOfferDetails = false
    var onlyProductDetails = false
    var onlyRFQDetails = false
}
