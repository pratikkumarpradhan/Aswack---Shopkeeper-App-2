import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../utils/helper.dart';

class DashboardScreen extends StatefulWidget {
  final String? categoryId;
  final String? companyId;
  final String? packageId;

  const DashboardScreen({
    Key? key,
    this.categoryId,
    this.companyId,
    this.packageId,
  }) : super(key: key);

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
      case '1':
        _appTitle = 'Sell Vehicle';
        break;
      case '3':
        _appTitle = 'Garage';
        break;
      case '4':
        _appTitle = 'Vehicle Insurance';
        break;
      case '5':
        _appTitle = 'Emergency Service\'s';
        break;
      case '6':
        _appTitle = 'Spare Parts';
        break;
      case '7':
        _appTitle = 'Car Accessories';
        break;
      case '8':
        _appTitle = 'Hire Heavy Equipment';
        break;
      case '9':
        _appTitle = 'Tyre Services';
        break;
      case '10':
        _appTitle = 'Break Down';
        break;
      case '11':
        _appTitle = 'Rent a Car';
        break;
      case '12':
        _appTitle = 'Courier';
        break;
      default:
        _appTitle = 'Aswack';
    }
  }

  void _toggleFabs() {
    setState(() {
      _isAllFabsVisible = !_isAllFabsVisible;
    });
  }

  void _onMyProductClick() {
    // TODO: Navigate based on categoryId
    print('My Product clicked - Category: ${widget.categoryId}');
  }

  void _onMyOfferClick() {
    // TODO: Navigate to MyOfferListScreen
    print('My Offer clicked');
  }

  void _onRFQClick() {
    // TODO: Navigate to RFQListScreen
    print('RFQ clicked');
  }

  void _onGeneratingNotificationClick() {
    // TODO: Navigate to SellerNotificationListScreen
    print('Generating Notification clicked');
  }

  void _onMyOrdersClick() {
    // TODO: Navigate to OrderListScreen
    print('My Orders clicked');
  }

  void _onCustomerChatClick() {
    // TODO: Navigate to ChatListScreen
    print('Customer Chat clicked');
  }

  void _onMyRatingClick() {
    // TODO: Navigate to RatingAndReviewScreen
    print('My Rating clicked');
  }

  void _onCustomiseRequirementClick() {
    // TODO: Navigate to SellerWisePackageListScreen
    print('Customise Requirement clicked');
  }

  void _onProfileFabClick() {
    // TODO: Navigate to CompanyProfileScreen
    print('Profile FAB clicked');
  }

  void _onAdminFabClick() {
    // TODO: Show contact us dialog
    print('Admin FAB clicked');
  }

  @override
  Widget build(BuildContext context) {
    final loginData = Helper.getLoginData();
    final userName = loginData.name.isNotEmpty ? loginData.name : 'User';

    return Scaffold(
      backgroundColor: AppColors.yellow,
      body: Stack(
        children: [
          // App Name Header
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            child: Container(
              padding: const EdgeInsets.all(15),
              color: AppColors.purple700,
              child: Text(
                _appTitle,
                style: const TextStyle(
                  color: AppColors.white,
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
                textAlign: TextAlign.center,
              ),
            ),
          ),

          // Content ScrollView
          Positioned(
            top: 60,
            left: 0,
            right: 0,
            bottom: 0,
            child: Container(
              decoration: BoxDecoration(
                color: AppColors.white,
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
              ),
              child: SingleChildScrollView(
                padding: const EdgeInsets.symmetric(
                  horizontal: 10,
                  vertical: 15,
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // Welcome Section
                    Text(
                      'Welcome',
                      style: TextStyle(
                        color: AppColors.orange,
                        fontSize: 13,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      'Welcome $userName',
                      style: TextStyle(
                        color: AppColors.purple700,
                        fontSize: 13,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 5),
                    Text(
                      'We are Happy to help you us to grow your Business',
                      style: TextStyle(
                        color: AppColors.gray,
                        fontSize: 10,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                    const SizedBox(height: 10),

                    // Grid of Dashboard Items
                    _buildDashboardGrid(),
                  ],
                ),
              ),
            ),
          ),

          // Floating Action Buttons
          Positioned(
            bottom: 16,
            right: 16,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                // Admin FAB
                if (_isAllFabsVisible)
                  Padding(
                    padding: const EdgeInsets.only(bottom: 5),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 5,
                            vertical: 2,
                          ),
                          decoration: BoxDecoration(
                            color: AppColors.orange,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: const Text(
                            'Admin Details',
                            style: TextStyle(
                              color: AppColors.white,
                              fontSize: 11,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                        const SizedBox(width: 5),
                        FloatingActionButton(
                          mini: true,
                          backgroundColor: AppColors.orange,
                          onPressed: _onAdminFabClick,
                          child: const Icon(
                            Icons.home,
                            color: AppColors.white,
                            size: 16,
                          ),
                        ),
                      ],
                    ),
                  ),

                // Profile FAB
                if (_isAllFabsVisible)
                  Padding(
                    padding: const EdgeInsets.only(bottom: 5),
                    child: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 5,
                            vertical: 2,
                          ),
                          decoration: BoxDecoration(
                            color: AppColors.orange,
                            borderRadius: BorderRadius.circular(10),
                          ),
                          child: const Text(
                            'Company Profile',
                            style: TextStyle(
                              color: AppColors.white,
                              fontSize: 11,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                        const SizedBox(width: 5),
                        FloatingActionButton(
                          mini: true,
                          backgroundColor: AppColors.orange,
                          onPressed: _onProfileFabClick,
                          child: const Icon(
                            Icons.home,
                            color: AppColors.white,
                            size: 16,
                          ),
                        ),
                      ],
                    ),
                  ),

                // Main Add FAB
                FloatingActionButton(
                  backgroundColor: AppColors.purple700,
                  onPressed: _toggleFabs,
                  child: Icon(
                    _isAllFabsVisible ? Icons.close : Icons.add,
                    color: AppColors.white,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDashboardGrid() {
    return Column(
      children: [
        // Row 1: My Product & My Offer
        Row(
          children: [
            Expanded(
              child: _buildDashboardItem(
                'My Products',
                'assets/images/ic_my_product.png',
                _onMyProductClick,
              ),
            ),
            const SizedBox(width: 5),
            Expanded(
              child: _buildDashboardItem(
                'My Offers',
                'assets/images/ic_my_offers.png',
                _onMyOfferClick,
              ),
            ),
          ],
        ),
        const SizedBox(height: 10),

        // Row 2: RFQ & Generating Notification
        Row(
          children: [
            Expanded(
              child: _buildDashboardItem(
                'RFQ',
                'assets/images/ic_dash_rfq.png',
                _onRFQClick,
              ),
            ),
            const SizedBox(width: 5),
            Expanded(
              child: _buildDashboardItem(
                'Generating Notification',
                'assets/images/ic_gen_noti.png',
                _onGeneratingNotificationClick,
              ),
            ),
          ],
        ),
        const SizedBox(height: 10),

        // Row 3: My Orders & Customer Chat
        Row(
          children: [
            Expanded(
              child: _buildDashboardItem(
                'My Orders',
                'assets/images/ic_my_orders.png',
                _onMyOrdersClick,
              ),
            ),
            const SizedBox(width: 5),
            Expanded(
              child: _buildDashboardItem(
                'Customer Chat',
                'assets/images/ic_custom_chat.png',
                _onCustomerChatClick,
              ),
            ),
          ],
        ),
        const SizedBox(height: 10),

        // Row 4: My Rating & Customise Requirement
        Row(
          children: [
            Expanded(
              child: _buildDashboardItem(
                'My Rating',
                'assets/images/ic_rating.png',
                _onMyRatingClick,
              ),
            ),
            const SizedBox(width: 5),
            Expanded(
              child: _buildDashboardItem(
                'Customise Requirement',
                'assets/images/ic_custome_requirement.png',
                _onCustomiseRequirementClick,
              ),
            ),
          ],
        ),
      ],
    );
  }

  Widget _buildDashboardItem(
    String title,
    String imagePath,
    VoidCallback onTap,
  ) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          ClipRRect(
            borderRadius: BorderRadius.circular(10),
            child: Image.asset(
              imagePath,
              width: double.infinity,
              height: 100,
              fit: BoxFit.cover,
              errorBuilder: (context, error, stackTrace) {
                return Container(
                  width: double.infinity,
                  height: 100,
                  color: AppColors.gray,
                  child: const Icon(Icons.image, size: 50),
                );
              },
            ),
          ),
          const SizedBox(height: 5),
          Text(
            title,
            style: TextStyle(
              color: AppColors.purple700,
              fontSize: 10,
              fontWeight: FontWeight.w500,
            ),
            textAlign: TextAlign.center,
          ),
        ],
      ),
    );
  }
}
