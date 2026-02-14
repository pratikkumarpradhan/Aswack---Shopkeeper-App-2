import 'package:flutter/material.dart';
import 'package:country_code_picker/country_code_picker.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import 'register_screen.dart';
import 'home_screen.dart';
import '../utils/helper.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({Key? key}) : super(key: key);

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;
  String _countryCode = '+91';

  @override
  void dispose() {
    _mobileController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _onSubmit() {
    if (_mobileController.text.isNotEmpty && _passwordController.text.isNotEmpty) {
      setState(() => _isLoading = true);
      // TODO: Implement Firebase authentication
      // For now, simulate login
      Future.delayed(const Duration(seconds: 1), () {
        if (mounted) {
          setState(() => _isLoading = false);
          Navigator.of(context).pushAndRemoveUntil(
            MaterialPageRoute(builder: (context) => const HomeScreen()),
            (route) => false,
          );
        }
      });
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Fill all details')),
      );
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
              children: [
                const SizedBox(height: 20),
                // App Icon
                Image.asset(
                  'assets/images/app_icon.png',
                  width: double.infinity,
                  height: 180,
                  fit: BoxFit.cover,
                ),
                const SizedBox(height: 30),
                // Title
                Text(
                  'World Vehicle Services',
                  style: AppTextStyles.otpTitle(),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 20),
                // Vehicle Image
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
                // Mobile Number Label
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    'Enter Phone No.',
                    style: AppTextStyles.textView13ssp(),
                  ),
                ),
                const SizedBox(height: 5),
                // Country Code Picker + Mobile Field
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
                          hintStyle: TextStyle(color: AppColors.black),
                          border: InputBorder.none,
                        ),
                      ),
                    ),
                  ],
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 20),
                // Password Label
                Align(
                  alignment: Alignment.centerLeft,
                  child: Text(
                    'Password',
                    style: AppTextStyles.textView13ssp(),
                  ),
                ),
                const SizedBox(height: 5),
                TextField(
                  controller: _passwordController,
                  obscureText: true,
                  maxLength: 16,
                  style: const TextStyle(color: AppColors.black),
                  decoration: const InputDecoration(
                    hintText: '********',
                    hintStyle: TextStyle(color: AppColors.black),
                    border: InputBorder.none,
                    counterText: '',
                  ),
                ),
                Container(height: 1, color: AppColors.black),
                const SizedBox(height: 5),
                // Forgot Password
                Align(
                  alignment: Alignment.centerRight,
                  child: TextButton(
                    onPressed: () {
                      // TODO: Navigate to ForgotPasswordScreen
                    },
                    child: const Text(
                      'Forgot Password ?',
                      style: TextStyle(
                        color: AppColors.black,
                        fontSize: 13,
                      ),
                    ),
                  ),
                ),
                const SizedBox(height: 20),
                // Submit Button
                Container(
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
                const SizedBox(height: 20),
                // Register Text
                GestureDetector(
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const RegisterScreen()),
                    );
                  },
                  child: const Text(
                    'Don\'t have an account? Register',
                    style: TextStyle(
                      color: AppColors.black,
                      fontSize: 13,
                    ),
                  ),
                ),
                const SizedBox(height: 40),
              ],
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
}
