import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/buy_vehicle.dart';
import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/fuels.dart';
import '../services/api_service.dart';
import '../utils/helper.dart';
import 'buy_vehicle_list_screen.dart';

class BuyVehicleScreen extends StatefulWidget {
  final String categoryId;

  const BuyVehicleScreen({Key? key, required this.categoryId}) : super(key: key);

  @override
  State<BuyVehicleScreen> createState() => _BuyVehicleScreenState();
}

class _BuyVehicleScreenState extends State<BuyVehicleScreen> {
  bool _isLoading = false;
  String? _errorMessage;
  LoginData _loginData = LoginData();

  List<BrandsTypesModels> _brandsList = [];
  List<BrandType> _typeList = [];
  List<BrandModel> _modelList = [];
  List<Years> _yearList = [];
  List<Fuels> _fuelList = [];

  int _transmission = -1; // -1=none, 0=automatic, 1=manual

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    _loginData = Helper.getLoginData();
    final brandsResponse =
        await ApiService.getVehicleBrandsTypesModels(widget.categoryId);

    if (!brandsResponse.status) {
      setState(() {
        _isLoading = false;
        _errorMessage = brandsResponse.message;
      });
      return;
    }

    _brandsList = brandsResponse.getBrandsTypesModelsList();

    final yearsResponse = await ApiService.getYearList();
    if (yearsResponse.status) {
      _yearList = yearsResponse.getYearsList();
    }

    final fuelResponse = await ApiService.getFuelList();
    if (fuelResponse.status) {
      _fuelList = fuelResponse.getFuelsList();
    }

