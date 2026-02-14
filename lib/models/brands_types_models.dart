class BrandsTypesModels {
  String? vehicleBrandName;
  String? vehicleBrandId;
  List<BrandType> typeList = [];
  bool isChecked = false;

  BrandsTypesModels.fromJson(Map<String, dynamic> json)
      : vehicleBrandName =
            json['vehicle_company_name']?.toString() ?? json['vehicle_brand_name']?.toString(),
        vehicleBrandId =
            json['vehicle_company_id']?.toString() ?? json['vehicle_brand_id']?.toString(),
        typeList = (json['type_list'] as List?)
                ?.map((e) => BrandType.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [];
}

class BrandType {
  String? vehicleTypeName;
  String? vehicleTypeId;
  List<BrandModel> modelList = [];
  bool isChecked = false;

  BrandType.fromJson(Map<String, dynamic> json)
      : vehicleTypeName =
            json['vehicle_type_name']?.toString(),
        vehicleTypeId = json['vehicle_type_id']?.toString(),
        modelList = (json['model_list'] as List?)
                ?.map((e) => BrandModel.fromJson(e as Map<String, dynamic>))
                .toList() ??
            [];
}

class BrandModel {
  String? vehicleModelName;
  String? vehicleModelId;
  bool isChecked = false;

  BrandModel.fromJson(Map<String, dynamic> json)
      : vehicleModelName = json['vehicle_model_name']?.toString(),
        vehicleModelId = json['vehicle_model_id']?.toString();
}
