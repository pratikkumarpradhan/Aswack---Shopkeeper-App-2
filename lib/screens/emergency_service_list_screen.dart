import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import '../models/product_item.dart';
import '../models/product_list_request.dart';
import '../services/api_service.dart';
import 'login_screen.dart';
import 'emergency_service_detail_screen.dart';
import 'add_emergency_service_screen.dart';
import 'add_insurance_product_screen.dart';

class EmergencyServiceListScreen extends StatefulWidget {
  final String title;
  final String mainCatId; // "5" for Emergency, "3" Garage, "10" Breakdown
  final String? companyId;
  final String? packageId;

  const EmergencyServiceListScreen({
    super.key,
    this.title = Strings.emergency_services,
    this.mainCatId = '5',
    this.companyId,
    this.packageId,
  });

  @override
  State<EmergencyServiceListScreen> createState() =>
      _EmergencyServiceListScreenState();
}

class _EmergencyServiceListScreenState
    extends State<EmergencyServiceListScreen> {
  bool _isLoading = false;
  final TextEditingController _searchController = TextEditingController();
  List<ProductItem> _items = [];
  List<ProductItem> _filteredItems = [];

  @override
  void initState() {
    super.initState();
    _loadItems();
    _searchController.addListener(_onSearchChanged);
  }

  @override
  void dispose() {
    _searchController.removeListener(_onSearchChanged);
    _searchController.dispose();
    super.dispose();
  }

  void _onSearchChanged() {
    final query = _searchController.text.toLowerCase();
    setState(() {
      if (query.isEmpty) {
        _filteredItems = List.from(_items);
      } else {
        _filteredItems = _items.where((p) {
          final name = (p.productName ?? '').toLowerCase();
          final code = (p.productCode ?? '').toLowerCase();
          final serial = (p.serialNumber ?? '').toLowerCase();
          final brand = (p.vehicleCompanyName ?? '').toLowerCase();
          final model = (p.vehicleModelName ?? '').toLowerCase();
          return name.contains(query) ||
              code.contains(query) ||
              serial.contains(query) ||
              brand.contains(query) ||
              model.contains(query);
        }).toList();
      }
    });
  }

  Future<void> _ensureLoggedIn() async {
    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      if (!mounted) return;
      await Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
    }
  }

  Future<void> _loadItems() async {
    await _ensureLoggedIn();
    if (!mounted) return;

    setState(() => _isLoading = true);

    try {
      final loginData = Helper.getLoginData();
      final request = ProductListRequest(
        sellerId: loginData.id,
        companySellerId: widget.companyId ?? '',
        packagePurchasedId: widget.packageId ?? '',
        masterCategoryId: widget.mainCatId,
      );

      final response = await ApiService.getProductList(request);
      if (!mounted) return;

      if (response.status) {
        _items = response.getProductItemList();
        _filteredItems = List.from(_items);
      } else {
        _items = [];
        _filteredItems = [];
        if (response.message.isNotEmpty) {
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(response.message)),
          );
        }
      }
    } catch (e) {
      if (!mounted) return;
      _items = [];
      _filteredItems = [];
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString())),
      );
    } finally {
      if (mounted) {
        setState(() => _isLoading = false);
      }
    }
  }

  void _onItemSelected(ProductItem item) {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => EmergencyServiceDetailScreen(item: item),
      ),
    );
  }

  Future<void> _onAddProduct() async {
    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      if (!mounted) return;
      await Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
      return;
    }

    // Let user pick basic vehicle category (Car / Bike / Scooter)
    final vehicleCategoryId = await _showVehicleCategorySheet();
    if (vehicleCategoryId == null || !mounted) return;

    final bool? result;
    if (widget.mainCatId == '4') {
      // Vehicle Insurance – use insurance-specific add screen with full dropdowns
      result = await Navigator.push<bool>(
        context,
        MaterialPageRoute(
          builder: (_) => AddInsuranceProductScreen(
            mainCatId: widget.mainCatId,
            vehicleCategoryId: vehicleCategoryId,
            companyId: widget.companyId ?? '',
            packageId: widget.packageId ?? '',
          ),
        ),
      );
    } else {
      // Garage / Emergency / Breakdown – existing add screen
      result = await Navigator.push<bool>(
        context,
        MaterialPageRoute(
          builder: (_) => AddEmergencyServiceScreen(
            mainCatId: widget.mainCatId,
            vehicleCategoryId: vehicleCategoryId,
            companyId: widget.companyId ?? '',
            packageId: widget.packageId ?? '',
          ),
        ),
      );
    }

    if (result == true && mounted) {
      _loadItems();
    }
  }

  Future<String?> _showVehicleCategorySheet() {
    return showModalBottomSheet<String>(
      context: context,
      backgroundColor: AppColors.yellow,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (context) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
              child: Row(
                children: [
                  Text(
                    'Select vehicle type',
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
            ),
            ListTile(
              leading: const Icon(Icons.directions_car, color: AppColors.black),
              title: const Text('Car'),
              onTap: () => Navigator.pop(context, '1'),
            ),
            ListTile(
              leading: const Icon(Icons.two_wheeler, color: AppColors.black),
              title: const Text('Bike'),
              onTap: () => Navigator.pop(context, '2'),
            ),
            ListTile(
              leading:
                  const Icon(Icons.electric_scooter, color: AppColors.black),
              title: const Text('Scooter'),
              onTap: () => Navigator.pop(context, '3'),
            ),
            const SizedBox(height: 8),
          ],
        ),
      ),
    );
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
          style: const TextStyle(
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
              children: [
                Expanded(
                  child: Text(
                    Strings.your_product_list,
                    style: AppTextStyles.textView13ssp()
                        .copyWith(color: AppColors.purple700),
                  ),
                ),
                TextButton.icon(
                  onPressed: _onAddProduct,
                  icon: const Icon(Icons.add,
                      size: 18, color: AppColors.white),
                  label: const Text(
                    Strings.add_new_products,
                    style: TextStyle(
                      color: AppColors.white,
                      fontSize: 11,
                    ),
                  ),
                  style: TextButton.styleFrom(
                    backgroundColor: AppColors.purple700,
                    padding: const EdgeInsets.symmetric(
                      horizontal: 8,
                      vertical: 6,
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
                hintText: Strings.search_hint,
                hintStyle:
                    TextStyle(color: AppColors.gray, fontSize: 13),
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
                            Icon(Icons.miscellaneous_services,
                                size: 64, color: AppColors.gray),
                            const SizedBox(height: 16),
                            Text(
                              widget.title,
                              style: TextStyle(
                                color: AppColors.purple700,
                                fontSize: 13,
                              ),
                              textAlign: TextAlign.center,
                            ),
                            const SizedBox(height: 8),
                            Text(
                              'No items found',
                              style: TextStyle(
                                  color: AppColors.gray, fontSize: 14),
                            ),
                          ],
                        ),
                      )
                    : ListView.builder(
                        padding:
                            const EdgeInsets.symmetric(horizontal: 10),
                        itemCount: _filteredItems.length,
                        itemBuilder: (context, index) {
                          final item = _filteredItems[index];
                          return _EmergencyServiceTile(
                            item: item,
                            onTap: () => _onItemSelected(item),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}

class _EmergencyServiceTile extends StatelessWidget {
  final ProductItem item;
  final VoidCallback onTap;

  const _EmergencyServiceTile({
    required this.item,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    final imageUrl = ApiService.resolveImageUrl(item.image1);

    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      elevation: 3,
      child: InkWell(
        onTap: onTap,
        child: Row(
          children: [
            Container(
              width: 110,
              height: 90,
              decoration: BoxDecoration(
                color: AppColors.bgEditText,
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(4),
                  bottomLeft: Radius.circular(4),
                ),
              ),
              clipBehavior: Clip.antiAlias,
              child: imageUrl.isEmpty
                  ? const Icon(Icons.miscellaneous_services,
                      size: 40, color: AppColors.gray)
                  : Image.network(
                      imageUrl,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => const Icon(
                        Icons.miscellaneous_services,
                        size: 40,
                        color: AppColors.gray,
                      ),
                    ),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(10),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      item.productName ?? 'Service',
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                      style: const TextStyle(
                        color: AppColors.purple700,
                        fontSize: 13,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    const SizedBox(height: 4),
                    if (item.productCode != null &&
                        item.productCode!.isNotEmpty)
                      Text(
                        'Code: ${item.productCode}',
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          color: AppColors.textColor,
                          fontSize: 11,
                        ),
                      ),
                    const SizedBox(height: 4),
                    if (item.price != null && item.price!.isNotEmpty)
                      Text(
                        '₹${item.price}',
                        style: const TextStyle(
                          color: AppColors.red,
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                    const SizedBox(height: 4),
                    if (item.createdDatetime != null &&
                        item.createdDatetime!.isNotEmpty)
                      Text(
                        'Posted on: ${item.createdDatetime}',
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          color: AppColors.textColor,
                          fontSize: 11,
                        ),
                      ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

