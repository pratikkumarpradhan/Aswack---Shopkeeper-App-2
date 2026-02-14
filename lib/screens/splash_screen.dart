import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import 'login_screen.dart';
import 'home_screen.dart';
import '../utils/helper.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({Key? key}) : super(key: key);

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    startTimer();
  }

  void startTimer() async {
    await Future.delayed(const Duration(seconds: 1));
    
    if (!mounted) return;
    
    // Skip connectivity check for now - can be added later
    final loginData = Helper.getLoginData();
    
    if (loginData.mobile.isEmpty) {
      Navigator.of(context).pushReplacement(
        MaterialPageRoute(builder: (context) => const LoginScreen()),
      );
    } else {
      Navigator.of(context).pushAndRemoveUntil(
        MaterialPageRoute(builder: (context) => const HomeScreen()),
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
            // App Icon
            Image.asset(
              'assets/images/app_icon.png',
              width: double.infinity,
              height: 180,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                return Container(
                  width: double.infinity,
                  height: 180,
                  color: AppColors.gray,
                  child: const Icon(Icons.directions_car, size: 80, color: AppColors.black),
                );
              },
            ),
            
            const SizedBox(height: 20),
            
            // Tagline
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
