import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../utils/app_colors.dart';
import '../utils/helper.dart';
import '../models/product.dart';

class GarageServiceListScreen extends StatefulWidget {
  final String title;
  final String mainCatId;
  final String? companyId;
  final String? packageId;

  const GarageServiceListScreen({
    super.key,
    required this.title,
    required this.mainCatId,
    this.companyId,
    this.packageId,
  });

  @override
  State<GarageServiceListScreen> createState() => _GarageServiceListScreenState();
}

class _GarageServiceListScreenState extends State<GarageServiceListScreen> {
  bool _isLoading = false;
  String? _error;
  List<Product> _products = [];
  String _searchQuery = '';

  @override
  void initState() {
    super.initState();
    _loadProducts();
  }

  Future<void> _loadProducts() async {
    setState(() {
      _isLoading = true;
      _error = null;
    });

    final loginData = Helper.getLoginData();
    final sellerId = loginData.id;

    if (sellerId.isEmpty) {
      setState(() {
        _isLoading = false;
        _error = 'Please login again to view your services.';
      });
      return;
    }

    final response = await ApiService.getProductList(
      masterCategoryId: widget.mainCatId,
      sellerId: sellerId,
      companySellerId: widget.companyId ?? '',
      packagePurchasedId: widget.packageId ?? '',
    );

    if (!mounted) return;

    if (!response.status) {
      setState(() {
        _isLoading = false;
        _error = response.message.isNotEmpty ? response.message : 'Failed to load services.';
      });
      return;
    }

    final list = response.getProductList();
    setState(() {
      _isLoading = false;
      _products = list;
    });
  }

  List<Product> get _filteredProducts {
    if (_searchQuery.trim().isEmpty) return _products;
    final q = _searchQuery.toLowerCase();
    return _products.where((p) {
      return (p.productName ?? '').toLowerCase().contains(q) ||
          (p.vehicleCompanyName ?? '').toLowerCase().contains(q) ||
          (p.vehicleModelName ?? '').toLowerCase().contains(q) ||
          (p.vehicleTypeName ?? '').toLowerCase().contains(q) ||
          (p.serialNumber ?? '').toLowerCase().contains(q) ||
          (p.price ?? '').toLowerCase().contains(q);
    }).toList();
  }

