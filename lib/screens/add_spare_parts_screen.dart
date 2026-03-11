import 'package:flutter/material.dart';

import 'add_insurance_product_screen.dart';

/// Thin wrapper so Spare Parts has its own dedicated screen file,
/// while reusing the shared vehicle/brand/model/year logic.
class AddSparePartsScreen extends StatelessWidget {
  final String vehicleCategoryId;
  final String? companyId;
  final String? packageId;

  const AddSparePartsScreen({
    super.key,
    required this.vehicleCategoryId,
    this.companyId,
    this.packageId,
  });

  @override
  Widget build(BuildContext context) {
    return AddInsuranceProductScreen(
      mainCatId: '6', // Spare Parts main category (matches Android)
      vehicleCategoryId: vehicleCategoryId,
      companyId: companyId,
      packageId: packageId,
    );
  }
}

