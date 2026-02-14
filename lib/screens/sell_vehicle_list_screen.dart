import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import 'vehicle_selection_screen.dart';

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
  List<Map<String, dynamic>> _items = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadItems();
  }

  void _loadItems() {
    setState(() => _isLoading = true);
    // TODO: API / Firebase fetch sell vehicle list
    Future.delayed(const Duration(seconds: 1), () {
      if (mounted) {
        setState(() {
          _isLoading = false;
          _items = [];
        });
      }
    });
  }

  void _onAddProduct() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => VehicleSelectionScreen(
          isSellMode: true,
          companyId: widget.companyId ?? '',
          packageId: widget.packageId ?? '',
        ),
      ),
    );
  }

  void _onItemSelected(Map<String, dynamic> item) {
    // TODO: Convert to SellVehicle and navigate to SellVehicleDetailScreen when API returns data
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => Scaffold(
          appBar: AppBar(
            backgroundColor: AppColors.yellow,
            leading: IconButton(icon: const Icon(Icons.arrow_back), onPressed: () => Navigator.pop(context)),
            title: Text(item['title']?.toString() ?? 'Detail'),
          ),
          body: Center(child: Text(item['subtitle']?.toString() ?? 'Vehicle detail')),
        ),
      ),
    );
  }

  @override
  void dispose() {
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
          // Add Product button row
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
          // Search
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
                : _items.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.directions_car, size: 64, color: AppColors.gray),
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
                        itemCount: _items.length,
                        itemBuilder: (context, index) {
                          final item = _items[index];
                          return Card(
                            margin: const EdgeInsets.only(bottom: 10),
                            child: ListTile(
                              title: Text(item['title'] ?? 'Item ${index + 1}'),
                              subtitle: Text(item['subtitle'] ?? ''),
                              trailing: const Icon(Icons.arrow_forward_ios),
                              onTap: () => _onItemSelected(item),
                            ),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
