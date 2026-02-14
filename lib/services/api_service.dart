import 'dart:convert';
import 'package:http/http.dart' as http;
import '../models/buy_vehicle.dart';
import '../models/sell_vehicle.dart';
import '../models/sell_vehicle_model.dart';
import '../models/categories.dart';
import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/fuels.dart';

class ApiService {
  static const String baseUrl = 'https://admin.aswack.com/api/';

  static Future<ApiResponse> login(String mobile, String password) async {
    try {
      final response = await http.post(
        Uri.parse('${baseUrl}login.php'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'mobile': mobile, 'password': password}),
      );
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> getVehicleCategories() async {
    try {
      final response = await http
          .post(
            Uri.parse('${baseUrl}vehicle_category.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode({'type': '0'}),
          )
          .timeout(const Duration(seconds: 15));
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static const _timeout = Duration(seconds: 15);

  static Future<ApiResponse> getVehicleBrandsTypesModels(String category) async {
    try {
      final response = await http
          .post(
            Uri.parse('${baseUrl}vehicle_brand.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode({'category': category}),
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> getYearList() async {
    try {
      final response = await http
          .get(
            Uri.parse('${baseUrl}vehicle_year.php'),
            headers: {'Content-Type': 'application/json'},
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> getFuelList() async {
    try {
      final response = await http
          .get(
            Uri.parse('${baseUrl}vehicle_fuel.php'),
            headers: {'Content-Type': 'application/json'},
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> getBuyVehicles(BuyVehicle buyVehicle) async {
    try {
      final response = await http.post(
        Uri.parse('${baseUrl}vehicle_buy_list.php'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(buyVehicle.toJson()),
      );
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> getVehicleSellList(String sellerId) async {
    try {
      final response = await http
          .post(
            Uri.parse('${baseUrl}vehicle_sell_list.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode({'seller_id': sellerId}),
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static Future<ApiResponse> submitSellVehicle(SellVehicleModel model) async {
    return sellVehicleWithImages(
      userId: model.userId ?? '0',
      sellerCompanyId: model.sellerCompanyId ?? '',
      userType: model.userType ?? '1',
      vehicleCat: model.vehicleCat ?? '',
      vehicleBrand: model.vehicleBrand ?? '',
      vehicleType: model.vehicleType ?? '',
      vehicleModel: model.vehicleModel ?? '',
      vehicleYear: model.vehicleYear ?? '',
      vehicleFuel: model.vehicleFuel ?? '',
      transmission: model.transmission ?? '0',
      drivenKm: model.drivenKm ?? '',
      title: model.title ?? '',
      owners: model.owners ?? '0',
      contactNumber: model.contactNumber ?? '',
      price: model.price ?? '',
      description: model.description ?? '',
      packagePurchasedId: model.packagePurchasedId ?? '',
      locationLongitude: model.locationLongitude,
      locationLatitude: model.locationLatitude,
      imagePaths: model.imagePaths,
    );
  }

  static Future<ApiResponse> sellVehicleWithImages({
    required String userId,
    required String sellerCompanyId,
    required String userType,
    required String vehicleCat,
    required String vehicleBrand,
    required String vehicleType,
    required String vehicleModel,
    required String vehicleYear,
    required String vehicleFuel,
    required String transmission,
    required String drivenKm,
    required String title,
    required String owners,
    required String contactNumber,
    required String price,
    required String description,
    required String packagePurchasedId,
    String? locationLongitude,
    String? locationLatitude,
    required List<String> imagePaths,
  }) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('${baseUrl}vehicle_sell.php'),
      );

      request.fields['seller_company_id'] = sellerCompanyId;
      request.fields['user_id'] = userId;
      request.fields['user_type'] = userType;
      request.fields['vehicle_cat'] = vehicleCat;
      request.fields['vehicle_brand'] = vehicleBrand;
      request.fields['vehicle_type'] = vehicleType;
      request.fields['vehicle_model'] = vehicleModel;
      request.fields['vehicle_year'] = vehicleYear;
      request.fields['vehicle_fuel'] = vehicleFuel;
      request.fields['transmission'] = transmission;
      request.fields['driven_km'] = drivenKm;
      request.fields['title'] = title;
      request.fields['owners'] = owners;
      request.fields['contact_number'] = contactNumber;
      request.fields['price'] = price;
      request.fields['description'] = description;
      request.fields['package_purchased_id'] = packagePurchasedId;
      request.fields['location_longitude'] = locationLongitude ?? '';
      request.fields['location_latitude'] = locationLatitude ?? '';

      for (var i = 0; i < imagePaths.length && i < 7; i++) {
        final file = await http.MultipartFile.fromPath(
          'image${i + 1}',
          imagePaths[i],
        );
        request.files.add(file);
      }

      final streamedResponse = await request.send();
      final response = await http.Response.fromStream(streamedResponse);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }

  static ApiResponse _parseResponse(http.Response response) {
    try {
      if (response.body.isEmpty) {
        return ApiResponse(status: false, message: 'Empty response');
      }
      final decoded = jsonDecode(response.body);
      if (decoded is! Map<String, dynamic>) {
        return ApiResponse(status: false, message: 'Invalid response format');
      }
      final data = decoded;
      final statusVal = data['status'];
      final status = statusVal == true ||
          statusVal == 1 ||
          statusVal == '1' ||
          statusVal == 'true';
      final message = data['message']?.toString() ?? '';
      final responseData = data['data'] ?? data['Data'] ?? data['result'];
      return ApiResponse(status: status, message: message, data: responseData);
    } catch (e) {
      return ApiResponse(status: false, message: e.toString());
    }
  }
}

class ApiResponse {
  final bool status;
  final String message;
  final dynamic data;

  ApiResponse({required this.status, required this.message, this.data});

  List<Categories> getCategoriesList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) => Categories.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  List<BrandsTypesModels> getBrandsTypesModelsList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) =>
            BrandsTypesModels.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  List<Years> getYearsList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) => Years.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  List<Fuels> getFuelsList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) => Fuels.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  List<SellVehicle> getSellVehicleList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) => SellVehicle.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }
}
