import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

/// Reusable list screen for Garage, Insurance, Spare Parts, Tyre, Courier.
class CategoryListScreen extends StatefulWidget {
  final String title;
  final String mainCatId;
  final String? companyId;
  final String? packageId;

  const CategoryListScreen({
    super.key,
    required this.title,
    required this.mainCatId,
    this.companyId,
    this.packageId,
  });

  @override
  State<CategoryListScreen> createState() => _CategoryListScreenState();
}

class _CategoryListScreenState extends State<CategoryListScreen> {
  bool _isLoading = false;

  @override
  void initState() {
    super.initState();
    _loadItems();
  }

  void _loadItems() {
    setState(() => _isLoading = true);
    Future.delayed(const Duration(milliseconds: 800), () {
      if (mounted) setState(() => _isLoading = false);
    });
  }

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
        title: Text(
          widget.title,
          style: const TextStyle(color: AppColors.black, fontSize: 16, fontWeight: FontWeight.w600),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : Center(
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(Icons.inventory_2, size: 64, color: AppColors.gray),
                    const SizedBox(height: 16),
                    Text(
                      widget.title,
                      style: TextStyle(color: AppColors.purple700, fontSize: 16, fontWeight: FontWeight.w600),
                      textAlign: TextAlign.center,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'No items yet. Add products from Dashboard.',
                      style: TextStyle(color: AppColors.gray, fontSize: 13),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              ),
            ),
    );
  }
}
