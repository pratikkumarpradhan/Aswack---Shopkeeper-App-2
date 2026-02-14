import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import 'login_screen.dart';
import 'home_screen.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    _startTimer();
  }

  void _startTimer() async {
    await Future.delayed(const Duration(milliseconds: 1000));
    if (!mounted) return;
    final loginData = Helper.getLoginData();
    if (loginData.mobile.isEmpty) {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (_) => const LoginScreen()),
      );
    } else {
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
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Image.asset(
              'assets/images/app_icon.png',
              width: double.infinity,
              height: 180,
              fit: BoxFit.cover,
              errorBuilder: (_, __, ___) => Container(
                height: 180,
                width: double.infinity,
                color: AppColors.viewColor,
                child: const Icon(Icons.directions_car, size: 80),
              ),
            ),
            const SizedBox(height: 20),
            const Text(
              'All in One Automobile Solution',
              style: TextStyle(
                color: AppColors.black,
                fontSize: 18,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
