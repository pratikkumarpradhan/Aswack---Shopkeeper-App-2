import 'dart:convert';
import 'dart:io';
import 'package:http/http.dart' as http;
import '../models/buy_vehicle.dart';
import '../models/sell_vehicle.dart';
import '../models/sell_vehicle_model.dart';
import '../models/categories.dart';
import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/fuels.dart';
import '../models/product_item.dart';
import '../models/product_list_request.dart';
import '../models/brand_type_model.dart';
import '../models/emergency_product_request.dart';
import '../models/insurance_product_request.dart';

class ApiService {
  static const String baseUrl = 'https://admin.aswack.com/api/';
  static const String imageBaseUrl = 'https://admin.aswack.com/';

  /// Resolves image URL - prepends base if relative path
  static String resolveImageUrl(String? url) {
    if (url == null || url.isEmpty) return '';
    if (url.startsWith('http://') || url.startsWith('https://')) return url;
    final base = imageBaseUrl.endsWith('/') ? imageBaseUrl : '$imageBaseUrl/';
    final path = url.startsWith('/') ? url.substring(1) : url;
    return '$base$path';
  }

  static const String _connectionErrorMsg =
      'Connection failed. Please check your internet and try again.';

  static String _messageFromError(Object e) {
    final s = e.toString().toLowerCase();
    if (s.contains('failed to fetch') ||
        s.contains('clientexception') ||
        s.contains('client expectation') ||
        s.contains('socketexception') ||
        s.contains('connection refused') ||
        s.contains('connection closed') ||
        s.contains('connection reset') ||
        s.contains('network is unreachable') ||
        s.contains('timeoutexception') ||
        s.contains('handshake') ||
        s.contains('certificate') ||
        s.contains('failed host')) {
      return _connectionErrorMsg;
    }
    return e.toString().length > 120
        ? '${e.toString().substring(0, 120)}...'
        : e.toString();
  }

