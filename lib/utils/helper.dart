class LoginData {
  String name = '';
  String email = '';
  String mobile = '';
  String password = '';
  String deviceToken = '';
  String id = '';
}

class Helper {
  static LoginData getLoginData() {
    // TODO: Implement SharedPreferences reading
    // For now, return empty LoginData
    return LoginData();
  }
  
  static void setLoginData(LoginData data) {
    // TODO: Implement SharedPreferences writing
  }
  
  static void clearLoginData() {
    // TODO: Implement SharedPreferences clearing
  }
}
