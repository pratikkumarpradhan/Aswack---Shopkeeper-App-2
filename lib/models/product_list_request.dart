class ProductListRequest {
  String? sellerId;
  String? companySellerId;
  String? packagePurchasedId;
  String? masterCategoryId;

  ProductListRequest({
    this.sellerId,
    this.companySellerId,
    this.packagePurchasedId,
    this.masterCategoryId,
  });

  Map<String, dynamic> toJson() => {
        'seller_id': sellerId,
        'company_seller_id': companySellerId,
        'package_purchased_id': packagePurchasedId,
        'master_category_id': masterCategoryId,
      };
}