  @override
  Widget build(BuildContext context) {
    final items = _filteredProducts;

    return Scaffold(
      backgroundColor: AppColors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.black),
          onPressed: () => Navigator.pop(context),
        ),
        title: Text(
          widget.title,
          style: const TextStyle(
            color: AppColors.black,
            fontSize: 16,
            fontWeight: FontWeight.w600,
          ),
        ),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh, color: AppColors.black),
            onPressed: _loadProducts,
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
            child: TextField(
              decoration: InputDecoration(
                hintText: 'Search services',
                prefixIcon: const Icon(Icons.search),
                filled: true,
                fillColor: AppColors.bgEditText,
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8),
                  borderSide: BorderSide.none,
                ),
                contentPadding: const EdgeInsets.symmetric(vertical: 0, horizontal: 8),
              ),
              onChanged: (value) {
                setState(() => _searchQuery = value);
              },
            ),
          ),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Products: ${items.length}',
                  style: const TextStyle(
                    color: AppColors.black,
                    fontSize: 13,
                    fontWeight: FontWeight.w500,
                  ),
                ),
                if (_isLoading)
                  const SizedBox(
                    height: 16,
                    width: 16,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  ),
              ],
            ),
          ),
          const Divider(height: 1),
          Expanded(
            child: _buildBody(items),
          ),
        ],
      ),
    );
  }

  Widget _buildBody(List<Product> items) {
    if (_isLoading && items.isEmpty && _error == null) {
      return const Center(child: CircularProgressIndicator());
    }
    if (_error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.error_outline, size: 40, color: AppColors.gray),
              const SizedBox(height: 12),
              Text(
                _error!,
                style: const TextStyle(color: AppColors.gray, fontSize: 14),
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: _loadProducts,
                style: ElevatedButton.styleFrom(
                  backgroundColor: AppColors.yellow,
                  foregroundColor: AppColors.black,
                ),
                child: const Text('Retry'),
              ),
            ],
          ),
        ),
      );
    }
    if (items.isEmpty) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: const [
              Icon(Icons.inventory_2, size: 40, color: AppColors.gray),
              SizedBox(height: 12),
              Text(
                'No products found.',
                style: TextStyle(color: AppColors.gray, fontSize: 14),
                textAlign: TextAlign.center,
              ),
            ],
          ),
        ),
      );
    }

    return Padding(
      padding: const EdgeInsets.all(8),
      child: GridView.builder(
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          crossAxisSpacing: 8,
          mainAxisSpacing: 8,
          childAspectRatio: 0.72,
        ),
        itemCount: items.length,
        itemBuilder: (context, index) {
          final p = items[index];
          return _buildProductCard(p);
        },
      ),
    );
  }

  Widget _buildProductCard(Product p) {
    final imageUrl = p.image1 ?? '';
    final priceText = (p.price ?? '').isNotEmpty ? '₹${p.price}' : '';

    return GestureDetector(
      onTap: () => _showProductDetail(p),
      child: Container(
        decoration: BoxDecoration(
          color: AppColors.bgEditText,
          borderRadius: BorderRadius.circular(10),
          border: Border.all(color: AppColors.viewColor),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            AspectRatio(
              aspectRatio: 1.4,
              child: ClipRRect(
                borderRadius: const BorderRadius.vertical(top: Radius.circular(10)),
                child: imageUrl.isNotEmpty
                    ? Image.network(
                        imageUrl,
                        fit: BoxFit.cover,
                        errorBuilder: (_, __, ___) => Container(
                          color: AppColors.viewColor,
                          child: const Icon(Icons.image, size: 40, color: AppColors.gray),
                        ),
                      )
                    : Container(
                        color: AppColors.viewColor,
                        child: const Icon(Icons.image, size: 40, color: AppColors.gray),
                      ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(8, 8, 8, 2),
              child: Text(
                p.productName ?? '',
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  color: AppColors.black,
                  fontSize: 13,
                  fontWeight: FontWeight.w600,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              child: Text(
                p.productCode ?? '',
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  color: AppColors.gray,
                  fontSize: 11,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
              child: Text(
                priceText,
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  color: AppColors.purple700,
                  fontSize: 13,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.fromLTRB(8, 2, 8, 6),
              child: Text(
                p.createdDatetime != null && p.createdDatetime!.isNotEmpty
                    ? 'Posted on: ${p.createdDatetime}'
                    : '',
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(
                  color: AppColors.gray,
                  fontSize: 10,
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _showProductDetail(Product p) {
    showDialog(
      context: context,
      builder: (context) {
        final imageUrl = p.image1 ?? '';
        final priceText = (p.price ?? '').isNotEmpty ? '₹${p.price}' : '';
        return Dialog(
          insetPadding: const EdgeInsets.all(16),
          child: SingleChildScrollView(
            child: Column(
              mainAxisSize: MainAxisSize.min,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                AspectRatio(
                  aspectRatio: 1.6,
                  child: imageUrl.isNotEmpty
                      ? Image.network(
                          imageUrl,
                          fit: BoxFit.cover,
                          errorBuilder: (_, __, ___) => Container(
                            color: AppColors.viewColor,
                            child: const Icon(Icons.image, size: 40, color: AppColors.gray),
                          ),
                        )
                      : Container(
                          color: AppColors.viewColor,
                          child: const Icon(Icons.image, size: 40, color: AppColors.gray),
                        ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(16, 12, 16, 4),
                  child: Text(
                    p.productName ?? '',
                    style: const TextStyle(
                      color: AppColors.black,
                      fontSize: 16,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                ),
                if ((p.productCode ?? '').isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 2),
                    child: Text(
                      'Code: ${p.productCode}',
                      style: const TextStyle(
                        color: AppColors.gray,
                        fontSize: 12,
                      ),
                    ),
                  ),
                if (priceText.isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 4),
                    child: Text(
                      priceText,
                      style: const TextStyle(
                        color: AppColors.purple700,
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                if ((p.description ?? '').isNotEmpty)
                  Padding(
                    padding: const EdgeInsets.fromLTRB(16, 4, 16, 12),
                    child: Text(
                      p.description ?? '',
                      style: const TextStyle(
                        color: AppColors.black,
                        fontSize: 13,
                      ),
                    ),
                  ),
                Align(
                  alignment: Alignment.centerRight,
                  child: TextButton(
                    onPressed: () => Navigator.pop(context),
                    child: const Text('Close'),
                  ),
                ),
              ],
            ),
          ),
        );
      },
    );
  }
}

