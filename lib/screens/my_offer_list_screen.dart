import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

class MyOfferListScreen extends StatelessWidget {
  final String? categoryId;
  final String? companyId;
  final String? packageId;

  const MyOfferListScreen({super.key, this.categoryId, this.companyId, this.packageId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('My Offers', style: TextStyle(color: AppColors.black, fontSize: 16)),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.local_offer, size: 64, color: AppColors.gray),
            const SizedBox(height: 16),
            Text('My Offers', style: TextStyle(color: AppColors.purple700, fontSize: 16)),
            const SizedBox(height: 8),
            Text('No offers yet', style: TextStyle(color: AppColors.gray, fontSize: 14)),
          ],
        ),
      ),
    );
  }
}
