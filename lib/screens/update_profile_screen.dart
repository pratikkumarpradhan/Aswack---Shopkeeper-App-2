import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';

class UpdateProfileScreen extends StatefulWidget {
  final String? mobileNumber;

  const UpdateProfileScreen({
    Key? key,
    this.mobileNumber,
  }) : super(key: key);

  @override
  State<UpdateProfileScreen> createState() => _UpdateProfileScreenState();
}

class _UpdateProfileScreenState extends State<UpdateProfileScreen> {
  final TextEditingController _fullNameController = TextEditingController();
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _houseNoController = TextEditingController();
  final TextEditingController _streetNameController = TextEditingController();
  final TextEditingController _pincodeController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  final TextEditingController _confirmPasswordController = TextEditingController();
  final TextEditingController _dateController = TextEditingController();
  
  bool _isLoading = false;
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
    final loginData = Helper.getLoginData();
    _fullNameController.text = loginData.name;
    _mobileController.text = widget.mobileNumber ?? loginData.mobile;
    _emailController.text = loginData.email;
  }

  bool _validateFields() {
    if (_fullNameController.text.isEmpty ||
        _mobileController.text.isEmpty ||
        _emailController.text.isEmpty ||
        _pincodeController.text.isEmpty ||
        _passwordController.text.isEmpty ||
        _confirmPasswordController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Fill all details')),
      );
      return false;
    }
    if (_passwordController.text != _confirmPasswordController.text) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Password not matched')),
      );
      return false;
    }
    if (_selectedGender == null) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Select Gender')),
      );
      return false;
    }
    return true;
  }

  void _onSubmit() {
    if (_validateFields()) {
      setState(() => _isLoading = true);
      // TODO: Implement Firebase update
      Future.delayed(const Duration(seconds: 1), () {
        if (mounted) {
          setState(() => _isLoading = false);
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Profile updated successfully.')),
          );
          Navigator.pop(context);
        }
      });
    }
  }

  Future<void> _selectDate() async {
    final DateTime? picked = await showDatePicker(
      context: context,
      initialDate: DateTime.now(),
      firstDate: DateTime(1900),
      lastDate: DateTime.now(),
    );
    if (picked != null) {
      setState(() {
        _dateController.text =
            '${picked.year}/${picked.month}/${picked.day}';
      });
    }
  }

  @override
  void dispose() {
    _fullNameController.dispose();
    _mobileController.dispose();
    _emailController.dispose();
    _houseNoController.dispose();
    _streetNameController.dispose();
    _pincodeController.dispose();
    _passwordController.dispose();
    _confirmPasswordController.dispose();
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
              child: const Text(
                'Update Profile',
                style: TextStyle(
                  color: AppColors.yellow,
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
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

                    // Full Name
                    _buildLabel('Full Name'),
                    _buildTextField(
                      controller: _fullNameController,
                      hint: 'Enter Full Name',
                    ),
                    const SizedBox(height: 15),

                    // Mobile No
                    _buildLabel('Mobile No'),
                    _buildTextField(
                      controller: _mobileController,
                      hint: 'Enter Mobile No',
                      keyboardType: TextInputType.number,
                    ),
                    const SizedBox(height: 15),

                    // Email
                    _buildLabel('Email'),
                    _buildTextField(
                      controller: _emailController,
                      hint: 'Enter Email',
                      keyboardType: TextInputType.emailAddress,
                    ),
                    const SizedBox(height: 15),

                    // House No
                    _buildLabel('House no,flat/society name(optional)'),
                    _buildTextField(
                      controller: _houseNoController,
                      hint: 'Enter house no.flat/society name',
                    ),
                    const SizedBox(height: 15),

                    // Street Name
                    _buildLabel('Street name(optional)'),
                    _buildTextField(
                      controller: _streetNameController,
                      hint: 'Enter street name',
                    ),
                    const SizedBox(height: 15),

                    // Pincode
                    _buildLabel('Pincode'),
                    _buildTextField(
                      controller: _pincodeController,
                      hint: 'Enter pincode',
                      keyboardType: TextInputType.number,
                    ),
                    const SizedBox(height: 15),

                    // Date of Birth
                    _buildLabel('Date of birth'),
                    _buildTextField(
                      controller: _dateController,
                      hint: 'Select birth date',
                      suffixIcon: const Icon(Icons.calendar_today),
                      onTap: _selectDate,
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
                    ),
                    const SizedBox(height: 15),

                    // Gender
                    _buildLabel('Select Gender'),
                    Row(
                      children: [
                        Radio<String>(
                          value: 'Male',
                          groupValue: _selectedGender,
                          onChanged: (value) {
                            setState(() {
                              _selectedGender = value;
                            });
                          },
                        ),
                        const Text('Male'),
                        const SizedBox(width: 20),
                        Radio<String>(
                          value: 'Female',
                          groupValue: _selectedGender,
                          onChanged: (value) {
                            setState(() {
                              _selectedGender = value;
                            });
                          },
                        ),
                        const Text('Female'),
                      ],
                    ),
                    const SizedBox(height: 15),

                    // Password
                    _buildLabel('Password'),
                    _buildTextField(
                      controller: _passwordController,
                      hint: 'Enter Password',
                      obscureText: true,
                    ),
                    const SizedBox(height: 15),

                    // Confirm Password
                    _buildLabel('Confirm Password'),
                    _buildTextField(
                      controller: _confirmPasswordController,
                      hint: 'Enter Confirm Password',
                      obscureText: true,
                    ),
                    const SizedBox(height: 30),

                    // Submit Button
                    Center(
                      child: Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 40,
                          vertical: 5,
                        ),
                        decoration: BoxDecoration(
                          color: AppColors.black,
                          borderRadius: BorderRadius.circular(25),
                        ),
                        child: TextButton(
                          onPressed: _isLoading ? null : _onSubmit,
                          child: Text(
                            'Submit',
                            style: AppTextStyles.buttonText(),
                          ),
                        ),
                      ),
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
    bool obscureText = false,
    Widget? suffixIcon,
    VoidCallback? onTap,
  }) {
    return TextField(
      controller: controller,
      keyboardType: keyboardType,
      obscureText: obscureText,
      readOnly: onTap != null,
      onTap: onTap,
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
        onChanged: onChanged,
        isExpanded: true,
        underline: const SizedBox(),
      ),
    );
  }
}
