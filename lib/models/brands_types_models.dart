List<BrandType> _parseTypeList(dynamic raw) {
  if (raw == null) return [];
  if (raw is! List) return [];
  final result = <BrandType>[];
  for (final e in raw) {
    if (e is Map<String, dynamic>) {
      result.add(BrandType.fromJson(e));
    } else if (e is Map) {
      result.add(BrandType.fromJson(Map<String, dynamic>.from(e)));
    }
  }
  return result;
}

List<BrandModel> _parseModelList(dynamic raw) {
  if (raw == null) return [];
  if (raw is! List) return [];
  final result = <BrandModel>[];
  for (final e in raw) {
    if (e is Map<String, dynamic>) {
      result.add(BrandModel.fromJson(e));
    } else if (e is Map) {
      result.add(BrandModel.fromJson(Map<String, dynamic>.from(e)));
    }
  }
  return result;
}

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
        typeList = _parseTypeList(json['type_list']);
}

class BrandType {
  String? vehicleTypeName;
  String? vehicleTypeId;
  List<BrandModel> modelList = [];
  bool isChecked = false;

  BrandType.fromJson(Map<String, dynamic> json)
      : vehicleTypeName =
            json['vehicle_type_name']?.toString() ?? json['type_name']?.toString(),
        vehicleTypeId = json['vehicle_type_id']?.toString() ?? json['type_id']?.toString(),
        modelList = _parseModelList(json['model_list']);
}

class BrandModel {
  String? vehicleModelName;
  String? vehicleModelId;
  bool isChecked = false;

  BrandModel.fromJson(Map<String, dynamic> json)
      : vehicleModelName = json['vehicle_model_name']?.toString() ?? json['model_name']?.toString(),
        vehicleModelId = json['vehicle_model_id']?.toString() ?? json['model_id']?.toString();
}
