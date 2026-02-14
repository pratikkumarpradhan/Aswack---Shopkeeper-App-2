import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import '../utils/strings.dart';
import 'login_screen.dart';
import 'profile_screen.dart';
import 'booking_list_screen.dart';
import 'about_us_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  bool _isAllFabsVisible = false;

  void _openDrawer() => _scaffoldKey.currentState?.openDrawer();
  void _closeDrawer() => _scaffoldKey.currentState?.closeDrawer();

  void _onCategoryTap(String categoryId) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Category $categoryId - Phase 2')),
    );
  }

  void _onLogout() async {
    await Helper.clearLoginData();
    if (!mounted) return;
    Navigator.of(context).pushAndRemoveUntil(
      MaterialPageRoute(builder: (_) => const LoginScreen()),
      (route) => false,
    );
  }

  void _callAdmin() async {
    final uri = Uri.parse('tel:+917635932119');
    if (await canLaunchUrl(uri)) await launchUrl(uri);
  }

  void _navigateTo(Widget screen) {
    _closeDrawer();
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => screen),
    );
  }

  @override
  Widget build(BuildContext context) {
    final loginData = Helper.getLoginData();
    final userName =
        loginData.name.isNotEmpty ? loginData.name : 'User';

    return Scaffold(
      key: _scaffoldKey,
      backgroundColor: AppColors.yellow,
      drawer: Drawer(
        backgroundColor: AppColors.yellow,
        child: Column(
          children: [
            Container(
              height: 80,
              padding: const EdgeInsets.all(10),
              color: AppColors.yellow,
              alignment: Alignment.centerLeft,
              child: Text(
                Strings.app_name,
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                  color: AppColors.black,
                ),
              ),
            ),
            Expanded(
              child: ListView(
                padding: const EdgeInsets.all(10),
                children: [
                  _drawerItem(Strings.profile, Icons.person, () => _navigateTo(const ProfileScreen())),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.booking_list, Icons.book, () => _navigateTo(const BookingListScreen())),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.my_packages, Icons.inventory, () => _navigateTo(const _PlaceholderScreen('My Packages'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.about_us, Icons.info, () => _navigateTo(const AboutUsScreen())),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.contact_us, Icons.contact_mail, () => _navigateTo(const _PlaceholderScreen('Contact Us'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.terms_condition, Icons.description, () => _navigateTo(const _PlaceholderScreen('Terms'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.privacy_policy, Icons.privacy_tip, () => _navigateTo(const _PlaceholderScreen('Privacy'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.help, Icons.help, () => _navigateTo(const _PlaceholderScreen('Help'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.faq, Icons.help_outline, () => _navigateTo(const _PlaceholderScreen('FAQ'))),
                  const Divider(height: 1, color: AppColors.black),
                  _drawerItem(Strings.logout, Icons.logout, _onLogout),
                  const Divider(height: 1, color: AppColors.black),
                ],
              ),
            ),
          ],
        ),
      ),
      body: Stack(
        children: [
          Column(
            children: [
              _buildHeader(userName),
              Expanded(
                child: SingleChildScrollView(
                  child: Column(
                    children: [
                      _buildMainImage(),
                      const SizedBox(height: 10),
                      _buildSlider(),
                      const SizedBox(height: 10),
                      _buildCategoryGrid(),
                      const SizedBox(height: 80),
                    ],
                  ),
                ),
              ),
            ],
          ),
          Positioned(
            bottom: 16,
            right: 16,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                if (_isAllFabsVisible) ...[
                  InkWell(
                    onTap: _callAdmin,
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 6,
                      ),
                      margin: const EdgeInsets.only(bottom: 5),
                      decoration: BoxDecoration(
                        color: AppColors.orange,
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: Text(
                        Strings.admin_details,
                        style: const TextStyle(
                          color: AppColors.yellow,
                          fontSize: 11,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ),
                  const SizedBox(height: 5),
                  FloatingActionButton(
                    heroTag: 'call',
                    mini: true,
                    backgroundColor: AppColors.black,
                    onPressed: _callAdmin,
                    child: const Icon(Icons.call, color: AppColors.yellow, size: 18),
                  ),
                  const SizedBox(height: 5),
                ],
                FloatingActionButton(
                  heroTag: 'add',
                  backgroundColor: AppColors.yellow,
                  onPressed: () {
                    setState(() => _isAllFabsVisible = !_isAllFabsVisible);
                  },
                  child: Icon(
                    _isAllFabsVisible ? Icons.close : Icons.add,
                    color: AppColors.black,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildHeader(String userName) {
    return Container(
      margin: const EdgeInsets.all(10),
      padding: const EdgeInsets.all(5),
      decoration: BoxDecoration(
        color: AppColors.yellow,
        borderRadius: BorderRadius.circular(5),
        boxShadow: const [
          BoxShadow(
            color: Colors.black12,
            blurRadius: 10,
            offset: Offset(0, 2),
          ),
        ],
      ),
      child: Row(
        children: [
          GestureDetector(
            onTap: _openDrawer,
            child: const Icon(Icons.menu, color: AppColors.black, size: 30),
          ),
          const SizedBox(width: 5),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: [
                Text(
                  '${Strings.hello} $userName',
                  style: const TextStyle(
                    color: AppColors.black,
                    fontSize: 12,
                  ),
                ),
                Text(
                  Strings.welcome_to_zarooori,
                  style: const TextStyle(
                    color: AppColors.black,
                    fontSize: 10,
                  ),
                ),
              ],
            ),
          ),
          IconButton(
            icon: const Icon(Icons.notifications, color: AppColors.black, size: 20),
            onPressed: () {},
            padding: EdgeInsets.zero,
            constraints: const BoxConstraints(),
          ),
          const SizedBox(width: 5),
          IconButton(
            icon: const Icon(Icons.currency_rupee, color: AppColors.black, size: 20),
            onPressed: () {},
            padding: EdgeInsets.zero,
            constraints: const BoxConstraints(),
          ),
          const SizedBox(width: 5),
          IconButton(
            icon: const Icon(Icons.language, color: AppColors.black, size: 20),
            onPressed: () {},
            padding: EdgeInsets.zero,
            constraints: const BoxConstraints(),
          ),
        ],
      ),
    );
  }

  Widget _buildMainImage() {
    return Image.asset(
      'assets/images/main_image.png',
      width: double.infinity,
      height: 250,
      fit: BoxFit.cover,
      errorBuilder: (_, __, ___) => Container(
        height: 250,
        width: double.infinity,
        color: AppColors.viewColor,
        child: const Icon(Icons.image, size: 60),
      ),
    );
  }

  Widget _buildSlider() {
    return SizedBox(
      height: 120,
      width: double.infinity,
      child: PageView.builder(
        itemCount: 2,
        itemBuilder: (_, i) => Container(
          color: AppColors.viewColor,
          child: const Icon(Icons.image, size: 48),
        ),
      ),
    );
  }

  Widget _buildCategoryGrid() {
    const categories = [
      (Strings.sell_vehicle, '1'),
      (Strings.buy_vehicle, '2'),
      (Strings.emergency_services, '5'),
      (Strings.vehicle_insurance, '4'),
      (Strings.spare_parts, '6'),
      (Strings.courier_services, '12'),
      (Strings.garages, '3'),
      (Strings.car_accessories, '7'),
      (Strings.heavy_equipments, '8'),
      (Strings.tyre_servicess, '9'),
      (Strings.break_down_services, '10'),
      (Strings.cars_for_rent, '11'),
    ];
    return Container(
      margin: const EdgeInsets.all(10),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: AppColors.yellow,
        borderRadius: BorderRadius.circular(5),
      ),
      child: Column(
        children: [
          Text(
            Strings.our_services,
            style: const TextStyle(
              color: AppColors.orange,
              fontSize: 14,
              fontWeight: FontWeight.w600,
            ),
          ),
          const SizedBox(height: 20),
          Row(
            children: [
              Expanded(child: _categoryCard(categories[0].$1, categories[0].$2)),
              Container(width: 0.5, color: AppColors.black, height: 120),
              Expanded(child: _categoryCard(categories[1].$1, categories[1].$2)),
            ],
          ),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          _categoryCardFull(categories[2].$1, categories[2].$2),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          Row(
            children: [
              Expanded(child: _categoryCard(categories[3].$1, categories[3].$2)),
              Container(width: 0.5, color: AppColors.black, height: 120),
              Expanded(child: _categoryCard(categories[4].$1, categories[4].$2)),
            ],
          ),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          _categoryCardFull(categories[5].$1, categories[5].$2),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          Row(
            children: [
              Expanded(child: _categoryCard(categories[6].$1, categories[6].$2)),
              Container(width: 0.5, color: AppColors.black, height: 120),
              Expanded(child: _categoryCard(categories[7].$1, categories[7].$2)),
            ],
          ),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          Row(
            children: [
              Expanded(child: _categoryCard(categories[8].$1, categories[8].$2)),
              Container(width: 0.5, color: AppColors.black, height: 120),
              Expanded(child: _categoryCard(categories[9].$1, categories[9].$2)),
            ],
          ),
          Container(height: 0.5, color: AppColors.black, width: double.infinity),
          Row(
            children: [
              Expanded(child: _categoryCard(categories[10].$1, categories[10].$2)),
              Container(width: 0.5, color: AppColors.black, height: 120),
              Expanded(child: _categoryCard(categories[11].$1, categories[11].$2)),
            ],
          ),
        ],
      ),
    );
  }

  Widget _categoryCard(String title, String id) {
    return GestureDetector(
      onTap: () => _onCategoryTap(id),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          children: [
            Container(
              height: 70,
              width: double.infinity,
              decoration: BoxDecoration(
                color: AppColors.viewColor,
                borderRadius: BorderRadius.circular(10),
              ),
              child: const Icon(Icons.directions_car, size: 36),
            ),
            const SizedBox(height: 12),
            Text(
              title,
              style: const TextStyle(
                color: AppColors.black,
                fontSize: 9,
                fontWeight: FontWeight.w600,
              ),
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
              decoration: BoxDecoration(
                color: AppColors.green,
                borderRadius: BorderRadius.circular(8),
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

  Widget _categoryCardFull(String title, String id) {
    return GestureDetector(
      onTap: () => _onCategoryTap(id),
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: Column(
          children: [
            Container(
              height: 140,
              width: double.infinity,
              decoration: BoxDecoration(
                color: AppColors.viewColor,
                borderRadius: BorderRadius.circular(10),
              ),
              child: const Icon(Icons.directions_car, size: 48),
            ),
            const SizedBox(height: 12),
            Text(
              title,
              style: const TextStyle(
                color: AppColors.black,
                fontSize: 9,
                fontWeight: FontWeight.w600,
              ),
              textAlign: TextAlign.center,
              maxLines: 2,
              overflow: TextOverflow.ellipsis,
            ),
            const SizedBox(height: 12),
            Container(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
              decoration: BoxDecoration(
                color: AppColors.green,
                borderRadius: BorderRadius.circular(8),
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

  Widget _drawerItem(String title, IconData icon, VoidCallback onTap) {
    return ListTile(
      leading: Icon(icon, color: AppColors.black, size: 24),
      title: Text(
        title,
        style: const TextStyle(color: AppColors.black, fontSize: 14),
      ),
      onTap: onTap,
    );
  }
}

class _PlaceholderScreen extends StatelessWidget {
  final String title;

  const _PlaceholderScreen(this.title);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.yellow,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(
          title,
          style: const TextStyle(color: AppColors.black, fontSize: 16),
        ),
      ),
      body: Center(
        child: Text(
          title,
          style: const TextStyle(color: AppColors.black, fontSize: 18),
        ),
      ),
    );
  }
}
