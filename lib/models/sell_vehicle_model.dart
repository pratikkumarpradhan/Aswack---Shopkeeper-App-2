class SellVehicleModel {
  String? userId;
  String? sellerCompanyId;
  String? userType;
  String? vehicleCat;
  String? vehicleBrand;
  String? vehicleType;
  String? vehicleModel;
  String? vehicleYear;
  String? vehicleFuel;
  String? transmission;
  String? drivenKm;
  String? title;
  String? owners;
  String? locationLongitude;
  String? locationLatitude;
  String? contactNumber;
  String? price;
  String? description;
  String? packagePurchasedId;
  List<String> imagePaths = [];

  Map<String, dynamic> toJson() => {
        'user_id': userId,
        'seller_company_id': sellerCompanyId,
        'user_type': userType,
        'vehicle_cat': vehicleCat,
        'vehicle_brand': vehicleBrand,
        'vehicle_type': vehicleType,
        'vehicle_model': vehicleModel,
        'vehicle_year': vehicleYear,
        'vehicle_fuel': vehicleFuel,
        'transmission': transmission,
        'driven_km': drivenKm,
        'title': title,
        'owners': owners,
        'location_longitude': locationLongitude ?? '',
        'location_latitude': locationLatitude ?? '',
        'contact_number': contactNumber,
        'price': price,
        'description': description,
        'package_purchased_id': packagePurchasedId,
      };
}
