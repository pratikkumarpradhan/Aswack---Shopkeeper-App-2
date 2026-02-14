import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

class SellVehiclePopupScreen extends StatelessWidget {
  final String title;
  final List<String> items;
  final String? selectedValue;

  const SellVehiclePopupScreen({
    super.key,
    required this.title,
    required this.items,
    this.selectedValue,
  });

  static Future<String?> show(
    BuildContext context, {
    required String title,
    required List<String> items,
    String? selectedValue,
  }) {
    return showModalBottomSheet<String>(
      context: context,
      backgroundColor: AppColors.yellow,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (context) => SellVehiclePopupScreen(
        title: title,
        items: items,
        selectedValue: selectedValue,
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return DraggableScrollableSheet(
      initialChildSize: 0.5,
      minChildSize: 0.3,
      maxChildSize: 0.8,
      expand: false,
      builder: (context, scrollController) {
        return Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Container(
              padding: const EdgeInsets.all(16),
              decoration: BoxDecoration(
                color: AppColors.yellow,
                borderRadius: const BorderRadius.vertical(top: Radius.circular(16)),
              ),
              child: Row(
                children: [
                  Text(
                    title,
                    style: const TextStyle(
                      color: AppColors.black,
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
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
            Container(height: 1, color: AppColors.black),
            Flexible(
              child: ListView.builder(
                controller: scrollController,
                padding: const EdgeInsets.symmetric(vertical: 8),
                itemCount: items.length,
                itemBuilder: (context, index) {
                  final item = items[index];
                  final isSelected = item == selectedValue;
                  return ListTile(
                    title: Text(
                      item,
                      style: TextStyle(
                        color: AppColors.black,
                        fontSize: 14,
                        fontWeight: isSelected ? FontWeight.w600 : FontWeight.normal,
                      ),
                    ),
                    trailing: isSelected
                        ? const Icon(Icons.check, color: AppColors.black, size: 20)
                        : null,
                    onTap: () => Navigator.pop(context, item),
                  );
                },
              ),
            ),
          ],
        );
      },
    );
  }
}
