import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

class ChatListScreen extends StatelessWidget {
  final String? companyId;

  const ChatListScreen({super.key, this.companyId});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: const Text('Customer Chat', style: TextStyle(color: AppColors.black, fontSize: 16)),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.chat_bubble_outline, size: 64, color: AppColors.gray),
            const SizedBox(height: 16),
            Text('Customer Chat', style: TextStyle(color: AppColors.purple700, fontSize: 16)),
            const SizedBox(height: 8),
            Text('No chats yet', style: TextStyle(color: AppColors.gray, fontSize: 14)),
          ],
        ),
      ),
    );
  }
}
