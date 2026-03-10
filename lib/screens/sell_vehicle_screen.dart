import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/fuels.dart';
import '../models/sell_vehicle_model.dart';
import '../services/api_service.dart';
import '../utils/helper.dart';
import 'sell_vehicle_popup_screen.dart';

class SellVehicleScreen extends StatefulWidget {
  final String categoryId;
  final String companyId;
  final String packageId;

  const SellVehicleScreen({
    super.key,
    required this.categoryId,
    this.companyId = '',
    this.packageId = '',
  });

  @override
  State<SellVehicleScreen> createState() => _SellVehicleScreenState();
}

class _SellVehicleScreenState extends State<SellVehicleScreen> {
  bool _isLoading = false;

  List<BrandsTypesModels> _brandsList = [];
  List<BrandType> _typeList = [];
  List<BrandModel> _modelList = [];
  List<Years> _yearList = [];
  List<Fuels> _fuelList = [];
  static const List<String> _ownersList = [
    'First Owner',
    'Second Owner',
    'Third Owner',
    'Fourth Owner',
  ];

  BrandsTypesModels? _selectedBrand;
  BrandType? _selectedType;
  BrandModel? _selectedModel;
  Years? _selectedYear;
  Fuels? _selectedFuel;
  int _selectedOwnerIndex = 0;
  int _transmission = 0;

  final _titleController = TextEditingController();
  final _descriptionController = TextEditingController();
  final _priceController = TextEditingController();
  final _kmController = TextEditingController();
  final _contactController = TextEditingController();

  final List<String> _imagePaths = [];
  final _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  @override
  void dispose() {
    _titleController.dispose();
    _descriptionController.dispose();
    _priceController.dispose();
    _kmController.dispose();
    _contactController.dispose();
    super.dispose();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);

