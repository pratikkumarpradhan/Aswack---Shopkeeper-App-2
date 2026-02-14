import 'package:flutter/material.dart';
import 'package:country_code_picker/country_code_picker.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import 'register_screen.dart';
import 'home_screen.dart';
import 'forgot_password_screen.dart';

class LoginScreen extends StatefulWidget {
  const LoginScreen({super.key});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _mobileController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();
  bool _isLoading = false;

  @override
  void dispose() {
    _mobileController.dispose();
    _passwordController.dispose();
    super.dispose();
  }

  void _onSubmit() async {
    final mobile = _mobileController.text.trim();
    final password = _passwordController.text.trim();
    if (mobile.isEmpty || password.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text(Strings.fill_all_details)),
      );
      return;
    }
    setState(() => _isLoading = true);
    final data = LoginData()
      ..mobile = mobile
      ..password = password
      ..name = 'User'
      ..email = ''
      ..id = mobile;
    await Helper.setLoginData(data);
    if (mounted) {
      setState(() => _isLoading = false);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text(Strings.login_successfull)),
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
                            hintStyle: TextStyle(color: AppColors.black),
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
                      hintStyle: TextStyle(color: AppColors.black),
                      border: InputBorder.none,
                      contentPadding: EdgeInsets.symmetric(
                        vertical: 10,
                        horizontal: 10,
                      ),
                      counterText: '',
                    ),
                  ),
                  Container(height: 1, color: AppColors.black),
                  const SizedBox(height: 10),
                  Align(
                    alignment: Alignment.centerRight,
                    child: TextButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (_) => const ForgotPasswordScreen(),
                          ),
                        );
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
                  const SizedBox(height: 20),
                  GestureDetector(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (_) => const RegisterScreen(),
                        ),
                      );
                    },
                    child: const Text(
                      "Don't have an account? Register",
                      style: TextStyle(
                        color: AppColors.black,
                        fontSize: 13,
                      ),
                      textAlign: TextAlign.center,
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
