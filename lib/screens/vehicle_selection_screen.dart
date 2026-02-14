import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import 'add_vehicle_screen.dart';
import 'sell_vehicle_list_screen.dart';
import 'buy_vehicle_screen.dart';
import '../services/api_service.dart';
import '../models/categories.dart';

/// Full-screen vehicle selection - "What are you selling?" or "What do you want to buy?"
class VehicleSelectionScreen extends StatefulWidget {
  final bool isSellMode; // true = sell, false = buy
  final String companyId;
  final String packageId;

  const VehicleSelectionScreen({
    Key? key,
    required this.isSellMode,
    this.companyId = '',
    this.packageId = '',
  }) : super(key: key);

  @override
  State<VehicleSelectionScreen> createState() => _VehicleSelectionScreenState();
}

class _VehicleSelectionScreenState extends State<VehicleSelectionScreen> {
  bool _isLoading = true;
  List<Categories> _categories = [];
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadCategories();
  }

  Future<void> _loadCategories() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final response = await ApiService.getVehicleCategories();

    if (!mounted) return;

    setState(() {
      _isLoading = false;
      if (response.status) {
        _categories = response.getCategoriesList();
      } else {
        _errorMessage = response.message;
      }
    });
  }

  List<_CategoryItem> get _displayCategories {
    if (_categories.isNotEmpty) {
      return _categories
          .map((c) => _CategoryItem(
                id: c.id ?? '',
                name: c.name ?? c.categoryName ?? 'Vehicle',
                subText: c.subText,
                image: c.image,
              ))
          .toList();
    }
    // Fallback - always show these so user can proceed
    return const [
      _CategoryItem(id: '1', name: 'Car'),
      _CategoryItem(id: '2', name: 'Bike'),
      _CategoryItem(id: '3', name: 'Scooter'),
    ];
  }

  void _onCategorySelected(String categoryId) {
    if (widget.isSellMode) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => AddVehicleScreen(
            categoryId: categoryId,
            companyId: widget.companyId,
            packageId: widget.packageId,
          ),
        ),
      );
    } else {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => BuyVehicleScreen(categoryId: categoryId),
        ),
      );
    }
  }

  void _onViewMyVehicles() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => const SellVehicleListScreen(),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    final title = widget.isSellMode
        ? 'What are you selling?'
        : 'What do you want to buy?';

    return Scaffold(
      backgroundColor: AppColors.yellow,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        foregroundColor: AppColors.black,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(
          widget.isSellMode ? 'Sell Vehicle' : 'Buy Vehicle',
          style: const TextStyle(
            color: AppColors.black,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: _isLoading
          ? const Center(
              child: CircularProgressIndicator(
                color: AppColors.purple700,
              ),
            )
          : Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Padding(
                  padding: const EdgeInsets.all(24),
                  child: Text(
                    title,
                    style: AppTextStyles.textView18ssp().copyWith(
                      fontWeight: FontWeight.bold,
                      color: AppColors.black,
                    ),
                  ),
                ),
                if (_errorMessage != null)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 24),
                    child: Text(
                      _errorMessage!,
                      style: const TextStyle(color: Colors.red, fontSize: 12),
                    ),
                  ),
                if (widget.isSellMode) ...[
                  _buildOptionTile(
                    icon: Icons.list,
                    title: 'View My Vehicles',
                    subtitle: 'See all your listed vehicles',
                    onTap: _onViewMyVehicles,
                  ),
                  const Divider(height: 1, color: AppColors.black),
                ],
                Expanded(
                  child: ListView(
                    padding: const EdgeInsets.all(16),
                    children: _displayCategories
                        .map((cat) => _buildCategoryTile(cat))
                        .toList(),
                  ),
                ),
              ],
            ),
    );
  }

  Widget _buildOptionTile({
    required IconData icon,
    required String title,
    required String subtitle,
    required VoidCallback onTap,
  }) {
    return ListTile(
      leading: Icon(icon, color: AppColors.black, size: 32),
      title: Text(
        title,
        style: const TextStyle(
          color: AppColors.black,
          fontWeight: FontWeight.w600,
          fontSize: 16,
        ),
      ),
      subtitle: Text(
        subtitle,
        style: TextStyle(
          color: AppColors.black.withOpacity(0.7),
          fontSize: 13,
        ),
      ),
      onTap: onTap,
    );
  }

  Widget _buildCategoryTile(_CategoryItem cat) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      color: Colors.white,
      child: ListTile(
        contentPadding: const EdgeInsets.symmetric(horizontal: 20, vertical: 12),
        leading: cat.image != null && cat.image!.isNotEmpty
            ? ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: Image.network(
                  cat.image!,
                  width: 50,
                  height: 50,
                  fit: BoxFit.cover,
                  errorBuilder: (_, __, ___) => _buildIconPlaceholder(),
                ),
              )
            : _buildIconPlaceholder(),
        title: Text(
          cat.name,
          style: const TextStyle(
            color: AppColors.black,
            fontWeight: FontWeight.w600,
            fontSize: 16,
          ),
        ),
        subtitle: cat.subText != null && cat.subText!.isNotEmpty
            ? Padding(
                padding: const EdgeInsets.only(top: 4),
                child: Text(
                  cat.subText!,
                  style: TextStyle(
                    color: AppColors.black.withOpacity(0.7),
                    fontSize: 13,
                  ),
                ),
              )
            : null,
        trailing: const Icon(Icons.chevron_right, color: AppColors.black),
        onTap: () => _onCategorySelected(cat.id),
      ),
    );
  }

  Widget _buildIconPlaceholder() {
    return Container(
      width: 50,
      height: 50,
      decoration: BoxDecoration(
        color: AppColors.gray.withOpacity(0.3),
        borderRadius: BorderRadius.circular(8),
      ),
      child: const Icon(Icons.directions_car, color: AppColors.black),
    );
  }
}

class _CategoryItem {
  final String id;
  final String name;
  final String? subText;
  final String? image;

  const _CategoryItem({
    required this.id,
    required this.name,
    this.subText,
    this.image,
  });
}
