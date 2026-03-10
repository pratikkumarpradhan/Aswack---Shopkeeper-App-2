class BrandTypeModel {
  String? vehicleBrandId;
  String? vehicleBrandName;

  BrandTypeModel({
    this.vehicleBrandId,
    this.vehicleBrandName,
  });

  BrandTypeModel.fromJson(Map<String, dynamic> json)
      : vehicleBrandId = json['vehicle_brand_id']?.toString(),
        vehicleBrandName = json['vehicle_brand_name']?.toString();
}

