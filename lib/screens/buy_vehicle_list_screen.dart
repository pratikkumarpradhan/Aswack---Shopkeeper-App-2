import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/buy_vehicle.dart';
import '../models/sell_vehicle.dart';
import '../services/api_service.dart';
import 'buy_vehicle_detail_screen.dart';

class BuyVehicleListScreen extends StatefulWidget {
  final BuyVehicle buyVehicle;

  const BuyVehicleListScreen({Key? key, required this.buyVehicle})
      : super(key: key);

  @override
  State<BuyVehicleListScreen> createState() => _BuyVehicleListScreenState();
}

class _BuyVehicleListScreenState extends State<BuyVehicleListScreen> {
  bool _isLoading = true;
  String? _errorMessage;
  List<SellVehicle> _vehicleList = [];
  List<SellVehicle> _filteredList = [];
  final TextEditingController _searchController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _loadVehicles();
    _searchController.addListener(_onSearchChanged);
  }

  @override
  void dispose() {
    _searchController.removeListener(_onSearchChanged);
    _searchController.dispose();
    super.dispose();
  }

  void _onSearchChanged() {
    setState(() {
      final query = _searchController.text.toLowerCase();
      if (query.isEmpty) {
        _filteredList = List.from(_vehicleList);
      } else {
        _filteredList = _vehicleList.where((v) {
          final brand = (v.vehicleBrandName ?? '').toLowerCase();
          final model = (v.vehicleModelName ?? '').toLowerCase();
          final type = (v.vehicleTypeName ?? '').toLowerCase();
          final fuel = (v.vehicleFuelName ?? '').toLowerCase();
          final year = (v.vehicleYearName ?? '').toLowerCase();
          return brand.contains(query) ||
              model.contains(query) ||
              type.contains(query) ||
              fuel.contains(query) ||
              year.contains(query);
        }).toList();
      }
    });
  }

  Future<void> _loadVehicles() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    final response = await ApiService.getBuyVehicles(widget.buyVehicle);

    setState(() {
      _isLoading = false;
      if (response.status) {
        _vehicleList = response.getSellVehicleList();
        _filteredList = List.from(_vehicleList);
      } else {
        _errorMessage = response.message;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        foregroundColor: AppColors.black,
        title: const Text(
          'Search Result',
          style: TextStyle(color: AppColors.black),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Column(
        children: [
          // Search bar
          Padding(
            padding: const EdgeInsets.all(12),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Search by brand, model, type...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
            ),
          ),
          // Result count
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: Align(
              alignment: Alignment.centerLeft,
              child: Text(
                '${_filteredList.length} vehicles found',
                style: AppTextStyles.textView13ssp(),
              ),
            ),
          ),
          const SizedBox(height: 8),
          // List
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _errorMessage != null
                    ? Center(
                        child: Padding(
                          padding: const EdgeInsets.all(24),
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.center,
                            children: [
                              Text(
                                _errorMessage!,
                                textAlign: TextAlign.center,
                                style: const TextStyle(color: Colors.red),
                              ),
                              const SizedBox(height: 16),
                              ElevatedButton(
                                onPressed: _loadVehicles,
                                child: const Text('Retry'),
                              ),
                            ],
                          ),
                        ),
                      )
                    : _filteredList.isEmpty
                        ? const Center(
                            child: Text('No vehicles found'),
                          )
                        : ListView.builder(
                            padding: const EdgeInsets.symmetric(horizontal: 12),
                            itemCount: _filteredList.length,
                            itemBuilder: (context, index) {
                              final vehicle = _filteredList[index];
                              return _VehicleListTile(
                                vehicle: vehicle,
                                onTap: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                      builder: (context) =>
                                          BuyVehicleDetailScreen(
                                        vehicle: vehicle,
                                      ),
                                    ),
                                  );
                                },
                              );
                            },
                          ),
          ),
        ],
      ),
    );
  }
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
      margin: const EdgeInsets.only(bottom: 12),
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
                    const SizedBox(height: 4),
                    Text(
                      '${vehicle.vehicleYearName ?? ''} - ${vehicle.drivenKm ?? ''} km',
                      style: AppTextStyles.textView13ssp().copyWith(
                          color: AppColors.textColor, fontSize: 11),
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
