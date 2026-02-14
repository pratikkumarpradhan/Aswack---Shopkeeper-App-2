import 'package:flutter/material.dart';
import 'screens/splash_screen.dart';
import 'utils/helper.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Helper.loadCache();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Zarooori',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        primarySwatch: Colors.yellow,
        scaffoldBackgroundColor: const Color(0xFFFFCC41),
      ),
      home: const SplashScreen(),
    );
  }
}
