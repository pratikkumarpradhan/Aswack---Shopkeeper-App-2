import 'dart:io';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/brands_types_models.dart';
import '../models/years.dart';
import '../models/fuels.dart';
import '../services/api_service.dart';
import '../utils/helper.dart';

class AddVehicleScreen extends StatefulWidget {
  final String categoryId;
  final String companyId;
  final String packageId;

  const AddVehicleScreen({
    Key? key,
    required this.categoryId,
    this.companyId = '',
    this.packageId = '',
  }) : super(key: key);

  @override
  State<AddVehicleScreen> createState() => _AddVehicleScreenState();
}

class _AddVehicleScreenState extends State<AddVehicleScreen> {
  bool _isLoading = false;

  List<BrandsTypesModels> _brandsList = [];
  List<BrandType> _typeList = [];
  List<BrandModel> _modelList = [];
  List<Years> _yearList = [];
  List<Fuels> _fuelList = [];
  final List<String> _ownersList = [
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
  int _transmission = 0; // 0=automatic, 1=manual

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

    final brandsRes =
        await ApiService.getVehicleBrandsTypesModels(widget.categoryId);
    if (!brandsRes.status) {
      setState(() => _isLoading = false);
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text(brandsRes.message)));
      }
      return;
    }

    _brandsList = brandsRes.getBrandsTypesModelsList();

    final yearsRes = await ApiService.getYearList();
    if (yearsRes.status) _yearList = yearsRes.getYearsList();

    final fuelRes = await ApiService.getFuelList();
    if (fuelRes.status) _fuelList = fuelRes.getFuelsList();

    setState(() => _isLoading = false);
  }

  void _onBrandSelected(BrandsTypesModels? brand) {
    setState(() {
      _selectedBrand = brand;
      _selectedType = null;
      _selectedModel = null;
      _typeList = brand?.typeList ?? [];
      _modelList = [];
    });
  }

  void _onTypeSelected(BrandType? type) {
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

    final image = await _picker.pickImage(
      source: source,
      maxWidth: 1200,
      imageQuality: 80,
    );
    if (image != null && mounted) {
      setState(() => _imagePaths.add(image.path));
    }
  }

  void _removeImage(int index) {
    setState(() => _imagePaths.removeAt(index));
  }

  Future<void> _submit() async {
    if (_titleController.text.isEmpty ||
        _descriptionController.text.isEmpty ||
        _priceController.text.isEmpty ||
        _kmController.text.isEmpty ||
        _contactController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please fill all required fields')),
      );
      return;
    }

    if (_selectedBrand == null ||
        _selectedType == null ||
        _selectedModel == null ||
        _selectedYear == null ||
        _selectedFuel == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please select Brand, Type, Model, Year and Fuel')),
      );
      return;
    }

    if (_imagePaths.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please add at least one image')),
      );
      return;
    }

    setState(() => _isLoading = true);

    final loginData = Helper.getLoginData();
    final response = await ApiService.sellVehicleWithImages(
      userId: loginData.id.isNotEmpty ? loginData.id : '0',
      sellerCompanyId: widget.companyId,
      userType: '1',
      vehicleCat: widget.categoryId,
      vehicleBrand: _selectedBrand!.vehicleBrandId ?? '',
      vehicleType: _selectedType!.vehicleTypeId ?? '',
      vehicleModel: _selectedModel!.vehicleModelId ?? '',
      vehicleYear: _selectedYear!.id ?? '',
      vehicleFuel: _selectedFuel!.id ?? '',
      transmission: _transmission.toString(),
      drivenKm: _kmController.text,
      title: _titleController.text,
      owners: _selectedOwnerIndex.toString(),
      contactNumber: _contactController.text,
      price: _priceController.text,
      description: _descriptionController.text,
      packagePurchasedId: widget.packageId,
      imagePaths: _imagePaths,
    );

    setState(() => _isLoading = false);

    if (!mounted) return;
    if (response.status) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Vehicle added successfully')),
      );
      Navigator.pop(context, true);
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
          'Add Vehicle',
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

                  _buildDropdown('Select Brand', _selectedBrand?.vehicleBrandName,
                      _brandsList.map((b) => b.vehicleBrandName ?? '').toList(),
                      (v) => _onBrandSelected(
                          _brandsList[_brandsList
                              .indexWhere((b) => b.vehicleBrandName == v)])),

                  _buildDropdown('Select Type', _selectedType?.vehicleTypeName,
                      _typeList.map((t) => t.vehicleTypeName ?? '').toList(),
                      (v) => _onTypeSelected(_typeList[_typeList
                          .indexWhere((t) => t.vehicleTypeName == v)])),

                  _buildDropdown('Select Model', _selectedModel?.vehicleModelName,
                      _modelList.map((m) => m.vehicleModelName ?? '').toList(),
                      (v) => setState(() => _selectedModel = _modelList[
                          _modelList.indexWhere((m) => m.vehicleModelName == v)])),

                  _buildDropdown('Select Year', _selectedYear?.year,
                      _yearList.map((y) => y.year ?? '').toList(),
                      (v) => setState(() => _selectedYear = _yearList[
                          _yearList.indexWhere((y) => y.year == v)])),

                  _buildDropdown('Select Fuel', _selectedFuel?.fuel,
                      _fuelList.map((f) => f.fuel ?? '').toList(),
                      (v) => setState(() => _selectedFuel = _fuelList[
                          _fuelList.indexWhere((f) => f.fuel == v)])),

                  _buildDropdown('Owner', _ownersList[_selectedOwnerIndex],
                      _ownersList, (v) => setState(() =>
                          _selectedOwnerIndex = v != null ? _ownersList.indexOf(v) : 0)),

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

  Widget _buildDropdown(
    String label,
    String? value,
    List<String> options,
    ValueChanged<String?> onChanged,
  ) {
    final optionsNotEmpty = options.isNotEmpty;
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(label, style: AppTextStyles.textView13ssp()),
          const SizedBox(height: 4),
          Container(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            decoration: BoxDecoration(
              color: AppColors.bgEditText,
              borderRadius: BorderRadius.circular(8),
            ),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                value: optionsNotEmpty && value != null && options.contains(value)
                    ? value
                    : null,
                hint: Text(
                  optionsNotEmpty ? 'Select $label' : 'No options',
                  style: AppTextStyles.textView13ssp(),
                ),
                isExpanded: true,
                items: optionsNotEmpty
                    ? options
                        .map((o) => DropdownMenuItem(
                            value: o, child: Text(o)))
                        .toList()
                    : null,
                onChanged: optionsNotEmpty ? onChanged : null,
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTextField(String label, TextEditingController controller,
      {int maxLines = 1, TextInputType? keyboardType}) {
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
