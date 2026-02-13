import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';
import 'update_profile_screen.dart';

class ProfileScreen extends StatefulWidget {
  final String? mobileNumber;

  const ProfileScreen({
    Key? key,
    this.mobileNumber,
  }) : super(key: key);

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  final TextEditingController _fullNameController = TextEditingController();
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _houseNoController = TextEditingController();
  final TextEditingController _streetNameController = TextEditingController();
  final TextEditingController _pincodeController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  
  bool _isLoading = false;
  bool _isEditMode = false;
  String? _selectedCountry;
  String? _selectedState;
  String? _selectedCity;
  String? _selectedGender;

  @override
  void initState() {
    super.initState();
    _loadProfileData();
  }

  void _loadProfileData() {
    // TODO: Load from Firebase
    final loginData = Helper.getLoginData();
    _fullNameController.text = loginData.name;
    _mobileController.text = widget.mobileNumber ?? loginData.mobile;
    _emailController.text = loginData.email;
    
    // Make fields read-only initially
    _fullNameController.addListener(() {});
    _mobileController.addListener(() {});
    _emailController.addListener(() {});
  }

  void _onEditClick() {
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => UpdateProfileScreen(
          mobileNumber: widget.mobileNumber,
        ),
      ),
    );
  }

  @override
  void dispose() {
    _fullNameController.dispose();
    _mobileController.dispose();
    _emailController.dispose();
    _houseNoController.dispose();
    _streetNameController.dispose();
    _pincodeController.dispose();
    _dateController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.black,
      body: Stack(
        children: [
          // App Name Header
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            child: Container(
              padding: const EdgeInsets.all(15),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Aswack',
                    style: TextStyle(
                      color: AppColors.yellow,
                      fontSize: 20,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  GestureDetector(
                    onTap: _onEditClick,
                    child: Row(
                      children: [
                        const Icon(
                          Icons.edit_square,
                          color: AppColors.yellow,
                          size: 20,
                        ),
                        const SizedBox(width: 4),
                        Text(
                          'Edit',
                          style: TextStyle(
                            color: AppColors.yellow,
                            fontSize: 13,
                          ),
                        ),
                      ],
                    ),
                  ),
                ],
              ),
            ),
          ),

          // Content ScrollView
          Positioned(
            top: 60,
            left: 0,
            right: 0,
            bottom: 0,
            child: Container(
              decoration: BoxDecoration(
                color: AppColors.white,
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
              ),
              child: SingleChildScrollView(
                padding: const EdgeInsets.symmetric(
                  horizontal: 10,
                  vertical: 20,
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Back Button
                    Row(
                      children: [
                        IconButton(
                          icon: const Icon(Icons.arrow_back),
                          onPressed: () => Navigator.pop(context),
                        ),
                      ],
                    ),

                    // Profile Label
                    Text(
                      'Profile',
                      style: TextStyle(
                        color: AppColors.yellow,
                        fontSize: 20,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 20),

                    // Full Name
                    _buildLabel('Full Name'),
                    _buildTextField(
                      controller: _fullNameController,
                      hint: 'Enter Full Name',
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Mobile No
                    _buildLabel('Mobile No'),
                    _buildTextField(
                      controller: _mobileController,
                      hint: 'Enter Mobile No',
                      keyboardType: TextInputType.number,
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Email
                    _buildLabel('Email'),
                    _buildTextField(
                      controller: _emailController,
                      hint: 'Enter Email',
                      keyboardType: TextInputType.emailAddress,
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // House No
                    _buildLabel('House no,flat/society name(optional)'),
                    _buildTextField(
                      controller: _houseNoController,
                      hint: 'Enter house no.flat/society name',
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Street Name
                    _buildLabel('Street name(optional)'),
                    _buildTextField(
                      controller: _streetNameController,
                      hint: 'Enter street name',
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Pincode
                    _buildLabel('Pincode'),
                    _buildTextField(
                      controller: _pincodeController,
                      hint: 'Enter pincode',
                      keyboardType: TextInputType.number,
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Date of Birth
                    _buildLabel('Date of birth'),
                    _buildTextField(
                      controller: _dateController,
                      hint: 'Select birth date',
                      enabled: false,
                      suffixIcon: const Icon(Icons.calendar_today),
                    ),
                    const SizedBox(height: 15),

                    // Country
                    _buildLabel('Select Country'),
                    _buildDropdown(
                      value: _selectedCountry,
                      items: ['Select Country', 'India', 'USA'],
                      onChanged: (value) {
                        setState(() {
                          _selectedCountry = value;
                        });
                      },
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // State
                    _buildLabel('Select State'),
                    _buildDropdown(
                      value: _selectedState,
                      items: ['Select State', 'Maharashtra', 'Gujarat'],
                      onChanged: (value) {
                        setState(() {
                          _selectedState = value;
                        });
                      },
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // City
                    _buildLabel('Select City'),
                    _buildDropdown(
                      value: _selectedCity,
                      items: ['Select City', 'Mumbai', 'Pune'],
                      onChanged: (value) {
                        setState(() {
                          _selectedCity = value;
                        });
                      },
                      enabled: false,
                    ),
                    const SizedBox(height: 15),

                    // Gender
                    _buildLabel('Select Gender'),
                    Row(
                      children: [
                        Radio<String>(
                          value: 'Male',
                          groupValue: _selectedGender,
                          onChanged: null,
                        ),
                        const Text('Male'),
                        const SizedBox(width: 20),
                        Radio<String>(
                          value: 'Female',
                          groupValue: _selectedGender,
                          onChanged: null,
                        ),
                        const Text('Female'),
                      ],
                    ),
                    const SizedBox(height: 30),
                  ],
                ),
              ),
            ),
          ),

          // Loading Indicator
          if (_isLoading)
            Positioned.fill(
              child: Container(
                color: Colors.black.withOpacity(0.3),
                child: const Center(
                  child: CircularProgressIndicator(),
                ),
              ),
            ),
        ],
      ),
    );
  }

  Widget _buildLabel(String text) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 5),
      child: Text(
        text,
        style: TextStyle(
          color: AppColors.orange,
          fontSize: 13,
        ),
      ),
    );
  }

  Widget _buildTextField({
    required TextEditingController controller,
    required String hint,
    TextInputType? keyboardType,
    bool enabled = true,
    Widget? suffixIcon,
  }) {
    return TextField(
      controller: controller,
      enabled: enabled,
      keyboardType: keyboardType,
      style: const TextStyle(color: AppColors.black),
      decoration: InputDecoration(
        hintText: hint,
        hintStyle: const TextStyle(color: AppColors.gray),
        filled: true,
        fillColor: AppColors.bgEditText,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(5),
          borderSide: BorderSide.none,
        ),
        suffixIcon: suffixIcon,
      ),
    );
  }

  Widget _buildDropdown({
    required String? value,
    required List<String> items,
    required ValueChanged<String?> onChanged,
    bool enabled = true,
  }) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12),
      decoration: BoxDecoration(
        color: AppColors.bgEditText,
        borderRadius: BorderRadius.circular(5),
      ),
      child: DropdownButton<String>(
        value: value ?? items.first,
        items: items.map((String item) {
          return DropdownMenuItem<String>(
            value: item,
            child: Text(item),
          );
        }).toList(),
        onChanged: enabled ? onChanged : null,
        isExpanded: true,
        underline: const SizedBox(),
      ),
    );
  }
}
