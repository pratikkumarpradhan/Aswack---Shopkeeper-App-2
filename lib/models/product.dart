class Product {
  String? id;
  String? productCode;
  String? sellerId;
  String? sellerName;
  String? sellerCompanyId;
  String? sellerCompanyName;
  String? companySellerId;
  String? packagePurchasedId;
  String? type;
  String? message;
  String? masterCategoryId;
  String? masterCategoryName;
  String? specializeIn;
  String? vehicleCatId;
  String? vehicleCatName;
  String? vehicleCompanyId;
  String? vehicleCompanyName;
  String? vehicleTypeId;
  String? vehicleTypeName;
  String? vehicleModelId;
  String? vehicleModelName;
  String? vehicleYearId;
  String? vehicleYearName;
  String? productName;
  String? serialNumber;
  String? price;
  String? description;
  String? productCondition;
  String? tyreWidth;
  String? rimDiameter;
  String? aspectRatio;
  String? loadIndex;
  String? speedIndex;
  String? tyreSize;
  String? isTubeless;
  String? image1;
  String? image2;
  String? image3;
  String? image4;
  String? image5;
  String? image6;
  String? image7;
  String? createdDatetime;

  Product({
    this.id,
    this.productCode,
    this.sellerId,
    this.sellerName,
    this.sellerCompanyId,
    this.sellerCompanyName,
    this.companySellerId,
    this.packagePurchasedId,
    this.type,
    this.message,
    this.masterCategoryId,
    this.masterCategoryName,
    this.specializeIn,
    this.vehicleCatId,
    this.vehicleCatName,
    this.vehicleCompanyId,
    this.vehicleCompanyName,
    this.vehicleTypeId,
    this.vehicleTypeName,
    this.vehicleModelId,
    this.vehicleModelName,
    this.vehicleYearId,
    this.vehicleYearName,
    this.productName,
    this.serialNumber,
    this.price,
    this.description,
    this.productCondition,
    this.tyreWidth,
    this.rimDiameter,
    this.aspectRatio,
    this.loadIndex,
    this.speedIndex,
    this.tyreSize,
    this.isTubeless,
    this.image1,
    this.image2,
    this.image3,
    this.image4,
    this.image5,
    this.image6,
    this.image7,
    this.createdDatetime,
  });

  factory Product.fromJson(Map<String, dynamic> json) {
    return Product(
      id: json['id']?.toString(),
      productCode: json['product_code']?.toString(),
      sellerId: json['seller_id']?.toString(),
      sellerName: json['seller_name']?.toString(),
      sellerCompanyId: json['seller_company_id']?.toString(),
      sellerCompanyName: json['seller_company_name']?.toString(),
      companySellerId: json['company_seller_id']?.toString(),
      packagePurchasedId: json['package_purchased_id']?.toString(),
      type: json['type']?.toString(),
      message: json['message']?.toString(),
      masterCategoryId: json['master_category_id']?.toString(),
      masterCategoryName: json['master_category_name']?.toString(),
      specializeIn: json['specialize_in']?.toString(),
      vehicleCatId: json['vehicle_cat_id']?.toString(),
      vehicleCatName: json['vehicle_cat_name']?.toString(),
      vehicleCompanyId: json['vehicle_company_id']?.toString(),
      vehicleCompanyName: json['vehicle_company_name']?.toString(),
      vehicleTypeId: json['vehicle_type_id']?.toString(),
      vehicleTypeName: json['vehicle_type_name']?.toString(),
      vehicleModelId: json['vehicle_model_id']?.toString(),
      vehicleModelName: json['vehicle_model_name']?.toString(),
      vehicleYearId: json['vehicle_year_id']?.toString(),
      vehicleYearName: json['vehicle_year_name']?.toString(),
      productName: json['product_name']?.toString(),
      serialNumber: json['serial_number']?.toString(),
      price: json['price']?.toString(),
      description: json['description']?.toString(),
      productCondition: json['product_condition']?.toString(),
      tyreWidth: json['tyre_width']?.toString(),
      rimDiameter: json['rim_diameter']?.toString(),
      aspectRatio: json['aspect_ratio']?.toString(),
      loadIndex: json['load_index']?.toString(),
      speedIndex: json['speed_index']?.toString(),
      tyreSize: json['tyre_size']?.toString(),
      isTubeless: json['is_tubeless']?.toString(),
      image1: json['image_1']?.toString(),
      image2: json['image_2']?.toString(),
      image3: json['image_3']?.toString(),
      image4: json['image_4']?.toString(),
      image5: json['image_5']?.toString(),
      image6: json['image_6']?.toString(),
      image7: json['image_7']?.toString(),
      createdDatetime: json['created_datetime']?.toString(),
    );
  }
}

