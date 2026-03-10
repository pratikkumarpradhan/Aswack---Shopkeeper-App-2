import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/product_item.dart';
import '../services/api_service.dart';

class EmergencyServiceDetailScreen extends StatelessWidget {
  final ProductItem item;

  const EmergencyServiceDetailScreen({super.key, required this.item});

  List<String> _getImageUrls() {
    final list = <String>[];
    for (final img in [
      item.image1,
      item.image2,
      item.image3,
      item.image4,
      item.image5,
      item.image6,
      item.image7,
    ]) {
      final resolved = ApiService.resolveImageUrl(img);
      if (resolved.isNotEmpty) list.add(resolved);
    }
    return list;
  }

  Future<void> _callAdmin(BuildContext context) async {
    // Match existing HomeActivity admin call behaviour
    const number = '+917635932119';
    final uri = Uri.parse('tel:$number');
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri);
    } else {
      if (context.mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Unable to start call')),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final images = _getImageUrls();

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        backgroundColor: AppColors.yellow,
        foregroundColor: AppColors.black,
        title: Text(
          item.productName != null && item.productName!.isNotEmpty
              ? '${item.productName} details'
              : 'Emergency Service Details',
          style: const TextStyle(color: AppColors.black),
        ),
        leading: IconButton(
          icon: const Icon(Icons.arrow_back),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              height: 250,
              child: images.isEmpty
                  ? Container(
                      color: AppColors.gray.withValues(alpha: 0.3),
                      child: const Center(
                        child: Icon(Icons.miscellaneous_services, size: 80),
                      ),
                    )
                  : PageView.builder(
                      itemCount: images.length,
                      itemBuilder: (context, index) {
                        return Image.network(
                          images[index],
                          fit: BoxFit.cover,
                          errorBuilder: (_, __, ___) => Container(
                            color: AppColors.gray.withValues(alpha: 0.3),
                            child: const Icon(
                              Icons.miscellaneous_services,
                              size: 80,
                            ),
                          ),
                        );
                      },
                    ),
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    item.productName ?? 'Emergency Service',
                    style: AppTextStyles.textView18ssp().copyWith(
                      fontWeight: FontWeight.bold,
                      color: AppColors.purple700,
                    ),
                  ),
                  if (item.productCode != null &&
                      item.productCode!.isNotEmpty)
                    Text(
                      'Code: ${item.productCode}',
                      style: AppTextStyles.textView13ssp()
                          .copyWith(color: AppColors.textColor),
                    ),
                  const SizedBox(height: 12),
                  if (item.price != null && item.price!.isNotEmpty)
                    Text(
                      '₹${item.price}',
                      style: AppTextStyles.textView18ssp().copyWith(
                        fontWeight: FontWeight.bold,
                        color: AppColors.red,
                      ),
                    ),
                  const SizedBox(height: 16),
                  if (item.vehicleCatName != null &&
                      item.vehicleCatName!.isNotEmpty)
                    _buildDetailRow('Category', item.vehicleCatName!),
                  if (item.vehicleCompanyName != null &&
                      item.vehicleCompanyName!.isNotEmpty)
                    _buildDetailRow('Brand', item.vehicleCompanyName!),
                  if (item.vehicleModelName != null &&
                      item.vehicleModelName!.isNotEmpty)
                    _buildDetailRow('Model', item.vehicleModelName!),
                  if (item.createdDatetime != null &&
                      item.createdDatetime!.isNotEmpty)
                    _buildDetailRow('Posted', item.createdDatetime!),
                  if (item.specializeIn != null &&
                      item.specializeIn!.isNotEmpty)
                    _buildDetailRow('Specialize in', item.specializeIn!),
                  const SizedBox(height: 12),
                  if (item.description != null &&
                      item.description!.isNotEmpty) ...[
                    Text(
                      'Description',
                      style: AppTextStyles.textView13ssp().copyWith(
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    const SizedBox(height: 4),
                    Text(
                      item.description!,
                      style: AppTextStyles.textView13ssp(),
                    ),
                    const SizedBox(height: 16),
                  ],
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      onPressed: () => _callAdmin(context),
                      icon: const Icon(Icons.call),
                      label: Text(
                        'Call Admin for Request',
                        style: AppTextStyles.textView15ssp().copyWith(
                          color: Colors.white,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppColors.green,
                        foregroundColor: Colors.white,
                        padding:
                            const EdgeInsets.symmetric(vertical: 14),
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDetailRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 110,
            child: Text(
              '$label:',
              style: AppTextStyles.textView13ssp().copyWith(
                fontWeight: FontWeight.w600,
                color: AppColors.textColor,
              ),
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: AppTextStyles.textView13ssp(),
            ),
          ),
        ],
      ),
    );
  }
}

