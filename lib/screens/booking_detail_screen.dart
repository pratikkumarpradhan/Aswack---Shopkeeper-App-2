import 'package:flutter/material.dart';
import '../utils/app_colors.dart';

class BookingDetailScreen extends StatefulWidget {
  final String? bookingId;

  const BookingDetailScreen({
    Key? key,
    this.bookingId,
  }) : super(key: key);

  @override
  State<BookingDetailScreen> createState() => _BookingDetailScreenState();
}

class _BookingDetailScreenState extends State<BookingDetailScreen> {
  bool _isLoading = false;
  bool _showRejectReason = false;
  final TextEditingController _rejectReasonController = TextEditingController();

  // Sample data - replace with actual booking data
  String _customerName = 'Customer Name';
  String _mobile = 'Jaydeep Rana';
  String _bookingCode = '-';
  String _modelName = '-';
  String _brandName = '-';
  String _vehicleType = '-';
  String _vehicleNumber = '-';
  String _appointmentDate = '-';
  String _appointmentTime = '-';
  String _bookingStatus = 'Pending';

  @override
  void dispose() {
    _rejectReasonController.dispose();
    super.dispose();
  }

  void _onAccept() {
    // TODO: API call to accept booking
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Booking accepted')),
    );
  }

  void _onReject() {
    setState(() => _showRejectReason = true);
  }

  void _onSendReject() {
    if (_rejectReasonController.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Please enter reason for rejection')),
      );
      return;
    }
    // TODO: API call to reject booking
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Booking rejected')),
    );
    Navigator.pop(context);
  }

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
        title: const Text(
          'Booking Detail',
          style: TextStyle(
            color: AppColors.black,
            fontSize: 13,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
              padding: const EdgeInsets.all(10),
              child: Card(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                child: Padding(
                  padding: const EdgeInsets.all(10),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        _customerName,
                        style: const TextStyle(
                          color: AppColors.red,
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      const SizedBox(height: 5),
                      Text(
                        _mobile,
                        style: TextStyle(
                          color: AppColors.purple700,
                          fontSize: 12,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      const SizedBox(height: 10),
                      _buildRow('Appointment Code :', _bookingCode),
                      const SizedBox(height: 15),
                      const Text(
                        'Vehicle Details',
                        style: TextStyle(
                          color: AppColors.red,
                          fontSize: 13,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      _buildRow('Model Name :', _modelName),
                      _buildRow('Brand Name :', _brandName),
                      _buildRow('Vehicle Type :', _vehicleType),
                      _buildRow('Vehicle Number :', _vehicleNumber),
                      _buildRow('Appintment Date :', _appointmentDate),
                      _buildRow('Appintment Time :', _appointmentTime),
                      _buildRow('Booking Approval :', _bookingStatus),
                      const SizedBox(height: 15),
                      Row(
                        children: [
                          ElevatedButton(
                            onPressed: _onAccept,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: AppColors.purple700,
                              foregroundColor: AppColors.white,
                              padding: const EdgeInsets.symmetric(
                                horizontal: 25,
                                vertical: 8,
                              ),
                            ),
                            child: const Text('Accept'),
                          ),
                          const SizedBox(width: 10),
                          ElevatedButton(
                            onPressed: _onReject,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: AppColors.purple700,
                              foregroundColor: AppColors.white,
                              padding: const EdgeInsets.symmetric(
                                horizontal: 25,
                                vertical: 8,
                              ),
                            ),
                            child: const Text('Reject'),
                          ),
                        ],
                      ),
                      if (_showRejectReason) ...[
                        const SizedBox(height: 10),
                        TextField(
                          controller: _rejectReasonController,
                          decoration: const InputDecoration(
                            hintText: 'Reason for rejection',
                            border: OutlineInputBorder(),
                          ),
                          maxLines: 2,
                        ),
                        const SizedBox(height: 10),
                        SizedBox(
                          width: double.infinity,
                          child: ElevatedButton(
                            onPressed: _onSendReject,
                            style: ElevatedButton.styleFrom(
                              backgroundColor: AppColors.purple700,
                              foregroundColor: AppColors.white,
                              padding: const EdgeInsets.symmetric(
                                horizontal: 25,
                                vertical: 8,
                              ),
                            ),
                            child: const Text('Send'),
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
              ),
            ),
    );
  }

  Widget _buildRow(String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(top: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          SizedBox(
            width: 140,
            child: Text(
              label,
              style: TextStyle(
                color: AppColors.textColor,
                fontSize: 11,
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
          Expanded(
            child: Text(
              value,
              style: TextStyle(
                color: AppColors.purple700,
                fontSize: 11,
                fontWeight: FontWeight.w600,
              ),
            ),
          ),
        ],
      ),
    );
  }
}
