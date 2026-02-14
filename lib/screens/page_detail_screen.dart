import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

/// About Us (1), Contact Us (2), Privacy Policy (3), Terms (4)
class PageDetailScreen extends StatelessWidget {
  final String pageNo;
  final String title;

  const PageDetailScreen({super.key, required this.pageNo, required this.title});

  @override
  Widget build(BuildContext context) {
    final content = _getContent();
    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(title, style: const TextStyle(color: AppColors.black, fontSize: 16)),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(20),
        child: Text(content, style: TextStyle(color: AppColors.black, fontSize: 14)),
      ),
    );
  }

  String _getContent() {
    switch (pageNo) {
      case '1':
        return 'About Us\n\nZarooori - World Vehicle Services. All in One Automobile Solution.\n\nWe provide comprehensive vehicle services including selling, buying, garage services, insurance, spare parts, and more.';
      case '2':
        return 'Contact Us\n\nEmail: support@aswack.com\nPhone: +91 7635932119\nWebsite: https://aswack.com';
      case '3':
        return 'Privacy Policy\n\nYour privacy is important to us. We collect and use your data to provide our services. Please visit our website for full policy.';
      case '4':
        return 'Terms and Conditions\n\nBy using this app you agree to our terms. Please visit our website for complete terms.';
      default:
        return 'Page content';
    }
  }
}
