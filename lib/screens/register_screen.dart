import 'dart:core';
import 'package:flutter/material.dart';
import 'package:country_code_picker/country_code_picker.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import 'home_screen.dart';
import '../utils/helper.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({Key? key}) : super(key: key);

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final TextEditingController _fullNameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;
  String _countryCode = '+91';

  @override
  void dispose() {
    _fullNameController.dispose();
    _emailController.dispose();
    _mobileController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  bool _validateFields() {
    if (_fullNameController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Full Name')),
      );
      return false;
    }
    if (_emailController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Email Address')),
      );
      return false;
    }
    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(_emailController.text.trim())) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter valid Email Address')),
      );
      return false;
    }
    if (_mobileController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Mobile Number')),
      );
      return false;
    }
    if (_passwordController.text.trim().isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter Password')),
      );
      return false;
    }
    if (_passwordController.text.trim().length < 6) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Password must be at least 6 characters')),
      );
      return false;
    }
    return true;
  }

  void _onSubmit() {
    if (_validateFields()) {
      setState(() => _isLoading = true);
      // TODO: Implement Firebase registration
      // For now, simulate registration
      Future.delayed(const Duration(seconds: 1), () {
        if (mounted) {
          setState(() => _isLoading = false);
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Registration successful')),
          );
          Navigator.of(context).pushAndRemoveUntil(
            MaterialPageRoute(builder: (context) => const HomeScreen()),
            (route) => false,
          );
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.yellow,
      body: Stack(
        children: [
          SingleChildScrollView(
            padding: const EdgeInsets.symmetric(horizontal: 20),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const SizedBox(height: 20),
                Image.asset(
                  'assets/images/app_icon.png',
                  width: double.infinity,
                  height: 180,
                  fit: BoxFit.cover,
                ),
                const SizedBox(height: 30),
                Text(
                  'World Vehicle Services',
                  style: AppTextStyles.otpTitle(),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 20),
                Image.asset(
                  'assets/images/groupvehicle.png',
                  height: 80,
                  fit: BoxFit.contain,
                  errorBuilder: (_, __, ___) => Container(
                    height: 80,
                    color: AppColors.gray,
                    child: const Icon(Icons.directions_car, size: 40),
                  ),
                ),
                const SizedBox(height: 40),
                Text('Full Name', style: AppTextStyles.textView13ssp()),
                const SizedBox(height: 5),
                TextField(
                  controller: _fullNameController,
                  style: const TextStyle(color: AppColors.black),
                  decoration: const InputDecoration(
                    hintText: 'Enter full name',
                    hintStyle: TextStyle(color: AppColors.black),
                    border: InputBorder.none,
                  ),
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 20),
                Text('Email Address', style: AppTextStyles.textView13ssp()),
                const SizedBox(height: 5),
                TextField(
                  controller: _emailController,
                  keyboardType: TextInputType.emailAddress,
                  style: const TextStyle(color: AppColors.black),
                  decoration: const InputDecoration(
                    hintText: 'Enter email',
                    hintStyle: TextStyle(color: AppColors.black),
                    border: InputBorder.none,
                  ),
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 20),
                Text('Enter Phone No.', style: AppTextStyles.textView13ssp()),
                const SizedBox(height: 5),
                Row(
                  children: [
                    CountryCodePicker(
                      onChanged: (CountryCode code) {
                        _countryCode = code.dialCode ?? '+91';
                      },
                      initialSelection: 'IN',
                      showFlag: false,
                      textStyle: const TextStyle(color: AppColors.black),
                    ),
                    Expanded(
                      child: TextField(
                        controller: _mobileController,
                        keyboardType: TextInputType.number,
                        style: const TextStyle(color: AppColors.black),
                        decoration: const InputDecoration(
                          hintText: 'Enter mobile',
                          hintStyle: TextStyle(color: AppColors.gray),
                          border: InputBorder.none,
                        ),
                      ),
                    ),
                  ],
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 20),
                Text('Password', style: AppTextStyles.textView13ssp()),
                const SizedBox(height: 5),
                TextField(
                  controller: _passwordController,
                  obscureText: true,
                  maxLength: 16,
                  style: const TextStyle(color: AppColors.black),
                  decoration: const InputDecoration(
                    hintText: '********',
                    hintStyle: TextStyle(color: AppColors.gray),
                    border: InputBorder.none,
                    counterText: '',
                  ),
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 25),
                Center(
                  child: Container(
                    padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 5),
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
                const SizedBox(height: 40),
              ],
            ),
          ),
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
}