  static Future<ApiResponse> login(String mobile, String password) async {
    try {
      final response = await http.post(
        Uri.parse('${baseUrl}login.php'),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'mobile': mobile, 'password': password}),
      );
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
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
      return ApiResponse(status: false, message: _messageFromError(e));
    }
  }

  static const _timeout = Duration(seconds: 30);

  static Future<ApiResponse> getVehicleBrandsTypesModels(String category) async {
    try {
      // Kotlin sends SellVehicle with only `category` for this endpoint.
      final body = {'category': category};
      final response = await http
          .post(
            Uri.parse('${baseUrl}vehicle_brand.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(body),
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
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
      return ApiResponse(status: false, message: _messageFromError(e));
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
      return ApiResponse(status: false, message: _messageFromError(e));
    }
  }

  static Future<ApiResponse> getBuyVehicles(BuyVehicle buyVehicle) async {
    try {
      final response = await http
          .post(
            Uri.parse('${baseUrl}vehicle_buy_list.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(buyVehicle.toJson()),
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
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
      return ApiResponse(status: false, message: _messageFromError(e));
    }
  }

  /// Generic product list (garage / emergency / breakdown) – product_list.php
  static Future<ApiResponse> getProductList(
      ProductListRequest request) async {
    try {
      final response = await http
          .post(
            Uri.parse('${baseUrl}product_list.php'),
            headers: {'Content-Type': 'application/json'},
            body: jsonEncode(request.toJson()),
          )
          .timeout(_timeout);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
    }
  }

  /// Insert garage/emergency/breakdown product – insert_product.php
  static Future<ApiResponse> insertEmergencyProduct(
      EmergencyProductRequest req, List<File> images) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('${baseUrl}insert_product.php'),
      );

      request.fields['seller_id'] = req.sellerId ?? '';
      request.fields['seller_company_id'] = req.sellerCompanyId ?? '';
      request.fields['package_purchased_id'] = req.packagePurchasedId ?? '';
      request.fields['master_category_id'] = req.masterCategoryId ?? '';
      request.fields['vehicle_category'] = req.vehicleCategoryId ?? '';
      request.fields['vehicle_company'] = req.vehicleCompanyId ?? '';
      request.fields['product_name'] = req.productName ?? '';
      request.fields['price'] = req.price ?? '';
      request.fields['description'] = req.description ?? '';
      request.fields['specialize_in'] = req.specializeIn ?? '';

      for (var i = 0; i < images.length && i < 6; i++) {
        final file = images[i];
        final part = await http.MultipartFile.fromPath(
          'image${i + 1}',
          file.path,
        );
        request.files.add(part);
      }

      final streamed = await request.send();
      final response = await http.Response.fromStream(streamed);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
    }
  }

  /// Insert insurance / heavy equipment / rent car product with vehicle details.
  /// Mirrors AddInsuranceViewModel.apiAddInsuranceProductWithFile (Kotlin).
  static Future<ApiResponse> insertInsuranceProduct(
      InsuranceProductRequest req, List<File> images) async {
    try {
      final request = http.MultipartRequest(
        'POST',
        Uri.parse('${baseUrl}insert_product.php'),
      );

      request.fields['seller_id'] = req.sellerId ?? '';
      request.fields['seller_company_id'] = req.sellerCompanyId ?? '';
      request.fields['package_purchased_id'] = req.packagePurchasedId ?? '';
      request.fields['master_category_id'] = req.masterCategoryId ?? '';
      request.fields['vehicle_category'] = req.vehicleCategoryId ?? '';
      request.fields['vehicle_company'] = req.vehicleCompanyId ?? '';
      request.fields['vehicle_model_type'] = req.vehicleModelTypeId ?? '';
      request.fields['vehicle_model_name'] = req.vehicleModelId ?? '';
      request.fields['vehicle_year'] = req.vehicleYearId ?? '';
      request.fields['product_name'] = req.productName ?? '';
      request.fields['serial_number'] = req.serialNumber ?? '';
      request.fields['price'] = req.price ?? '';
      request.fields['description'] = req.description ?? '';
      request.fields['product_condition'] = req.productCondition ?? '';

      for (var i = 0; i < images.length && i < 6; i++) {
        final file = images[i];
        final part = await http.MultipartFile.fromPath(
          'image${i + 1}',
          file.path,
        );
        request.files.add(part);
      }

      final streamed = await request.send();
      final response = await http.Response.fromStream(streamed);
      return _parseResponse(response);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
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
      return ApiResponse(status: false, message: _messageFromError(e));
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
      dynamic responseData = data['data'] ??
          data['Data'] ??
          data['result'] ??
          data['vehicle_list'] ??
          data['vehicles'] ??
          data['list'] ??
          data['brand_list'] ??
          data['year_list'] ??
          data['fuel_list'];
      // Unwrap nested list (e.g. { "data": { "list": [...] } })
      if (responseData is Map) {
        final map = Map<String, dynamic>.from(responseData);
        responseData = map['list'] ?? map['data'] ?? map['years'] ?? map['fuels'] ?? map['brands'] ?? responseData;
      }
      return ApiResponse(status: status, message: message, data: responseData);
    } catch (e) {
      return ApiResponse(status: false, message: _messageFromError(e));
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
    List list;
    if (data is List) {
      list = data as List;
    } else if (data is Map) {
      final map = Map<String, dynamic>.from(data as Map);
      final raw = map['vehicles'] ?? map['vehicle_list'] ?? map['list'] ?? map['data'];
      list = raw is List ? raw : [];
    } else {
      list = [];
    }
    return list
        .map((e) => SellVehicle.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  List<BrandTypeModel> getBrandTypeModelList() {
    if (data == null) return [];
    final list = data is List ? data : (data is Map ? [data] : []);
    return list
        .map((e) => BrandTypeModel.fromJson(
            e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }

  /// Map generic product list (garage / emergency / breakdown)
  List<ProductItem> getProductItemList() {
    if (data == null) return [];
    List list;
    if (data is List) {
      list = data as List;
    } else if (data is Map) {
      final map = Map<String, dynamic>.from(data as Map);
      final raw = map['list'] ?? map['data'] ?? map['products'];
      list = raw is List ? raw : [];
    } else {
      list = [];
    }
    return list
        .map((e) => ProductItem.fromJson(e is Map ? Map<String, dynamic>.from(e) : {}))
        .toList();
  }
}
