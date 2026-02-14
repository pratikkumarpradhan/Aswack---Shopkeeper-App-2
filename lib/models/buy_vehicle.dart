class BuyVehicle {
  String? category;
  String? userId;
  String? userType;
  String? vehicleCat;
  List<String> vehicleBrand = [];
  List<String> vehicleType = [];
  List<String> vehicleModel = [];
  List<String> vehicleYear = [];
  List<String> vehicleFuel = [];
  String? transmission;

  Map<String, dynamic> toJson() => {
        'category': category,
        'user_id': userId,
        'user_type': userType ?? '1', // 1 = SELLER
        'vehicle_cat': vehicleCat,
        'vehicle_brand': vehicleBrand,
        'vehicle_type': vehicleType,
        'vehicle_model': vehicleModel,
        'vehicle_year': vehicleYear,
        'vehicle_fuel': vehicleFuel,
        'transmission': transmission ?? '',
      };
}
