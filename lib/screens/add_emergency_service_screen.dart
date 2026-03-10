import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';

import '../models/brand_type_model.dart';
import '../models/emergency_product_request.dart';
import '../services/api_service.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import 'login_screen.dart';

class AddEmergencyServiceScreen extends StatefulWidget {
  final String mainCatId; // "5" Emergency, "3" Garage, "10" Breakdown
  final String vehicleCategoryId;
  final String? companyId;
  final String? packageId;

  const AddEmergencyServiceScreen({
    super.key,
    required this.mainCatId,
    required this.vehicleCategoryId,
    this.companyId,
    this.packageId,
  });

  @override
  State<AddEmergencyServiceScreen> createState() =>
      _AddEmergencyServiceScreenState();
}

class _AddEmergencyServiceScreenState
    extends State<AddEmergencyServiceScreen> {
  final _productNameCtrl = TextEditingController();
  final _specializeInCtrl = TextEditingController();
  final _descriptionCtrl = TextEditingController();
  final _priceCtrl = TextEditingController();

  bool _isLoading = false;
  List<BrandTypeModel> _brands = [];
  BrandTypeModel? _selectedBrand;

  final List<File> _images = [];
  final ImagePicker _picker = ImagePicker();

  @override
  void initState() {
    super.initState();
    _ensureLoggedInAndLoadBrands();
  }

  @override
  void dispose() {
    _productNameCtrl.dispose();
    _specializeInCtrl.dispose();
    _descriptionCtrl.dispose();
    _priceCtrl.dispose();
    super.dispose();
  }

  Future<void> _ensureLoggedInAndLoadBrands() async {
    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      if (!mounted) return;
      await Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
        (route) => false,
      );
      return;
    }
    await _loadBrands();
  }

  Future<void> _loadBrands() async {
    setState(() => _isLoading = true);
    try {
      final response =
          await ApiService.getVehicleBrandsTypesModels(widget.vehicleCategoryId);
      if (!mounted) return;

      if (response.status) {
        try {
          _brands = response.getBrandTypeModelList();
        } catch (_) {
          _brands = _getFallbackBrands();
        }
      } else {
        _brands = _getFallbackBrands();
      }

      if (_brands.isEmpty) {
        _brands = _getFallbackBrands();
      }

      if (_brands.isNotEmpty && _selectedBrand == null) {
        _selectedBrand = _brands.first;
      }
    } catch (_) {
      if (!mounted) return;
      _brands = _getFallbackBrands();
      if (_brands.isNotEmpty && _selectedBrand == null) {
        _selectedBrand = _brands.first;
      }
    } finally {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  List<BrandTypeModel> _getFallbackBrands() {
    // Category-aware simple brand list so dropdown is always usable,
    // even when live brand data is unavailable.
    switch (widget.vehicleCategoryId) {
      case '2': // Bike
        return [
          BrandTypeModel(vehicleBrandId: '1', vehicleBrandName: 'Hero'),
          BrandTypeModel(vehicleBrandId: '2', vehicleBrandName: 'Honda'),
          BrandTypeModel(vehicleBrandId: '3', vehicleBrandName: 'Bajaj'),
          BrandTypeModel(vehicleBrandId: '4', vehicleBrandName: 'TVS'),
        ];
      case '3': // Scooter
        return [
          BrandTypeModel(vehicleBrandId: '1', vehicleBrandName: 'Honda'),
          BrandTypeModel(vehicleBrandId: '2', vehicleBrandName: 'TVS'),
          BrandTypeModel(vehicleBrandId: '3', vehicleBrandName: 'Suzuki'),
          BrandTypeModel(vehicleBrandId: '4', vehicleBrandName: 'Yamaha'),
        ];
      case '1': // Car / default
      default:
        return [
          BrandTypeModel(vehicleBrandId: '1', vehicleBrandName: 'Maruti Suzuki'),
          BrandTypeModel(vehicleBrandId: '2', vehicleBrandName: 'Hyundai'),
          BrandTypeModel(vehicleBrandId: '3', vehicleBrandName: 'Tata'),
          BrandTypeModel(vehicleBrandId: '4', vehicleBrandName: 'Mahindra'),
        ];
    }
  }

  Future<void> _pickImage(ImageSource source) async {
    final picked = await _picker.pickImage(source: source, imageQuality: 80);
    if (picked == null) return;
    setState(() {
      if (_images.length < 6) {
        _images.add(File(picked.path));
      }
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
              leading: const Icon(Icons.photo_library, color: AppColors.black),
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
        _specializeInCtrl.text.trim().isEmpty ||
        _descriptionCtrl.text.trim().isEmpty ||
        _priceCtrl.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text(Strings.fill_all_details)),
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
      final req = EmergencyProductRequest(
        sellerId: loginData.id,
        sellerCompanyId: widget.companyId ?? '',
        packagePurchasedId: widget.packageId ?? '',
        masterCategoryId: widget.mainCatId,
        vehicleCategoryId: widget.vehicleCategoryId,
        vehicleCompanyId: _selectedBrand?.vehicleBrandId ?? '',
        productName: _productNameCtrl.text.trim(),
        specializeIn: _specializeInCtrl.text.trim(),
        description: _descriptionCtrl.text.trim(),
        price: _priceCtrl.text.trim(),
      );

      final response =
          await ApiService.insertEmergencyProduct(req, _images.take(6).toList());

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
          'Add Garage / Emergency Detail',
          style: TextStyle(
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
                Text(
                  'Select company name',
                  style: AppTextStyles.textView13ssp(),
                ),
                const SizedBox(height: 6),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 10),
                  decoration: BoxDecoration(
                    color: AppColors.bgEditText,
                    borderRadius: BorderRadius.circular(6),
                  ),
                  child: DropdownButton<BrandTypeModel>(
                    value: _selectedBrand,
                    isExpanded: true,
                    underline: const SizedBox(),
                    icon: const Icon(Icons.arrow_drop_down),
                    hint: const Text('Select brand'),
                    items: _brands
                        .map(
                          (b) => DropdownMenuItem(
                            value: b,
                            child: Text(
                              b.vehicleBrandName ?? 'Brand',
                              style: const TextStyle(
                                color: AppColors.black,
                                fontSize: 13,
                              ),
                            ),
                          ),
                        )
                        .toList(),
                    onChanged: (v) {
                      setState(() => _selectedBrand = v);
                    },
                  ),
                ),
                const SizedBox(height: 16),
                Text(
                  'Product name',
                  style: AppTextStyles.textView13ssp(),
                ),
                TextField(
                  controller: _productNameCtrl,
                  decoration: const InputDecoration(
                    isDense: true,
                    border: UnderlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                Text(
                  'Specialize in',
                  style: AppTextStyles.textView13ssp(),
                ),
                TextField(
                  controller: _specializeInCtrl,
                  decoration: const InputDecoration(
                    isDense: true,
                    border: UnderlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
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
                const SizedBox(height: 16),
                Text(
                  Strings.price,
                  style: AppTextStyles.textView13ssp(),
                ),
                TextField(
                  controller: _priceCtrl,
                  keyboardType: TextInputType.number,
                  decoration: const InputDecoration(
                    isDense: true,
                    border: UnderlineInputBorder(),
                  ),
                ),
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
}