    setState(() {
      _updateTypesFromBrands();
      _updateModelsFromTypes();
      _isLoading = false;
    });
  }

  void _updateTypesFromBrands() {
    _typeList = [];
    for (final brand in _brandsList) {
      if (brand.isChecked) {
        _typeList.addAll(brand.typeList);
      }
    }
  }

  void _updateModelsFromTypes() {
    _modelList = [];
    for (final type in _typeList) {
      if (type.isChecked) {
        _modelList.addAll(type.modelList);
      }
    }
  }

  BuyVehicle _buildBuyVehicle() {
    final loginData = _loginData;
    final buyVehicle = BuyVehicle()
      ..userId = loginData.id.isNotEmpty ? loginData.id : null
      ..category = widget.categoryId
      ..userType = '1'; // SELLER

    if (_transmission == 0) {
      buyVehicle.transmission = '0'; // AUTOMATIC
    } else if (_transmission == 1) {
      buyVehicle.transmission = '1'; // MANUAL
    }

    for (final brand in _brandsList) {
      if (brand.isChecked && brand.vehicleBrandId != null) {
        buyVehicle.vehicleBrand.add(brand.vehicleBrandId!);
      }
    }

    for (final type in _typeList) {
      if (type.isChecked && type.vehicleTypeId != null) {
        buyVehicle.vehicleType.add(type.vehicleTypeId!);
      }
    }

    for (final model in _modelList) {
      if (model.isChecked && model.vehicleModelId != null) {
        buyVehicle.vehicleModel.add(model.vehicleModelId!);
      }
    }

    for (final year in _yearList) {
      if (year.isChecked && year.id != null) {
        buyVehicle.vehicleYear.add(year.id!);
      }
    }

    for (final fuel in _fuelList) {
      if (fuel.isChecked && fuel.id != null) {
        buyVehicle.vehicleFuel.add(fuel.id!);
      }
    }

    return buyVehicle;
  }

  void _onSearch() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => BuyVehicleListScreen(
          buyVehicle: _buildBuyVehicle(),
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        foregroundColor: AppColors.black,
        title: const Text('Search Vehicles', style: TextStyle(color: AppColors.black)),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: _isLoading
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
                          onPressed: _loadData,
                          child: const Text('Retry'),
                        ),
                      ],
                    ),
                  ),
                )
              : SingleChildScrollView(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Include some details',
                        style: AppTextStyles.textView13ssp().copyWith(
                            color: AppColors.purple700,
                            fontWeight: FontWeight.w600),
                      ),
                      const SizedBox(height: 16),

                      // Brands
                      _buildSection(
                        'Select Brand',
                        Wrap(
                          spacing: 8,
                          runSpacing: 8,
                          children: _brandsList
                              .map((b) => _buildChip(
                                    label: b.vehicleBrandName ?? '',
                                    selected: b.isChecked,
                                    onTap: () {
                                      setState(() {
                                        b.isChecked = !b.isChecked;
                                        _updateTypesFromBrands();
                                        _updateModelsFromTypes();
                                      });
                                    },
                                  ))
                              .toList(),
                        ),
                      ),

                      // Types
                      _buildSection(
                        'Select Type',
                        Wrap(
                          spacing: 8,
                          runSpacing: 8,
                          children: _typeList
                              .map((t) => _buildChip(
                                    label: t.vehicleTypeName ?? '',
                                    selected: t.isChecked,
                                    onTap: () {
                                      setState(() {
                                        t.isChecked = !t.isChecked;
                                        _updateModelsFromTypes();
                                      });
                                    },
                                  ))
                              .toList(),
                        ),
                      ),

                      // Models
                      _buildSection(
                        'Select Model',
                        Wrap(
                          spacing: 8,
                          runSpacing: 8,
                          children: _modelList
                              .map((m) => _buildChip(
                                    label: m.vehicleModelName ?? '',
                                    selected: m.isChecked,
                                    onTap: () {
                                      setState(() {
                                        m.isChecked = !m.isChecked;
                                      });
                                    },
                                  ))
                              .toList(),
                        ),
                      ),

                      // Year
                      _buildSection(
                        'Select Year',
                        Wrap(
                          spacing: 8,
                          runSpacing: 8,
                          children: _yearList
                              .map((y) => _buildChip(
                                    label: y.year ?? '',
                                    selected: y.isChecked,
                                    onTap: () {
                                      setState(() {
                                        y.isChecked = !y.isChecked;
                                      });
                                    },
                                  ))
                              .toList(),
                        ),
                      ),

                      // Fuel
                      _buildSection(
                        'Select Fuel',
                        Wrap(
                          spacing: 8,
                          runSpacing: 8,
                          children: _fuelList
                              .map((f) => _buildChip(
                                    label: f.fuel ?? '',
                                    selected: f.isChecked,
                                    onTap: () {
                                      setState(() {
                                        f.isChecked = !f.isChecked;
                                      });
                                    },
                                  ))
                              .toList(),
                        ),
                      ),

                      // Transmission
                      Text(
                        'Transmission',
                        style: AppTextStyles.textView13ssp(),
                      ),
                      const SizedBox(height: 8),
                      Row(
                        children: [
                          Radio<int>(
                            value: -1,
                            groupValue: _transmission,
                            onChanged: (v) => setState(() => _transmission = -1),
                            activeColor: AppColors.purple700,
                          ),
                          const Text('None'),
                          const SizedBox(width: 8),
                          Radio<int>(
                            value: 0,
                            groupValue: _transmission,
                            onChanged: (v) => setState(() => _transmission = 0),
                            activeColor: AppColors.purple700,
                          ),
                          const Text('Automatic'),
                          const SizedBox(width: 8),
                          Radio<int>(
                            value: 1,
                            groupValue: _transmission,
                            onChanged: (v) => setState(() => _transmission = 1),
                            activeColor: AppColors.purple700,
                          ),
                          const Text('Manual'),
                        ],
                      ),

                      const SizedBox(height: 32),
                      SizedBox(
                        width: double.infinity,
                        child: ElevatedButton(
                          onPressed: _onSearch,
                          style: ElevatedButton.styleFrom(
                            backgroundColor: AppColors.purple700,
                            foregroundColor: Colors.white,
                            padding: const EdgeInsets.symmetric(vertical: 14),
                          ),
                          child: const Text('Search'),
                        ),
                      ),
                    ],
                  ),
                ),
    );
  }

  Widget _buildSection(String title, Widget child) {
    return Container(
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.bgEditText,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(title, style: AppTextStyles.textView13ssp()),
          const SizedBox(height: 8),
          child,
        ],
      ),
    );
  }

  Widget _buildChip({
    required String label,
    required bool selected,
    required VoidCallback onTap,
  }) {
    return FilterChip(
      label: Text(label),
      selected: selected,
      onSelected: (_) => onTap(),
      selectedColor: AppColors.yellow,
    );
  }
}
