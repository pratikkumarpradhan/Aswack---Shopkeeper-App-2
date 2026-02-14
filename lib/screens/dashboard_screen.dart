import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import 'sell_vehicle_list_screen.dart';
import 'category_list_screen.dart';
import 'my_offer_list_screen.dart';
import 'rfq_list_screen.dart';
import 'seller_notification_screen.dart';
import 'rating_review_screen.dart';
import 'seller_wise_package_list_screen.dart';
import 'chat_list_screen.dart';
import 'order_list_screen.dart';
import 'company_profile_screen.dart';
import 'page_detail_screen.dart';

class DashboardScreen extends StatefulWidget {
  final String? categoryId;
  final String? companyId;
  final String? packageId;

  const DashboardScreen({
    super.key,
    this.categoryId,
    this.companyId,
    this.packageId,
  });

  @override
  State<DashboardScreen> createState() => _DashboardScreenState();
}

class _DashboardScreenState extends State<DashboardScreen> {
  bool _isAllFabsVisible = false;
  String _appTitle = 'Aswack';

  @override
  void initState() {
    super.initState();
    _setAppTitle();
  }

  void _setAppTitle() {
    switch (widget.categoryId) {
      case '1': _appTitle = 'Sell Vehicle'; break;
      case '3': _appTitle = 'Garage'; break;
      case '4': _appTitle = 'Vehicle Insurance'; break;
      case '5': _appTitle = 'Emergency Service\'s'; break;
      case '6': _appTitle = 'Spare Parts'; break;
      case '7': _appTitle = 'Car Accessories'; break;
      case '8': _appTitle = 'Hire Heavy Equipment'; break;
      case '9': _appTitle = 'Tyre Services'; break;
      case '10': _appTitle = 'Break Down'; break;
      case '11': _appTitle = 'Rent a Car'; break;
      case '12': _appTitle = 'Courier'; break;
      default: _appTitle = 'Aswack';
    }
  }

  void _toggleFabs() => setState(() => _isAllFabsVisible = !_isAllFabsVisible);

