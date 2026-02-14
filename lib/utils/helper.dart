import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

class LoginData {
  String name = '';
  String email = '';
  String mobile = '';
  String password = '';
  String deviceToken = '';
  String id = '';

  Map<String, dynamic> toJson() => {
        'name': name,
        'email': email,
        'mobile': mobile,
        'password': password,
        'deviceToken': deviceToken,
        'id': id,
      };

  static LoginData fromJson(Map<String, dynamic> json) {
    final d = LoginData();
    d.name = json['name']?.toString() ?? '';
    d.email = json['email']?.toString() ?? '';
    d.mobile = json['mobile']?.toString() ?? '';
    d.password = json['password']?.toString() ?? '';
    d.deviceToken = json['deviceToken']?.toString() ?? '';
    d.id = json['id']?.toString() ?? '';
    return d;
  }
}

class Helper {
  static const _key = 'REGISTER_DATA';
  static LoginData? _cached;

  static Future<void> loadCache() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final data = prefs.getString(_key);
      if (data == null || data.isEmpty) {
        _cached = LoginData();
        return;
      }
      final map = jsonDecode(data) as Map<String, dynamic>?;
      _cached = map != null ? LoginData.fromJson(map) : LoginData();
    } catch (_) {
      _cached = LoginData();
    }
  }

  static LoginData getLoginData() => _cached ?? LoginData();

  static Future<void> setLoginData(LoginData data) async {
    _cached = data;
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_key, jsonEncode(data.toJson()));
    } catch (_) {}
  }

  static Future<void> clearLoginData() async {
    _cached = LoginData();
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString(_key, '');
    } catch (_) {}
  }
}
