import 'package:flutter/material.dart';

import 'add_insurance_product_screen.dart';

/// Thin wrapper so Car Accessories has its own dedicated screen file,
/// while reusing the shared vehicle/brand/model/year logic.
class AddCarAccessoriesScreen extends StatelessWidget {
  final String vehicleCategoryId;
  final String? companyId;
  final String? packageId;

  const AddCarAccessoriesScreen({
    super.key,
    required this.vehicleCategoryId,
    this.companyId,
    this.packageId,
  });

  @override
  Widget build(BuildContext context) {
    return AddInsuranceProductScreen(
      mainCatId: '7', // Car Accessories main category (matches Android)
      vehicleCategoryId: vehicleCategoryId,
      companyId: companyId,
      packageId: packageId,
    );
  }
}

