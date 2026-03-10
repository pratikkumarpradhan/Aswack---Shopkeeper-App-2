import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';

/// Category IDs used by API / search. Must match backend or fallback data.
class BuyVehicleCategory {
  static const String twoWheeler = '1';
  static const String commercial = '2';
  static const String heavyEquipment = '3';
  static const String ambulance = '4';
  static const String trucks = '5';
  static const String excavators = '6';

  static const List<({String id, String label, IconData icon})> all = [
    (id: twoWheeler, label: 'Two wheeler / Bike', icon: Icons.two_wheeler),
    (id: commercial, label: 'Commercial vehicle', icon: Icons.local_shipping),
    (id: heavyEquipment, label: 'Heavy equipment', icon: Icons.construction),
    (id: ambulance, label: 'Ambulance', icon: Icons.medical_services),
    (id: trucks, label: 'Trucks', icon: Icons.fire_truck),
    (id: excavators, label: 'Excavators', icon: Icons.landscape),
  ];
}

/// Shows a popup with 6 card buttons for Buy Vehicle category selection.
/// Returns the selected categoryId when a card is tapped.
class BuyVehicleCategoryPopup extends StatelessWidget {
  const BuyVehicleCategoryPopup({super.key});

  static Future<String?> show(BuildContext context) {
    return showModalBottomSheet<String>(
      context: context,
      isScrollControlled: true,
      backgroundColor: Colors.transparent,
      builder: (context) => const BuyVehicleCategoryPopup(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: const BoxDecoration(
        color: AppColors.yellow,
        borderRadius: BorderRadius.vertical(top: Radius.circular(20)),
      ),
      child: SafeArea(
        top: false,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 16, 20, 8),
              child: Row(
                children: [
                  Text(
                    'Select vehicle type',
                    style: AppTextStyles.textView18ssp().copyWith(
                      fontWeight: FontWeight.bold,
                      color: AppColors.black,
                    ),
                  ),
                  const Spacer(),
                  IconButton(
                    icon: const Icon(Icons.close, color: AppColors.black),
                    onPressed: () => Navigator.pop(context),
                  ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
              child: GridView.count(
                shrinkWrap: true,
                physics: const NeverScrollableScrollPhysics(),
                crossAxisCount: 2,
                mainAxisSpacing: 12,
                crossAxisSpacing: 12,
                childAspectRatio: 1.1,
                children: BuyVehicleCategory.all.map((cat) {
                  return _CategoryCard(
                    label: cat.label,
                    icon: cat.icon,
                    onTap: () => Navigator.pop(context, cat.id),
                  );
                }).toList(),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _CategoryCard extends StatelessWidget {
  final String label;
  final IconData icon;
  final VoidCallback onTap;

  const _CategoryCard({
    required this.label,
    required this.icon,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return Material(
      color: AppColors.white,
      borderRadius: BorderRadius.circular(12),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(12),
        child: Container(
          padding: const EdgeInsets.all(12),
          decoration: BoxDecoration(
            borderRadius: BorderRadius.circular(12),
            border: Border.all(color: AppColors.viewColor),
            color: AppColors.white,
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(icon, size: 36, color: AppColors.purple700),
              const SizedBox(height: 8),
              Text(
                label,
                style: AppTextStyles.textView13ssp().copyWith(
                  fontWeight: FontWeight.w600,
                  color: AppColors.black,
                ),
                textAlign: TextAlign.center,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