    try {
      final brandsRes =
          await ApiService.getVehicleBrandsTypesModels(widget.categoryId);
      if (brandsRes.status) {
        try {
          _brandsList = brandsRes.getBrandsTypesModelsList();
        } catch (_) {
          _brandsList = _getFallbackBrands();
        }
      } else {
        _brandsList = _getFallbackBrands();
      }
      if (_brandsList.isEmpty) {
        _brandsList = _getFallbackBrands();
      }

      final yearsRes = await ApiService.getYearList();
      if (yearsRes.status) {
        _yearList = yearsRes.getYearsList();
      }
      if (_yearList.isEmpty) {
        _yearList = _getFallbackYears();
      }

      final fuelRes = await ApiService.getFuelList();
      if (fuelRes.status) {
        _fuelList = fuelRes.getFuelsList();
      }
      if (_fuelList.isEmpty) {
        _fuelList = _getFallbackFuels();
      }
    } catch (e) {
      _brandsList = _getFallbackBrands();
      _yearList = _getFallbackYears();
      _fuelList = _getFallbackFuels();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text(
              'Using offline options. Live data is currently unavailable.',
            ),
          ),
        );
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  List<BrandsTypesModels> _getFallbackBrands() {
    // Fallback is only used when the API returns no usable brand/type/model data.
    // Keep it category-aware so dropdowns still behave correctly offline.
    switch (widget.categoryId) {
      case '2': // Bike
        return [
          _brand('Hero', '1', typeLabel: 'Bike', models: ['Splendor', 'Passion', 'Xtreme']),
          _brand('Honda', '2', typeLabel: 'Bike', models: ['Shine', 'Unicorn', 'CBR']),
          _brand('Bajaj', '3', typeLabel: 'Bike', models: ['Pulsar', 'Platina', 'Avenger']),
          _brand('TVS', '4', typeLabel: 'Bike', models: ['Apache', 'Sport', 'Raider']),
        ];
      case '3': // Scooter
        return [
          _brand('Honda', '1', typeLabel: 'Scooter', models: ['Activa', 'Dio', 'Grazia']),
          _brand('TVS', '2', typeLabel: 'Scooter', models: ['Jupiter', 'Ntorq', 'Scooty Pep+']),
          _brand('Suzuki', '3', typeLabel: 'Scooter', models: ['Access 125', 'Burgman', 'Avenis']),
          _brand('Yamaha', '4', typeLabel: 'Scooter', models: ['RayZR', 'Fascino', 'Aerox 155']),
        ];
      case '1': // Car (default in current fallback popup)
      default:
        return [
          _brand('Maruti Suzuki', '1', typeLabel: 'Car', models: ['Alto 800', 'Swift', 'Dzire']),
          _brand('Hyundai', '2', typeLabel: 'Car', models: ['i20', 'Creta', 'Venue']),
          _brand('Tata', '3', typeLabel: 'Car', models: ['Nexon', 'Punch', 'Tiago']),
          _brand('Mahindra', '4', typeLabel: 'Car', models: ['Bolero', 'XUV300', 'Scorpio']),
        ];
    }
  }

  BrandsTypesModels _brand(
    String name,
    String id, {
    required String typeLabel,
    required List<String> models,
  }) {
    return BrandsTypesModels.fromJson({
      'vehicle_brand_name': name,
      'vehicle_brand_id': id,
      'type_list': [
        {
          'vehicle_type_name': typeLabel,
          'vehicle_type_id': id,
          'model_list': models
              .asMap()
              .entries
              .map((e) => {
                    'vehicle_model_name': e.value,
                    'vehicle_model_id': '${e.key + 1}',
                  })
              .toList(),
        },
      ],
    });
  }

  List<Years> _getFallbackYears() {
    final currentYear = DateTime.now().year;
    return List.generate(15, (i) {
      final y = (currentYear - i).toString();
      return Years.fromJson({'id': y, 'year': y});
    });
  }

  List<Fuels> _getFallbackFuels() {
    return [
      Fuels.fromJson({'id': '1', 'fuel': 'Petrol'}),
      Fuels.fromJson({'id': '2', 'fuel': 'Diesel'}),
      Fuels.fromJson({'id': '3', 'fuel': 'CNG'}),
      Fuels.fromJson({'id': '4', 'fuel': 'Electric'}),
    ];
  }

  Future<void> _openSelectionPopup({
    required String title,
    required List<String> items,
    required ValueChanged<String> onSelected,
    String? currentValue,
  }) async {
    if (items.isEmpty) return;
    final result = await SellVehiclePopupScreen.show(
      context,
      title: title,
      items: items,
      selectedValue: currentValue,
    );
    if (result != null && mounted) onSelected(result);
  }

  void _onBrandSelected(String value) {
    final matches = _brandsList.where((b) => (b.vehicleBrandName ?? '') == value);
    final brand = matches.isEmpty ? null : matches.first;
    setState(() {
      _selectedBrand = brand;
      _selectedType = null;
      _selectedModel = null;
      _typeList = brand?.typeList ?? [];
      _modelList = [];
    });
  }

  void _onTypeSelected(String value) {
    final matches = _typeList.where((t) => (t.vehicleTypeName ?? '') == value);
    final type = matches.isEmpty ? null : matches.first;
    setState(() {
      _selectedType = type;
      _selectedModel = null;
      _modelList = type?.modelList ?? [];
    });
  }

  Future<void> _pickImage() async {
    if (_imagePaths.length >= 7) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Maximum 7 images allowed')),
      );
      return;
    }
    final source = await showModalBottomSheet<ImageSource>(
      context: context,
      builder: (context) => SafeArea(
        child: Wrap(
          children: [
            ListTile(
              leading: const Icon(Icons.camera_alt),
              title: const Text('Camera'),
              onTap: () => Navigator.pop(context, ImageSource.camera),
            ),
            ListTile(
              leading: const Icon(Icons.photo_library),
              title: const Text('Gallery'),
              onTap: () => Navigator.pop(context, ImageSource.gallery),
            ),
          ],
        ),
      ),
    );
    if (source == null || !mounted) return;

    if (source == ImageSource.gallery) {
      final remaining = 7 - _imagePaths.length;
      final images = await _picker.pickMultiImage(
        maxWidth: 1200,
        imageQuality: 80,
      );
      if (images.isNotEmpty && mounted) {
        setState(() {
          for (final x in images.take(remaining)) {
            _imagePaths.add(x.path);
          }
        });
      }
    } else {
      final image = await _picker.pickImage(
        source: source,
        maxWidth: 1200,
        imageQuality: 80,
      );
      if (image != null && mounted) {
        setState(() => _imagePaths.add(image.path));
      }
    }
  }

  void _removeImage(int index) {
    setState(() => _imagePaths.removeAt(index));
  }

  Future<void> _submit() async {
    if (widget.companyId.trim().isEmpty || widget.packageId.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text(
              'Your company/package is not set. Please purchase/activate a package before adding vehicles.'),
        ),
      );
      return;
    }

    if (_titleController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Ad Title')),
      );
      return;
    }
    if (_descriptionController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Description')),
      );
      return;
    }
    if (_priceController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Price')),
      );
      return;
    }
    if (_kmController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Km Driven')),
      );
      return;
    }
    if (_contactController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Contact Number')),
      );
      return;
    }

    if (_selectedBrand == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Brand')),
      );
      return;
    }
    if (_selectedType == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Type')),
      );
      return;
    }
    if (_selectedModel == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Model')),
      );
      return;
    }
    if (_selectedYear == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Year')),
      );
      return;
    }
    if (_selectedFuel == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Fuel')),
      );
      return;
    }

    if (_imagePaths.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please add at least one image')),
      );
      return;
    }

    final loginData = Helper.getLoginData();
    final model = SellVehicleModel()
      ..userId = loginData.id.isNotEmpty ? loginData.id : '0'
      ..sellerCompanyId = widget.companyId
      ..userType = '1'
      ..vehicleCat = widget.categoryId
      ..vehicleBrand = _selectedBrand!.vehicleBrandId ?? ''
      ..vehicleType = _selectedType!.vehicleTypeId ?? ''
      ..vehicleModel = _selectedModel!.vehicleModelId ?? ''
      ..vehicleYear = _selectedYear!.id ?? ''
      ..vehicleFuel = _selectedFuel!.id ?? ''
      ..transmission = _transmission.toString()
      ..drivenKm = _kmController.text.trim()
      ..title = _titleController.text.trim()
      ..owners = _selectedOwnerIndex.toString()
      ..contactNumber = _contactController.text.trim()
      ..price = _priceController.text.trim()
      ..description = _descriptionController.text.trim()
      ..packagePurchasedId = widget.packageId
      ..imagePaths = List.from(_imagePaths);

    setState(() => _isLoading = true);

    final response = await ApiService.submitSellVehicle(model);

    setState(() => _isLoading = false);

    if (!mounted) return;
    if (response.status) {
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: const Text('Success'),
          content: const Text('Vehicle added successfully'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context);
                Navigator.pop(context, true);
              },
              child: const Text('OK'),
            ),
          ],
        ),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(response.message)),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.yellow,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        foregroundColor: AppColors.black,
        title: const Text(
          'Sell Vehicle',
          style: TextStyle(color: AppColors.black),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: _isLoading && _brandsList.isEmpty
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text('Include some details',
                      style: AppTextStyles.textView13ssp().copyWith(
                          color: AppColors.purple700,
                          fontWeight: FontWeight.w600)),
                  const SizedBox(height: 16),

                  _buildSelectionField(
                    'Select Brand',
                    _selectedBrand?.vehicleBrandName,
                    () => _openSelectionPopup(
                      title: 'Select Brand',
                      items: _brandsList
                          .map((b) => b.vehicleBrandName ?? '')
                          .where((s) => s.isNotEmpty)
                          .toList(),
                      currentValue: _selectedBrand?.vehicleBrandName,
                      onSelected: _onBrandSelected,
                    ),
                  ),

                  _buildSelectionField(
                    'Select Type',
                    _selectedType?.vehicleTypeName,
                    () => _openSelectionPopup(
                      title: 'Select Type',
                      items: _typeList
                          .map((t) => t.vehicleTypeName ?? '')
                          .where((s) => s.isNotEmpty)
                          .toList(),
                      currentValue: _selectedType?.vehicleTypeName,
                      onSelected: _onTypeSelected,
                    ),
                  ),

                  _buildSelectionField(
                    'Select Model',
                    _selectedModel?.vehicleModelName,
                    () => _openSelectionPopup(
                      title: 'Select Model',
                      items: _modelList
                          .map((m) => m.vehicleModelName ?? '')
                          .where((s) => s.isNotEmpty)
                          .toList(),
                      currentValue: _selectedModel?.vehicleModelName,
                      onSelected: (v) {
                        final m = _modelList
                            .where((x) => (x.vehicleModelName ?? '') == v)
                            .toList();
                        if (m.isNotEmpty) setState(() => _selectedModel = m.first);
                      },
                    ),
                  ),

                  _buildSelectionField(
                    'Select Year',
                    _selectedYear?.year,
                    () => _openSelectionPopup(
                      title: 'Select Year',
                      items: _yearList
                          .map((y) => y.year ?? '')
                          .where((s) => s.isNotEmpty)
                          .toList(),
                      currentValue: _selectedYear?.year,
                      onSelected: (v) {
                        final y = _yearList
                            .where((x) => (x.year ?? '') == v)
                            .toList();
                        if (y.isNotEmpty) setState(() => _selectedYear = y.first);
                      },
                    ),
                  ),

                  _buildSelectionField(
                    'Select Fuel',
                    _selectedFuel?.fuel,
                    () => _openSelectionPopup(
                      title: 'Select Fuel',
                      items: _fuelList
                          .map((f) => f.fuel ?? '')
                          .where((s) => s.isNotEmpty)
                          .toList(),
                      currentValue: _selectedFuel?.fuel,
                      onSelected: (v) {
                        final f = _fuelList
                            .where((x) => (x.fuel ?? '') == v)
                            .toList();
                        if (f.isNotEmpty) setState(() => _selectedFuel = f.first);
                      },
                    ),
                  ),

                  _buildSelectionField(
                    'Owner',
                    _ownersList[_selectedOwnerIndex],
                    () => _openSelectionPopup(
                      title: 'Select Owner',
                      items: _ownersList,
                      currentValue: _ownersList[_selectedOwnerIndex],
                      onSelected: (v) => setState(() =>
                          _selectedOwnerIndex = _ownersList.indexOf(v)),
                    ),
                  ),

                  const SizedBox(height: 8),
                  Text('Transmission', style: AppTextStyles.textView13ssp()),
                  Row(
                    children: [
                      Radio<int>(
                        value: 0,
                        groupValue: _transmission,
                        onChanged: (v) => setState(() => _transmission = 0),
                        activeColor: AppColors.purple700,
                      ),
                      const Text('Automatic'),
                      Radio<int>(
                        value: 1,
                        groupValue: _transmission,
                        onChanged: (v) => setState(() => _transmission = 1),
                        activeColor: AppColors.purple700,
                      ),
                      const Text('Manual'),
                    ],
                  ),

                  _buildTextField('Ad Title', _titleController),
                  _buildTextField('Description', _descriptionController,
                      maxLines: 4),
                  _buildTextField('Price (₹)', _priceController,
                      keyboardType: TextInputType.number),
                  _buildTextField('Km Driven', _kmController,
                      keyboardType: TextInputType.number),
                  _buildTextField('Contact Number', _contactController,
                      keyboardType: TextInputType.phone),

                  const SizedBox(height: 12),
                  Text('Photos (max 7)', style: AppTextStyles.textView13ssp()),
                  const SizedBox(height: 8),
                  SizedBox(
                    height: 100,
                    child: ListView(
                      scrollDirection: Axis.horizontal,
                      children: [
                        ..._imagePaths.asMap().entries.map((e) => Padding(
                              padding: const EdgeInsets.only(right: 8),
                              child: Stack(
                                children: [
                                  ClipRRect(
                                    borderRadius: BorderRadius.circular(8),
                                    child: Image.file(
                                      File(e.value),
                                      width: 80,
                                      height: 80,
                                      fit: BoxFit.cover,
                                    ),
                                  ),
                                  Positioned(
                                    top: 0,
                                    right: 0,
                                    child: GestureDetector(
                                      onTap: () => _removeImage(e.key),
                                      child: const Icon(Icons.close,
                                          color: Colors.red, size: 20),
                                    ),
                                  ),
                                ],
                              ),
                            )),
                        if (_imagePaths.length < 7)
                          GestureDetector(
                            onTap: _pickImage,
                            child: Container(
                              width: 80,
                              height: 80,
                              decoration: BoxDecoration(
                                color: AppColors.gray.withOpacity(0.3),
                                borderRadius: BorderRadius.circular(8),
                              ),
                              child: const Icon(Icons.add_a_photo, size: 32),
                            ),
                          ),
                      ],
                    ),
                  ),

                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton(
                      onPressed: _isLoading ? null : _submit,
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppColors.purple700,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
                      ),
                      child: _isLoading
                          ? const SizedBox(
                              height: 24,
                              width: 24,
                              child: CircularProgressIndicator(
                                  color: Colors.white, strokeWidth: 2),
                            )
                          : const Text('Submit'),
                    ),
                  ),
                ],
              ),
            ),
    );
  }

  Widget _buildSelectionField(
    String label,
    String? value,
    VoidCallback onTap,
  ) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: AppTextStyles.textView13ssp()),
          const SizedBox(height: 4),
          InkWell(
            onTap: onTap,
            child: Container(
              padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 14),
              decoration: BoxDecoration(
                color: AppColors.bgEditText,
                borderRadius: BorderRadius.circular(8),
              ),
              child: Row(
                children: [
                  Expanded(
                    child: Text(
                      value ?? 'Select $label',
                      style: AppTextStyles.textView13ssp().copyWith(
                        color: value != null
                            ? AppColors.black
                            : AppColors.gray,
                      ),
                    ),
                  ),
                  const Icon(Icons.arrow_drop_down, color: AppColors.black),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTextField(
    String label,
    TextEditingController controller, {
    int maxLines = 1,
    TextInputType? keyboardType,
  }) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: AppTextStyles.textView13ssp()),
          const SizedBox(height: 4),
          TextField(
            controller: controller,
            maxLines: maxLines,
            keyboardType: keyboardType,
            style: const TextStyle(color: AppColors.black),
            decoration: InputDecoration(
              filled: true,
              fillColor: AppColors.bgEditText,
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(8),
                borderSide: BorderSide.none,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
