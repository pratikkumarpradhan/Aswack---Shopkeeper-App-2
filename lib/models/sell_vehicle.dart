class SellVehicle {
  String? id;
  String? sellVehicleId;
  String? category;
  String? advertisementCode;
  String? sellerCompanyId;
  String? sellerCompanyName;
  String? userId;
  String? sellerName;
  String? userType;
  String? vehicleCat;
  String? vehicleCatName;
  String? vehicleBrand;
  String? vehicleBrandName;
  String? vehicleType;
  String? vehicleTypeName;
  String? vehicleModel;
  String? vehicleModelName;
  String? vehicleYear;
  String? vehicleYearName;
  String? vehicleFuel;
  String? vehicleFuelName;
  String? transmission;
  String? drivenKm;
  String? title;
  String? owners;
  String? locationLongitude;
  String? contactNumber;
  String? locationLatitude;
  String? price;
  String? description;
  String? image1;
  String? image2;
  String? image3;
  String? image4;
  String? image5;
  String? image6;
  String? image7;
  String? createdDatetime;

  SellVehicle.fromJson(Map<String, dynamic> json)
      : id = json['id']?.toString(),
        sellVehicleId = json['sell_vehicle_id']?.toString(),
        category = json['category']?.toString(),
        advertisementCode = json['advertisement_code']?.toString(),
        sellerCompanyId = json['seller_company_id']?.toString(),
        sellerCompanyName = json['seller_company_name']?.toString(),
        userId = json['user_id']?.toString(),
        sellerName = json['seller_name']?.toString(),
        userType = json['user_type']?.toString(),
        vehicleCat = json['vehicle_cat']?.toString(),
        vehicleCatName = json['vehicle_cat_name']?.toString(),
        vehicleBrand = json['vehicle_brand']?.toString(),
        vehicleBrandName = json['vehicle_brand_name']?.toString(),
        vehicleType = json['vehicle_type']?.toString(),
        vehicleTypeName = json['vehicle_type_name']?.toString(),
        vehicleModel = json['vehicle_model']?.toString(),
        vehicleModelName = json['vehicle_model_name']?.toString(),
        vehicleYear = json['vehicle_year']?.toString(),
        vehicleYearName = json['vehicle_year_name']?.toString(),
        vehicleFuel = json['vehicle_fuel']?.toString(),
        vehicleFuelName = json['vehicle_fuel_name']?.toString(),
        transmission = json['transmission']?.toString(),
        drivenKm = json['driven_km']?.toString(),
        title = json['title']?.toString(),
        owners = json['owners']?.toString(),
        locationLongitude = json['location_longitude']?.toString(),
        contactNumber = json['contact_number']?.toString(),
        locationLatitude = json['location_latitude']?.toString(),
        price = json['price']?.toString(),
        description = json['description']?.toString(),
        image1 = json['image_1']?.toString(),
        image2 = json['image_2']?.toString(),
        image3 = json['image_3']?.toString(),
        image4 = json['image_4']?.toString(),
        image5 = json['image_5']?.toString(),
        image6 = json['image_6']?.toString(),
        image7 = json['image_7']?.toString(),
        createdDatetime = json['created_datetime']?.toString();
}