  void _onMyProductClick() {
    final cat = widget.categoryId ?? '';
    if (cat == '1') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => SellVehicleListScreen(categoryId: cat, companyId: widget.companyId, packageId: widget.packageId)));
    } else if (cat == '4' || cat == '8' || cat == '11') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => CategoryListScreen(
        title: cat == '4' ? 'Vehicle Insurance' : cat == '8' ? 'Hire Heavy Equipment' : 'Rent a Car',
        mainCatId: cat,
        companyId: widget.companyId,
        packageId: widget.packageId,
      )));
    } else if (cat == '6' || cat == '7') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => CategoryListScreen(
        title: cat == '6' ? 'Spare Parts' : 'Car Accessories',
        mainCatId: cat,
        companyId: widget.companyId,
        packageId: widget.packageId,
      )));
    } else if (cat == '9') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => CategoryListScreen(title: 'Tyre Services', mainCatId: cat, companyId: widget.companyId, packageId: widget.packageId)));
    } else if (cat == '3' || cat == '5' || cat == '10') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => CategoryListScreen(
        title: cat == '3' ? 'Garage' : cat == '5' ? 'Emergency Service' : 'Break Down',
        mainCatId: cat,
        companyId: widget.companyId,
        packageId: widget.packageId,
      )));
    } else if (cat == '12') {
      Navigator.push(context, MaterialPageRoute(builder: (_) => CategoryListScreen(title: 'Courier', mainCatId: cat, companyId: widget.companyId, packageId: widget.packageId)));
    }
  }

  void _onMyOfferClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => MyOfferListScreen(categoryId: widget.categoryId, companyId: widget.companyId, packageId: widget.packageId)));
  }

  void _onRFQClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => RFQListScreen(categoryId: widget.categoryId, companyId: widget.companyId)));
  }

  void _onGeneratingNotificationClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => SellerNotificationScreen(categoryId: widget.categoryId, companyId: widget.companyId, packageId: widget.packageId)));
  }

  void _onMyOrdersClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => OrderListScreen(companyId: widget.companyId)));
  }

  void _onCustomerChatClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => ChatListScreen(companyId: widget.companyId)));
  }

  void _onMyRatingClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => RatingReviewScreen(categoryId: widget.categoryId, companyId: widget.companyId)));
  }

  void _onCustomiseRequirementClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => SellerWisePackageListScreen(categoryId: widget.categoryId, companyId: widget.companyId)));
  }

  void _onProfileFabClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => CompanyProfileScreen(categoryId: widget.categoryId, companyId: widget.companyId)));
  }

  void _onAdminFabClick() {
    Navigator.push(context, MaterialPageRoute(builder: (_) => const PageDetailScreen(pageNo: '2', title: 'Contact Us')));
  }

  @override
  Widget build(BuildContext context) {
    final loginData = Helper.getLoginData();
    final userName = loginData.name.isNotEmpty ? loginData.name : 'User';

    return Scaffold(
      backgroundColor: AppColors.yellow,
      appBar: AppBar(
        backgroundColor: AppColors.purple700,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.white),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(_appTitle, style: const TextStyle(color: AppColors.white, fontSize: 18, fontWeight: FontWeight.bold)),
      ),
      body: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(15),
            color: AppColors.purple700,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Welcome', style: TextStyle(color: AppColors.orange, fontSize: 13)),
                Text('Welcome $userName', style: TextStyle(color: AppColors.white, fontSize: 13)),
                Text('We are Happy to help you grow your Business', style: TextStyle(color: AppColors.white.withValues(alpha: 0.8), fontSize: 11)),
              ],
            ),
          ),
          Expanded(
            child: Container(
              decoration: BoxDecoration(
                color: AppColors.white,
                borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
              ),
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(15),
                child: _buildDashboardGrid(),
              ),
            ),
          ),
        ],
      ),
      floatingActionButton: Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.end,
        children: [
          if (_isAllFabsVisible) ...[
            _buildFabRow('Company Profile', _onProfileFabClick),
            const SizedBox(height: 8),
            _buildFabRow('Admin Details', _onAdminFabClick),
            const SizedBox(height: 8),
          ],
          FloatingActionButton(
            backgroundColor: AppColors.purple700,
            onPressed: _toggleFabs,
            child: Icon(_isAllFabsVisible ? Icons.close : Icons.add, color: AppColors.white),
          ),
        ],
      ),
    );
  }

  Widget _buildFabRow(String label, VoidCallback onTap) {
    return Row(
      mainAxisSize: MainAxisSize.min,
      children: [
        Container(
          padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
          decoration: BoxDecoration(color: AppColors.orange, borderRadius: BorderRadius.circular(10)),
          child: Text(label, style: const TextStyle(color: AppColors.white, fontSize: 11)),
        ),
        const SizedBox(width: 8),
        FloatingActionButton(
          mini: true,
          backgroundColor: AppColors.orange,
          onPressed: onTap,
          child: const Icon(Icons.home, color: AppColors.white, size: 20),
        ),
      ],
    );
  }

  Widget _buildDashboardGrid() {
    return Column(
      children: [
        Row(
          children: [
            Expanded(child: _buildDashboardItem('My Products', Icons.inventory_2, _onMyProductClick)),
            const SizedBox(width: 10),
            Expanded(child: _buildDashboardItem('My Offers', Icons.local_offer, _onMyOfferClick)),
          ],
        ),
        const SizedBox(height: 15),
        Row(
          children: [
            Expanded(child: _buildDashboardItem('RFQ', Icons.request_quote, _onRFQClick)),
            const SizedBox(width: 10),
            Expanded(child: _buildDashboardItem('Generating Notification', Icons.notifications_active, _onGeneratingNotificationClick)),
          ],
        ),
        const SizedBox(height: 15),
        Row(
          children: [
            Expanded(child: _buildDashboardItem('My Orders', Icons.shopping_cart, _onMyOrdersClick)),
            const SizedBox(width: 10),
            Expanded(child: _buildDashboardItem('Customer Chat', Icons.chat, _onCustomerChatClick)),
          ],
        ),
        const SizedBox(height: 15),
        Row(
          children: [
            Expanded(child: _buildDashboardItem('My Rating', Icons.star, _onMyRatingClick)),
            const SizedBox(width: 10),
            Expanded(child: _buildDashboardItem('Customise Requirement', Icons.tune, _onCustomiseRequirementClick)),
          ],
        ),
      ],
    );
  }

  Widget _buildDashboardItem(String title, IconData icon, VoidCallback onTap) {
    return GestureDetector(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.all(16),
        decoration: BoxDecoration(
          color: AppColors.bgEditText,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: AppColors.viewColor),
        ),
        child: Column(
          children: [
            Icon(icon, size: 40, color: AppColors.purple700),
            const SizedBox(height: 8),
            Text(
              title,
              style: TextStyle(color: AppColors.purple700, fontSize: 11, fontWeight: FontWeight.w600),
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
          ],
        ),
      ),
    );
  }
}
