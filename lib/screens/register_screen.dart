import 'package:flutter/material.dart';
import 'package:country_code_picker/country_code_picker.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import 'home_screen.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final TextEditingController _fullNameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;

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
    if (!RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$')
        .hasMatch(_emailController.text.trim())) {
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
        const SnackBar(
            content: Text('Password must be at least 6 characters')),
      );
      return false;
    }
    return true;
  }

  void _onSubmit() async {
    if (!_validateFields()) return;
    setState(() => _isLoading = true);
    final data = LoginData()
      ..name = _fullNameController.text.trim()
      ..email = _emailController.text.trim()
      ..mobile = _mobileController.text.trim()
      ..password = _passwordController.text.trim()
      ..id = _mobileController.text.trim();
    await Helper.setLoginData(data);
    if (mounted) {
      setState(() => _isLoading = false);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text(Strings.registration_successfull)),
      );
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (_) => const HomeScreen()),
        (route) => false,
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
            child: Padding(
              padding: const EdgeInsets.symmetric(horizontal: 20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  const SizedBox(height: 20),
                  Image.asset(
                    'assets/images/app_icon.png',
                    height: 180,
                    fit: BoxFit.cover,
                    errorBuilder: (_, __, ___) => Container(
                      height: 180,
                      color: AppColors.viewColor,
                      child: const Icon(Icons.image, size: 60),
                    ),
                  ),
                  const SizedBox(height: 30),
                  Text(
                    Strings.world_vehical_service,
                    style: const TextStyle(
                      color: AppColors.black,
                      fontSize: 18,
                      fontWeight: FontWeight.w600,
                    ),
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 5),
                  Image.asset(
                    'assets/images/groupvehicle.png',
                    height: 60,
                    fit: BoxFit.contain,
                    errorBuilder: (_, __, ___) => const SizedBox(height: 60),
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    'Full Name',
                    style: TextStyle(color: AppColors.black, fontSize: 13),
                  ),
                  const SizedBox(height: 5),
                  TextField(
                    controller: _fullNameController,
                    style: const TextStyle(color: AppColors.black),
                    decoration: const InputDecoration(
                      hintText: 'Enter full name',
                      hintStyle: TextStyle(color: AppColors.black),
                      border: InputBorder.none,
                      contentPadding: EdgeInsets.symmetric(
                        vertical: 10,
                        horizontal: 10,
                      ),
                    ),
                  ),
                  Container(height: 1, color: AppColors.black),
                  const SizedBox(height: 15),
                  const Text(
                    'Email Address',
                    style: TextStyle(color: AppColors.black, fontSize: 13),
                  ),
                  const SizedBox(height: 5),
                  TextField(
                    controller: _emailController,
                    keyboardType: TextInputType.emailAddress,
                    style: const TextStyle(color: AppColors.black),
                    decoration: const InputDecoration(
                      hintText: 'Enter email',
                      hintStyle: TextStyle(color: AppColors.black),
                      border: InputBorder.none,
                      contentPadding: EdgeInsets.symmetric(
                        vertical: 10,
                        horizontal: 10,
                      ),
                    ),
                  ),
                  Container(height: 1, color: AppColors.black),
                  const SizedBox(height: 15),
                  Text(
                    Strings.enter_email_phone_no,
                    style: const TextStyle(color: AppColors.black, fontSize: 13),
                  ),
                  const SizedBox(height: 5),
                  Row(
                    children: [
                      CountryCodePicker(
                        onChanged: (_) {},
                        initialSelection: 'IN',
                        showFlag: false,
                        textStyle: const TextStyle(
                          color: AppColors.black,
                          fontSize: 16,
                        ),
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
                            contentPadding: EdgeInsets.symmetric(
                              vertical: 10,
                              horizontal: 10,
                            ),
                          ),
                        ),
                      ),
                    ],
                  ),
                  Container(height: 1, color: AppColors.black),
                  const SizedBox(height: 15),
                  Text(
                    Strings.password,
                    style: const TextStyle(color: AppColors.black, fontSize: 13),
                  ),
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
                      contentPadding: EdgeInsets.symmetric(
                        vertical: 10,
                        horizontal: 10,
                      ),
                      counterText: '',
                    ),
                  ),
                  Container(height: 1, color: AppColors.black),
                  const SizedBox(height: 30),
                  Center(
                    child: Material(
                      color: AppColors.black,
                      borderRadius: BorderRadius.circular(25),
                      child: InkWell(
                        onTap: _isLoading ? null : _onSubmit,
                        borderRadius: BorderRadius.circular(25),
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 40,
                            vertical: 12,
                          ),
                          child: Text(
                            Strings.submit,
                            style: const TextStyle(
                              color: AppColors.yellow,
                              fontSize: 13,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 40),
                ],
              ),
            ),
          ),
          if (_isLoading)
            Container(
              color: Colors.black.withValues(alpha: 0.3),
              child: const Center(child: CircularProgressIndicator()),
            ),
        ],
      ),
    );
  }
}
