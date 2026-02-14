class Fuels {
  String? id;
  String? fuel;
  bool isChecked = false;

  Fuels.fromJson(Map<String, dynamic> json)
      : id = json['id']?.toString(),
        fuel = json['fuel']?.toString();
}
