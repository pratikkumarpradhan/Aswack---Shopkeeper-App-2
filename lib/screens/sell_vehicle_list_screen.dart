import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';
import '../models/sell_vehicle.dart';
import '../services/api_service.dart';
import 'sell_vehicle_screen.dart';
import 'buy_vehicle_detail_screen.dart';

class SellVehicleListScreen extends StatefulWidget {
  final String? companyId;
  final String? categoryId;
  final String? packageId;

  const SellVehicleListScreen({
    Key? key,
    this.companyId,
    this.categoryId,
    this.packageId,
  }) : super(key: key);

  @override
  State<SellVehicleListScreen> createState() => _SellVehicleListScreenState();
}

class _SellVehicleListScreenState extends State<SellVehicleListScreen> {
  bool _isLoading = false;
  List<SellVehicle> _items = [];
  List<SellVehicle> _filteredItems = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadItems();
    _searchController.addListener(_onSearchChanged);
  }

  void _onSearchChanged() {
    setState(() {
      final query = _searchController.text.toLowerCase();
      if (query.isEmpty) {
        _filteredItems = List.from(_items);
      } else {
        _filteredItems = _items.where((v) {
          final brand = (v.vehicleBrandName ?? '').toLowerCase();
          final model = (v.vehicleModelName ?? '').toLowerCase();
          final title = (v.title ?? '').toLowerCase();
          return brand.contains(query) ||
              model.contains(query) ||
              title.contains(query);
        }).toList();
      }
    });
  }

  Future<void> _loadItems() async {
    setState(() => _isLoading = true);

    try {
      final sellerId = Helper.getLoginData().id;
      final response =
          await ApiService.getVehicleSellList(sellerId.isNotEmpty ? sellerId : '0');

      if (mounted) {
        if (response.status) {
          _items = response.getSellVehicleList();
          _filteredItems = List.from(_items);
        } else {
          _items = [];
          _filteredItems = [];
        }
        setState(() => _isLoading = false);
      }
    } catch (e) {
      if (mounted) {
        _items = [];
        _filteredItems = [];
        setState(() => _isLoading = false);
      }
    }
  }

  Future<void> _onAddProduct() async {
    final categoryId = await _showVehicleTypePopup();
    if (categoryId == null || !mounted) return;

    final result = await Navigator.push<bool>(
      context,
      MaterialPageRoute(
        builder: (context) => SellVehicleScreen(
          categoryId: categoryId,
          companyId: widget.companyId ?? '',
          packageId: widget.packageId ?? '',
        ),
      ),
    );

    if (result == true && mounted) {
      _loadItems();
    }
  }

  Future<String?> _showVehicleTypePopup() async {
    List<_CategoryItem> categories = [];
    try {
      final response = await ApiService.getVehicleCategories();
      if (!mounted) return null;
      if (response.status) {
        final list = response.getCategoriesList();
        categories = list
            .map((c) => _CategoryItem(
                  id: c.id ?? '',
                  name: c.name ?? c.categoryName ?? 'Vehicle',
                ))
            .toList();
      }
    } catch (_) {}

    if (categories.isEmpty) {
      categories = const [
        _CategoryItem(id: '1', name: 'Car'),
        _CategoryItem(id: '2', name: 'Bike'),
        _CategoryItem(id: '3', name: 'Scooter'),
      ];
    }

    if (!mounted) return null;
    return showModalBottomSheet<String>(
      context: context,
      backgroundColor: AppColors.yellow,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (context) => Container(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Text(
                  'Select Vehicle Type',
                  style: AppTextStyles.textView18ssp().copyWith(
                    fontWeight: FontWeight.w600,
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
            const SizedBox(height: 8),
            ...categories.map((cat) => ListTile(
                  title: Text(
                    cat.name,
                    style: const TextStyle(
                      color: AppColors.black,
                      fontSize: 16,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  trailing: const Icon(Icons.chevron_right, color: AppColors.black),
                  onTap: () => Navigator.pop(context, cat.id),
                )),
          ],
        ),
      ),
    );
  }

  void _onItemSelected(SellVehicle vehicle) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => BuyVehicleDetailScreen(vehicle: vehicle),
      ),
    );
  }

  @override
  void dispose() {
    _searchController.removeListener(_onSearchChanged);
    _searchController.dispose();
    super.dispose();
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
        title: const Text(
          'Sell Vehicle',
          style: TextStyle(
            color: AppColors.black,
            fontSize: 13,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      body: Column(
        children: [
          Container(height: 1, color: AppColors.viewColor),
          Padding(
            padding: const EdgeInsets.all(10),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                TextButton.icon(
                  onPressed: _onAddProduct,
                  icon: const Icon(Icons.add, size: 20, color: AppColors.white),
                  label: const Text(
                    'Add New Products',
                    style: TextStyle(
                      color: AppColors.white,
                      fontSize: 13,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                  style: TextButton.styleFrom(
                    backgroundColor: AppColors.purple700,
                    padding: const EdgeInsets.symmetric(
                      horizontal: 10,
                      vertical: 7,
                    ),
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 10),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Search by name, serial number...',
                hintStyle: TextStyle(color: AppColors.gray, fontSize: 13),
                prefixIcon: const Icon(Icons.search),
                filled: true,
                fillColor: AppColors.bgEditText,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8),
                  borderSide: BorderSide.none,
                ),
              ),
            ),
          ),
          const SizedBox(height: 10),
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _filteredItems.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.directions_car,
                                size: 64, color: AppColors.gray),
                            const SizedBox(height: 16),
                            Text(
                              'Your product List',
                              style: TextStyle(
                                color: AppColors.purple700,
                                fontSize: 13,
                              ),
                            ),
                            const SizedBox(height: 8),
                            Text(
                              'No items found',
                              style: TextStyle(color: AppColors.gray, fontSize: 14),
                            ),
                          ],
                        ),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.symmetric(horizontal: 10),
                        itemCount: _filteredItems.length,
                        itemBuilder: (context, index) {
                          final vehicle = _filteredItems[index];
                          return _VehicleListTile(
                            vehicle: vehicle,
                            onTap: () => _onItemSelected(vehicle),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}

class _CategoryItem {
  final String id;
  final String name;

  const _CategoryItem({required this.id, required this.name});
}

class _VehicleListTile extends StatelessWidget {
  final SellVehicle vehicle;
  final VoidCallback onTap;

  const _VehicleListTile({
    required this.vehicle,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final imageUrl = vehicle.image1;
    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      child: InkWell(
        onTap: onTap,
        borderRadius: BorderRadius.circular(8),
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: imageUrl != null && imageUrl.isNotEmpty
                    ? Image.network(
                        imageUrl,
                        width: 80,
                        height: 80,
                        fit: BoxFit.cover,
                        errorBuilder: (_, __, ___) => _buildPlaceholder(),
                      )
                    : _buildPlaceholder(),
              ),
              const SizedBox(width: 12),
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      vehicle.title ?? 'Vehicle',
                      style: AppTextStyles.textView15ssp().copyWith(
                          fontWeight: FontWeight.w600,
                          color: AppColors.purple700),
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      '${vehicle.vehicleTypeName ?? ''}, ${vehicle.vehicleModelName ?? ''}',
                      style: AppTextStyles.textView13ssp(),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    Text(
                      '₹${vehicle.price ?? '0'}',
                      style: AppTextStyles.textView13ssp().copyWith(
                          fontWeight: FontWeight.bold, color: AppColors.red),
                    ),
                  ],
                ),
              ),
              const Icon(Icons.chevron_right),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildPlaceholder() {
    return Container(
      width: 80,
      height: 80,
      color: AppColors.gray.withOpacity(0.3),
      child: const Icon(Icons.directions_car, size: 40),
    );
  }
}
