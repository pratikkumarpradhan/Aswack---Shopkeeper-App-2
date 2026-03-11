import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/insurance_product_request.dart';
import '../services/api_service.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import 'login_screen.dart';

class AddInsuranceProductScreen extends StatefulWidget {
  final String mainCatId; // "4" insurance, "8" heavy, "11" rent car
  final String vehicleCategoryId;
  final String? companyId;
  final String? packageId;

  const AddInsuranceProductScreen({
    super.key,
    required this.mainCatId,
    required this.vehicleCategoryId,
    this.companyId,
    this.packageId,
  });

  @override
  State<AddInsuranceProductScreen> createState() =>
      _AddInsuranceProductScreenState();
}

class _AddInsuranceProductScreenState extends State<AddInsuranceProductScreen> {
  final _productNameCtrl = TextEditingController();
  final _serialCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();
  final _priceCtrl = TextEditingController();

  bool _isLoading = false;

  List<BrandsTypesModels> _brands = [];
  List<BrandType> _types = [];
  List<BrandModel> _models = [];
  List<Years> _years = [];

  BrandsTypesModels? _selectedBrand;
  BrandType? _selectedType;
  BrandModel? _selectedModel;
  Years? _selectedYear;

  String _productCondition = 'New';

  final List<File> _images = [];
  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _ensureLoggedInAndLoadData();
  }

  @override
  void dispose() {
    _productNameCtrl.dispose();
    _serialCtrl.dispose();
    _descriptionCtrl.dispose();
    _priceCtrl.dispose();
    super.dispose();
  }

  Future<void> _ensureLoggedInAndLoadData() async {
    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      if (!mounted) return;
      await Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
      return;
    }
    await _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    try {
      // Brands / types / models
      final brandsRes =
          await ApiService.getVehicleBrandsTypesModels(widget.vehicleCategoryId);
      if (!mounted) return;
      if (brandsRes.status) {
        try {
          _brands = brandsRes.getBrandsTypesModelsList();
        } catch (_) {
          _brands = _getFallbackBrands();
        }
      } else {
        _brands = _getFallbackBrands();
      }
      if (_brands.isEmpty) _brands = _getFallbackBrands();
      if (_brands.isNotEmpty && _selectedBrand == null) {
        _selectedBrand = _brands.first;
        _types = _selectedBrand!.typeList;
        if (_types.isNotEmpty) {
          _selectedType = _types.first;
          _models = _selectedType!.modelList;
          if (_models.isNotEmpty) {
            _selectedModel = _models.first;
          }
        }
      }

      // Years
      final yearsRes = await ApiService.getYearList();
      if (yearsRes.status) {
        _years = yearsRes.getYearsList();
      }
      if (_years.isEmpty) {
        _years = _getFallbackYears();
      }
      if (_years.isNotEmpty && _selectedYear == null) {
        _selectedYear = _years.first;
      }
    } catch (_) {
      _brands = _getFallbackBrands();
      _years = _getFallbackYears();
      if (_brands.isNotEmpty && _selectedBrand == null) {
        _selectedBrand = _brands.first;
        _types = _selectedBrand!.typeList;
        if (_types.isNotEmpty) {
          _selectedType = _types.first;
          _models = _selectedType!.modelList;
          if (_models.isNotEmpty) _selectedModel = _models.first;
        }
      }
      if (_years.isNotEmpty && _selectedYear == null) {
        _selectedYear = _years.first;
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  List<BrandsTypesModels> _getFallbackBrands() {
    switch (widget.vehicleCategoryId) {
      case '2': // Bike
        return [
          _brand('Hero', '1', typeLabel: 'Bike', models: ['Splendor', 'Passion']),
          _brand('Honda', '2', typeLabel: 'Bike', models: ['Shine', 'Unicorn']),
          _brand('Bajaj', '3', typeLabel: 'Bike', models: ['Pulsar', 'Platina']),
        ];
      case '3': // Scooter
        return [
          _brand('Honda', '1', typeLabel: 'Scooter', models: ['Activa', 'Dio']),
          _brand('TVS', '2', typeLabel: 'Scooter', models: ['Jupiter', 'Ntorq']),
        ];
      case '1': // Car / default
      default:
        return [
          _brand('Maruti Suzuki', '1', typeLabel: 'Car', models: ['Alto 800', 'Swift']),
          _brand('Hyundai', '2', typeLabel: 'Car', models: ['i20', 'Creta']),
          _brand('Tata', '3', typeLabel: 'Car', models: ['Nexon', 'Tiago']),
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

  Future<void> _pickImage(ImageSource source) async {
    if (_images.length >= 6) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Maximum 6 images allowed')),
      );
      return;
    }
    final picked = await _picker.pickImage(source: source, imageQuality: 80);
    if (picked == null) return;
    setState(() {
      _images.add(File(picked.path));
    });
  }

  Future<void> _showImageSourceSheet() async {
    showModalBottomSheet(
      context: context,
      backgroundColor: AppColors.yellow,
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(top: Radius.circular(16)),
      ),
      builder: (_) => SafeArea(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              leading: const Icon(Icons.photo_camera, color: AppColors.black),
              title: const Text('Camera'),
              onTap: () {
                Navigator.pop(context);
                _pickImage(ImageSource.camera);
              },
            ),
            ListTile(
              leading:
                  const Icon(Icons.photo_library, color: AppColors.black),
              title: const Text('Gallery'),
              onTap: () {
                Navigator.pop(context);
                _pickImage(ImageSource.gallery);
              },
            ),
            const SizedBox(height: 8),
          ],
        ),
      ),
    );
  }

  bool _validate() {
    if (_productNameCtrl.text.trim().isEmpty ||
        _descriptionCtrl.text.trim().isEmpty ||
        _priceCtrl.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text(Strings.fill_all_details)),
      );
      return false;
    }
    if (_selectedBrand == null ||
        _selectedType == null ||
        _selectedModel == null ||
        _selectedYear == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
            content: Text('Please select Brand, Type, Model and Year')),
      );
      return false;
    }
    return true;
  }

  Future<void> _submit() async {
    if (!_validate()) return;

    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      if (!mounted) return;
      await Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
      return;
    }

    setState(() => _isLoading = true);

    try {
      final req = InsuranceProductRequest(
        sellerId: loginData.id,
        sellerCompanyId: widget.companyId ?? '',
        packagePurchasedId: widget.packageId ?? '',
        masterCategoryId: widget.mainCatId,
        vehicleCategoryId: widget.vehicleCategoryId,
        vehicleCompanyId: _selectedBrand?.vehicleBrandId ?? '',
        vehicleModelTypeId: _selectedType?.vehicleTypeId ?? '',
        vehicleModelId: _selectedModel?.vehicleModelId ?? '',
        vehicleYearId: _selectedYear?.id ?? '',
        productName: _productNameCtrl.text.trim(),
        serialNumber: _serialCtrl.text.trim(),
        price: _priceCtrl.text.trim(),
        description: _descriptionCtrl.text.trim(),
        productCondition: _productCondition,
      );

      final response =
          await ApiService.insertInsuranceProduct(req, _images.take(6).toList());

      if (!mounted) return;
      if (response.status) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Product added successfully')),
        );
        Navigator.pop(context, true);
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(response.message)),
        );
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(e.toString())),
      );
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    final title = () {
      switch (widget.mainCatId) {
        case '4':
          return 'Add Insurance Details';
        case '6':
          return 'Add Spare Parts Details';
        case '7':
          return 'Add Car Accessories Details';
        case '8':
        case '11':
          return 'Add Vehicle Details';
        default:
          return 'Add Product Details';
      }
    }();

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
          title,
          style: const TextStyle(
            color: AppColors.black,
            fontSize: 13,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      body: Stack(
        children: [
          SingleChildScrollView(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Include some details',
                  style: AppTextStyles.textView13ssp().copyWith(
                    fontWeight: FontWeight.w600,
                    color: AppColors.purple700,
                  ),
                ),
                const SizedBox(height: 16),
                _buildDropdownRow(
                  label: 'Brand',
                  value: _selectedBrand?.vehicleBrandName,
                  options: _brands.map((b) => b.vehicleBrandName ?? '').toList(),
                  onChanged: (v) {
                    final match = _brands.firstWhere(
                      (b) => b.vehicleBrandName == v,
                      orElse: () => _brands.first,
                    );
                    setState(() {
                      _selectedBrand = match;
                      _types = match.typeList;
                      _selectedType = _types.isNotEmpty ? _types.first : null;
                      _models =
                          _selectedType != null ? _selectedType!.modelList : [];
                      _selectedModel =
                          _models.isNotEmpty ? _models.first : null;
                    });
                  },
                ),
                _buildDropdownRow(
                  label: 'Type',
                  value: _selectedType?.vehicleTypeName,
                  options:
                      _types.map((t) => t.vehicleTypeName ?? '').toList(),
                  onChanged: (v) {
                    final match = _types.firstWhere(
                      (t) => t.vehicleTypeName == v,
                      orElse: () => _types.first,
                    );
                    setState(() {
                      _selectedType = match;
                      _models = match.modelList;
                      _selectedModel =
                          _models.isNotEmpty ? _models.first : null;
                    });
                  },
                ),
                _buildDropdownRow(
                  label: 'Model',
                  value: _selectedModel?.vehicleModelName,
                  options:
                      _models.map((m) => m.vehicleModelName ?? '').toList(),
                  onChanged: (v) {
                    final match = _models.firstWhere(
                      (m) => m.vehicleModelName == v,
                      orElse: () => _models.first,
                    );
                    setState(() => _selectedModel = match);
                  },
                ),
                _buildDropdownRow(
                  label: 'Year',
                  value: _selectedYear?.year,
                  options: _years.map((y) => y.year ?? '').toList(),
                  onChanged: (v) {
                    final match = _years.firstWhere(
                      (y) => y.year == v,
                      orElse: () => _years.first,
                    );
                    setState(() => _selectedYear = match);
                  },
                ),
                _buildDropdownRow(
                  label: 'Condition',
                  value: _productCondition,
                  options: const ['New', 'Used'],
                  onChanged: (v) {
                    setState(() => _productCondition = v ?? 'New');
                  },
                ),
                const SizedBox(height: 12),
                _buildTextField('Product name', _productNameCtrl),
                _buildTextField('Serial number', _serialCtrl),
                Text(
                  Strings.description,
                  style: AppTextStyles.textView13ssp(),
                ),
                const SizedBox(height: 4),
                TextField(
                  controller: _descriptionCtrl,
                  maxLines: 4,
                  maxLength: 200,
                  decoration: InputDecoration(
                    counterText: '',
                    isDense: true,
                    filled: true,
                    fillColor: AppColors.bgEditText,
                    border: OutlineInputBorder(
                      borderRadius: BorderRadius.circular(6),
                      borderSide: BorderSide.none,
                    ),
                  ),
                ),
                _buildTextField(Strings.price, _priceCtrl,
                    keyboardType: TextInputType.number),
                const SizedBox(height: 20),
                GestureDetector(
                  onTap: _showImageSourceSheet,
                  child: Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(
                        vertical: 12, horizontal: 16),
                    decoration: BoxDecoration(
                      color: AppColors.purple700,
                      borderRadius: BorderRadius.circular(6),
                    ),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: const [
                        Icon(Icons.file_upload, color: AppColors.white),
                        SizedBox(width: 8),
                        Text(
                          'Upload photos',
                          style: TextStyle(
                            color: AppColors.white,
                            fontSize: 13,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 12),
                if (_images.isNotEmpty)
                  GridView.builder(
                    shrinkWrap: true,
                    physics: const NeverScrollableScrollPhysics(),
                    gridDelegate:
                        const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 3,
                      crossAxisSpacing: 8,
                      mainAxisSpacing: 8,
                    ),
                    itemCount: _images.length,
                    itemBuilder: (_, index) {
                      final file = _images[index];
                      return Stack(
                        fit: StackFit.expand,
                        children: [
                          ClipRRect(
                            borderRadius: BorderRadius.circular(6),
                            child: Image.file(
                              file,
                              fit: BoxFit.cover,
                            ),
                          ),
                          Positioned(
                            top: 2,
                            right: 2,
                            child: GestureDetector(
                              onTap: () {
                                setState(() {
                                  _images.removeAt(index);
                                });
                              },
                              child: Container(
                                padding: const EdgeInsets.all(2),
                                decoration: const BoxDecoration(
                                  color: Colors.black54,
                                  shape: BoxShape.circle,
                                ),
                                child: const Icon(
                                  Icons.close,
                                  size: 14,
                                  color: Colors.white,
                                ),
                              ),
                            ),
                          ),
                        ],
                      );
                    },
                  ),
                const SizedBox(height: 24),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton(
                    onPressed: _isLoading ? null : _submit,
                    style: ElevatedButton.styleFrom(
                      backgroundColor: AppColors.purple700,
                      foregroundColor: Colors.white,
                      padding: const EdgeInsets.symmetric(vertical: 12),
                    ),
                    child: Text(
                      'Review & Submit',
                      style: AppTextStyles.textView15ssp().copyWith(
                        color: Colors.white,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 40),
              ],
            ),
          ),
          if (_isLoading)
            Container(
              color: Colors.black.withValues(alpha: 0.2),
              child: const Center(
                child: CircularProgressIndicator(),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildDropdownRow({
    required String label,
    required String? value,
    required List<String> options,
    required ValueChanged<String?> onChanged,
  }) {
    final hasOptions = options.isNotEmpty;
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: AppTextStyles.textView13ssp()),
          const SizedBox(height: 4),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 10),
            decoration: BoxDecoration(
              color: AppColors.bgEditText,
              borderRadius: BorderRadius.circular(6),
            ),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                value: hasOptions && value != null && options.contains(value)
                    ? value
                    : null,
                isExpanded: true,
                icon: const Icon(Icons.arrow_drop_down),
                hint: Text(
                  hasOptions ? 'Select $label' : 'No options',
                  style: AppTextStyles.textView13ssp(),
                ),
                items: hasOptions
                    ? options
                        .map(
                          (o) => DropdownMenuItem(
                            value: o,
                            child: Text(
                              o,
                              style: const TextStyle(
                                color: AppColors.black,
                                fontSize: 13,
                              ),
                            ),
                          ),
                        )
                        .toList()
                    : null,
                onChanged: hasOptions ? onChanged : null,
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
            keyboardType: keyboardType,
            decoration: const InputDecoration(
              isDense: true,
              border: UnderlineInputBorder(),
            ),
          ),
        ],
      ),
    );
  }
}

