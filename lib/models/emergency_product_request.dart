class EmergencyProductRequest {
  String? sellerId;
  String? sellerCompanyId;
  String? packagePurchasedId;
  String? masterCategoryId; // "5" for Emergency
  String? vehicleCategoryId;
  String? vehicleCompanyId;
  String? productName;
  String? specializeIn;
  String? description;
  String? price;

  EmergencyProductRequest({
    this.sellerId,
    this.sellerCompanyId,
    this.packagePurchasedId,
    this.masterCategoryId,
    this.vehicleCategoryId,
    this.vehicleCompanyId,
    this.productName,
    this.specializeIn,
    this.description,
    this.price,
  });
}

