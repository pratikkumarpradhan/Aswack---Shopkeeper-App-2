import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';
import '../models/sell_vehicle.dart';

class SellVehicleDetailScreen extends StatelessWidget {
  final SellVehicle vehicle;

  const SellVehicleDetailScreen({Key? key, required this.vehicle})
      : super(key: key);

  List<String> _getImageUrls() {
    final list = <String>[];
    for (final img in [
      vehicle.image1,
      vehicle.image2,
      vehicle.image3,
      vehicle.image4,
      vehicle.image5,
      vehicle.image6,
      vehicle.image7,
    ]) {
      if (img != null && img.isNotEmpty) list.add(img);
    }
    return list;
  }

  Future<void> _callSeller(BuildContext context) async {
    final number = vehicle.contactNumber ?? '';
    if (number.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Contact number not available')),
      );
      return;
    }
    final uri = Uri.parse('tel:$number');
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri);
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
        title: const Text(
          'Vehicle Details',
          style: TextStyle(color: AppColors.black),
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
                      color: AppColors.gray.withOpacity(0.3),
                      child: const Center(
                        child: Icon(Icons.directions_car, size: 80),
                      ),
                    )
                  : PageView.builder(
                      itemCount: images.length,
                      itemBuilder: (context, index) => Image.network(
                        images[index],
                        fit: BoxFit.cover,
                        errorBuilder: (_, __, ___) => Container(
                          color: AppColors.gray.withOpacity(0.3),
                          child: const Icon(Icons.directions_car, size: 80),
                        ),
                      ),
                    ),
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    vehicle.title ?? 'Vehicle',
                    style: AppTextStyles.textView18ssp().copyWith(
                        fontWeight: FontWeight.bold,
                        color: AppColors.purple700),
                  ),
                  if (vehicle.advertisementCode != null)
                    Text('Ad no.: ${vehicle.advertisementCode}',
                        style: AppTextStyles.textView13ssp()
                            .copyWith(color: AppColors.textColor)),
                  const SizedBox(height: 12),
                  Text(
                    '₹${vehicle.price ?? '0'}',
                    style: AppTextStyles.textView18ssp().copyWith(
                        fontWeight: FontWeight.bold, color: AppColors.red),
                  ),
                  const SizedBox(height: 16),
                  _buildRow('Model',
                      '${vehicle.vehicleModelName ?? ''} (${vehicle.vehicleYearName ?? ''})'),
                  _buildRow('Brand & Type',
                      '${vehicle.vehicleBrandName ?? ''}, ${vehicle.vehicleTypeName ?? ''}'),
                  _buildRow('Fuel', vehicle.vehicleFuelName ?? '-'),
                  _buildRow('Km Driven', '${vehicle.drivenKm ?? '-'} km'),
                  _buildRow('Owners', vehicle.owners ?? '-'),
                  if (vehicle.description != null &&
                      vehicle.description!.isNotEmpty) ...[
                    const SizedBox(height: 12),
                    Text('Description',
                        style: AppTextStyles.textView13ssp()
                            .copyWith(fontWeight: FontWeight.w600)),
                    const SizedBox(height: 4),
                    Text(vehicle.description!,
                        style: AppTextStyles.textView13ssp()),
                  ],
                  const SizedBox(height: 16),
                  SizedBox(
                    width: double.infinity,
                    child: ElevatedButton.icon(
                      onPressed: () => _callSeller(context),
                      icon: const Icon(Icons.call),
                      label: Text(
                        'Call ${vehicle.contactNumber ?? 'Seller'}',
                        style: AppTextStyles.textView15ssp()
                            .copyWith(
                                color: Colors.white,
                                fontWeight: FontWeight.w600),
                      ),
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppColors.green,
                        foregroundColor: Colors.white,
                        padding: const EdgeInsets.symmetric(vertical: 14),
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

  Widget _buildRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 8),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 100,
            child: Text(
              '$label:',
              style: AppTextStyles.textView13ssp().copyWith(
                  fontWeight: FontWeight.w600,
                  color: AppColors.textColor),
            ),
          ),
          Expanded(
            child: Text(value, style: AppTextStyles.textView13ssp()),
          ),
        ],
      ),
    );
  }
}
