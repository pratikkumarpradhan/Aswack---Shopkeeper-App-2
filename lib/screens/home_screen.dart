import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import 'login_screen.dart';
import '../utils/helper.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  bool _isLoading = false;

  void _openDrawer() {
    _scaffoldKey.currentState?.openDrawer();
  }

  void _closeDrawer() {
    _scaffoldKey.currentState?.closeDrawer();
  }

  void _onCategoryClick(String categoryId) {
    // TODO: Navigate to respective category screens
    print('Category clicked: $categoryId');
  }

  void _onLogout() {
    Helper.clearLoginData();
    Navigator.of(context).pushAndRemoveUntil(
      MaterialPageRoute(builder: (context) => const LoginScreen()),
      (route) => false,
    );
  }

  @override
  Widget build(BuildContext context) {
    final loginData = Helper.getLoginData();
    
    return Scaffold(
      key: _scaffoldKey,
      backgroundColor: AppColors.yellow,
      drawer: _buildDrawer(loginData),
      body: Stack(
        children: [
          // Header
          Positioned(
            top: 0,
            left: 0,
            right: 0,
            child: _buildHeader(loginData),
          ),

          // Content
          Positioned(
            top: 80,
            left: 0,
            right: 0,
            bottom: 0,
            child: SingleChildScrollView(
              child: Column(
                children: [
                  // Main Image
                  Image.asset(
                    'assets/images/main_image.png',
                    width: double.infinity,
                    height: 250,
                    fit: BoxFit.cover,
                    errorBuilder: (_, __, ___) => Container(
                      width: double.infinity,
                      height: 250,
                      color: AppColors.gray,
                      child: const Icon(Icons.image, size: 80),
                    ),
                  ),

                  // Image Slider
                  SizedBox(
                    width: double.infinity,
                    height: 120,
                    child: PageView.builder(
                      itemCount: 3,
                      itemBuilder: (context, index) {
                        return Image.asset(
                          'assets/images/slider_${index + 1}.png',
                          fit: BoxFit.cover,
                          errorBuilder: (_, __, ___) => Container(
                            color: AppColors.gray,
                            child: const Icon(Icons.image, size: 50),
                          ),
                        );
                      },
                    ),
                  ),

                  // Our Services Section
                  _buildOurServicesSection(),

                  const SizedBox(height: 100), // Space for FAB
                ],
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
                // Chat FAB
                FloatingActionButton(
                  mini: true,
                  backgroundColor: AppColors.black,
                  onPressed: () {
                    // TODO: Navigate to Chat
                  },
                  child: const Icon(Icons.chat, color: AppColors.white),
                ),
                const SizedBox(height: 5),
                // Call FAB
                FloatingActionButton(
                  mini: true,
                  backgroundColor: AppColors.black,
                  onPressed: () {
                    // TODO: Make call
                  },
                  child: const Icon(Icons.call, color: AppColors.yellow, size: 16),
                ),
                const SizedBox(height: 5),
                // Add FAB
                FloatingActionButton(
                  backgroundColor: AppColors.yellow,
                  onPressed: () {
                    // TODO: Show add options
                  },
                  child: const Icon(Icons.add, color: AppColors.black),
                ),
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

  Widget _buildHeader(loginData) {
    return Container(
      padding: const EdgeInsets.all(10),
      color: AppColors.yellow,
      child: Row(
        children: [
          IconButton(
            icon: const Icon(Icons.menu, color: AppColors.black),
            onPressed: _openDrawer,
          ),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  'Hello,',
                  style: AppTextStyles.textView13ssp(),
                ),
                Text(
                  loginData.name.isNotEmpty ? loginData.name : 'Welcome to Zarooori',
                  style: AppTextStyles.textView13ssp(),
                ),
              ],
            ),
          ),
          IconButton(
            icon: const Icon(Icons.notifications, color: AppColors.black),
            onPressed: () {},
          ),
          IconButton(
            icon: const Icon(Icons.currency_rupee, color: AppColors.black),
            onPressed: () {},
          ),
          IconButton(
            icon: const Icon(Icons.language, color: AppColors.black),
            onPressed: () {},
          ),
        ],
      ),
    );
  }

  Widget _buildDrawer(loginData) {
    return Drawer(
      backgroundColor: AppColors.yellow,
      child: Column(
        children: [
          // Drawer Header
          Container(
            height: 80,
            padding: const EdgeInsets.all(10),
            color: AppColors.yellow,
            child: Row(
              children: [
                const Text(
                  'Zarooori',
                  style: TextStyle(
                    fontSize: 20,
                    fontWeight: FontWeight.bold,
                    color: AppColors.black,
                  ),
                ),
                const Spacer(),
                IconButton(
                  icon: const Icon(Icons.close, color: AppColors.black),
                  onPressed: _closeDrawer,
                ),
              ],
            ),
          ),

          // Drawer Menu Items
          Expanded(
            child: ListView(
              padding: EdgeInsets.zero,
              children: [
                _buildDrawerItem('Profile', Icons.person, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Booking List', Icons.book, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('My Packages', Icons.inventory, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('About Us', Icons.info, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Contact Us', Icons.contact_mail, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Terms and Condition', Icons.description, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Privacy Policy', Icons.privacy_tip, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Help', Icons.help, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('FAQ\'s', Icons.help_outline, () {}),
                const Divider(color: AppColors.black, height: 1),
                _buildDrawerItem('Logout', Icons.logout, _onLogout),
                const Divider(color: AppColors.black, height: 1),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildDrawerItem(String title, IconData icon, VoidCallback onTap) {
    return ListTile(
      leading: Icon(icon, color: AppColors.black),
      title: Text(
        title,
        style: const TextStyle(color: AppColors.black),
      ),
      onTap: onTap,
    );
  }

  Widget _buildOurServicesSection() {
    return Container(
      margin: const EdgeInsets.all(10),
      padding: const EdgeInsets.all(10),
      decoration: BoxDecoration(
        color: AppColors.white,
        borderRadius: BorderRadius.circular(10),
        boxShadow: [
          BoxShadow(
            color: Colors.black.withOpacity(0.1),
            blurRadius: 10,
            offset: const Offset(0, 5),
          ),
        ],
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Please Select your Domain from below list.',
            style: TextStyle(
              color: AppColors.orange,
              fontSize: 14,
              fontWeight: FontWeight.w600,
            ),
          ),
          const SizedBox(height: 10),
          // TODO: Implement GridView with all service categories
          // This is a placeholder - needs full implementation based on row_our_service_list.xml
          GridView.count(
            shrinkWrap: true,
            physics: const NeverScrollableScrollPhysics(),
            crossAxisCount: 2,
            crossAxisSpacing: 0.5,
            mainAxisSpacing: 0.5,
            childAspectRatio: 1.2,
            children: [
              _buildServiceCard('Sell Vehicle', '1'),
              _buildServiceCard('Buy Vehicle', '2'),
              _buildServiceCard('Emergency Service', '3'),
              _buildServiceCard('Vehicle Insurance', '4'),
              _buildServiceCard('Spare Parts', '5'),
              _buildServiceCard('Car Accessories', '6'),
              _buildServiceCard('Garage', '7'),
              _buildServiceCard('Hire Heavy Equipment', '8'),
              _buildServiceCard('Tyre Services', '9'),
              _buildServiceCard('Breakdown', '10'),
              _buildServiceCard('Rent a Car', '11'),
              _buildServiceCard('Courier', '12'),
            ],
          ),
        ],
      ),
    );
  }

  Widget _buildServiceCard(String title, String categoryId) {
    return GestureDetector(
      onTap: () => _onCategoryClick(categoryId),
      child: Container(
        color: AppColors.yellow,
        padding: const EdgeInsets.all(12),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Expanded(
              child: Container(
                width: double.infinity,
                decoration: BoxDecoration(
                  color: AppColors.gray,
                  borderRadius: BorderRadius.circular(10),
                ),
                child: const Icon(Icons.image, size: 50),
              ),
            ),
            const SizedBox(height: 12),
            Text(
              title,
              textAlign: TextAlign.center,
              style: const TextStyle(
                color: AppColors.black,
                fontSize: 9,
                fontWeight: FontWeight.w600,
              ),
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
              decoration: BoxDecoration(
                color: AppColors.green,
                borderRadius: BorderRadius.circular(15),
              ),
              child: Text(
                title.toUpperCase(),
                style: const TextStyle(
                  color: AppColors.yellow,
                  fontSize: 9,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
