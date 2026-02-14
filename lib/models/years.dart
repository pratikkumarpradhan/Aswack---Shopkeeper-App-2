class Years {
  String? id;
  String? year;
  bool isChecked = false;

  Years.fromJson(Map<String, dynamic> json)
      : id = json['id']?.toString(),
        year = json['year']?.toString();
}
