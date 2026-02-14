class Categories {
  String? id;
  String? name;
  String? subText;
  String? categoryName;
  String? category;
  String? image;
  bool isChecked = false;

  Categories.fromJson(Map<String, dynamic> json)
      : id = json['id']?.toString(),
        name = json['name']?.toString() ?? json['category_name']?.toString(),
        subText = json['sub_text']?.toString(),
        categoryName = json['category_name']?.toString(),
        category = json['category']?.toString(),
        image = json['image']?.toString();
}
