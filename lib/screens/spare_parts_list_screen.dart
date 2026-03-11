import 'package:flutter/material.dart';

import '../utils/strings.dart';
import 'emergency_service_list_screen.dart';

/// Spare Parts list screen (separate file).
/// Internally reuses the shared product-list UI + API logic.
class SparePartsListScreen extends StatelessWidget {
  final String? companyId;
  final String? packageId;

  const SparePartsListScreen({
    super.key,
    this.companyId,
    this.packageId,
  });

  @override
  Widget build(BuildContext context) {
    return EmergencyServiceListScreen(
      title: Strings.spare_parts,
      mainCatId: '6',
      companyId: companyId,
      packageId: packageId,
    );
  }
}

