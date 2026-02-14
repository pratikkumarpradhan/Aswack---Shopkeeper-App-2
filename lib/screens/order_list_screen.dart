import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

class OrderListScreen extends StatefulWidget {
  final String? companyId;

  const OrderListScreen({
    super.key,
    this.companyId,
  });

  @override
  State<OrderListScreen> createState() => _OrderListScreenState();
}

class _OrderListScreenState extends State<OrderListScreen> {
  bool _isLoading = false;
  List<Map<String, dynamic>> _orders = [];

  @override
  void initState() {
    super.initState();
    _loadOrders();
  }

  void _loadOrders() {
    setState(() => _isLoading = true);
    // TODO: API / Firebase fetch orders
    Future.delayed(const Duration(seconds: 1), () {
      if (mounted) {
        setState(() {
          _isLoading = false;
          _orders = [];
        });
      }
    });
  }

  void _onOrderSelected(Map<String, dynamic> order) {
    // TODO: Navigate to OrderDetailsScreen
  }

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
        title: const Text(
          'My Orders',
          style: TextStyle(
            color: AppColors.black,
            fontSize: 13,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      body: Column(
        children: [
          Container(
            height: 1,
            color: AppColors.viewColor,
          ),
          Expanded(
            child: _isLoading
                ? const Center(child: CircularProgressIndicator())
                : _orders.isEmpty
                    ? Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            Icon(Icons.receipt_long, size: 64, color: AppColors.gray),
                            const SizedBox(height: 16),
                            Text(
                              'No orders found',
                              style: TextStyle(color: AppColors.gray, fontSize: 16),
                            ),
                          ],
                        ),
                      )
                    : ListView.builder(
                        padding: const EdgeInsets.all(10),
                        itemCount: _orders.length,
                        itemBuilder: (context, index) {
                          final order = _orders[index];
                          return Card(
                            margin: const EdgeInsets.only(bottom: 10),
                            child: ListTile(
                              title: Text(order['title'] ?? 'Order ${index + 1}'),
                              subtitle: Text(order['date'] ?? ''),
                              trailing: const Icon(Icons.arrow_forward_ios),
                              onTap: () => _onOrderSelected(order),
                            ),
                          );
                        },
                      ),
          ),
        ],
      ),
    );
  }
}
