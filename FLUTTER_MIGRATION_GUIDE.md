# Flutter Migration Guide

## Project Structure Created

```
lib/
├── main.dart                    # App entry point
├── screens/
│   ├── splash_screen.dart      # ✅ Converted
│   ├── login_screen.dart       # ✅ Converted
│   ├── register_screen.dart    # ✅ Converted
│   └── home_screen.dart        # ✅ Converted (basic structure)
├── utils/
│   ├── app_colors.dart         # ✅ All colors extracted
│   ├── app_text_styles.dart    # ✅ Text styles defined
│   └── helper.dart             # ✅ Helper utilities
└── widgets/                    # For reusable widgets
```

## Completed Screens

1. **Splash Screen** - Complete with network check and navigation logic
2. **Login Screen** - Complete with country code picker and Firebase integration placeholder
3. **Register Screen** - Complete with validation and Firebase integration placeholder
4. **Home Screen** - Basic structure with drawer and service grid (needs full implementation)

## Remaining Screens to Convert (75+)

### Priority Order:

1. **Authentication Flow**
   - ForgotPasswordScreen
   - OTPForForgotPasswordScreen
   - VerifyOTPScreen

2. **Main Feature Screens**
   - DashBoardActivity → dashboard_screen.dart
   - SellVehicleListActivity → sell_vehicle_list_screen.dart
   - BuyVehicalActivity → buy_vehicle_screen.dart
   - SearchVehicleListActivity → search_vehicle_list_screen.dart
   - SearchVehicleDetailActivity → search_vehicle_detail_screen.dart

3. **Service Screens**
   - EmergencyServiceListActivity → emergency_service_list_screen.dart
   - GarageServiceListActivity → garage_service_list_screen.dart
   - SparePartsCarAccessoriesListActivity → spare_parts_list_screen.dart
   - TyreServiceListActivity → tyre_service_list_screen.dart
   - InsuranceProductsListActivity → insurance_products_list_screen.dart
   - CourierListActivity → courier_list_screen.dart

4. **Add/Edit Screens**
   - AddVehicleForSellActivity → add_vehicle_sell_screen.dart
   - AddGarageServiceActivity → add_garage_service_screen.dart
   - AddSparePartsCarAccesoriesActivity → add_spare_parts_screen.dart
   - AddTyreServiceActivity → add_tyre_service_screen.dart
   - AddInsuranceHeavyEquipRentCarActivity → add_insurance_screen.dart
   - AddCourierProductActivity → add_courier_product_screen.dart

5. **Detail Screens**
   - SellVehicleDetailActivity → sell_vehicle_detail_screen.dart
   - BookingDetailActivity → booking_detail_screen.dart
   - OrderDetailsActivity → order_details_screen.dart
   - PackageDetailsActivity → package_details_screen.dart

6. **Profile & Settings**
   - ProfileActivity → profile_screen.dart
   - UpdateProfileActivity → update_profile_screen.dart
   - CompanyProfileActivity → company_profile_screen.dart
   - UpdateCompanyProfileActivity → update_company_profile_screen.dart

7. **Other Screens**
   - BookingListActivity → booking_list_screen.dart
   - OrderListActivity → order_list_screen.dart
   - PackageListActivity → package_list_screen.dart
   - PurchasedPackageListActivity → purchased_package_list_screen.dart
   - ChatListActivity → chat_list_screen.dart
   - ChatActivity → chat_screen.dart
   - RFQListActivity → rfq_list_screen.dart
   - RFQDetailActivity → rfq_detail_screen.dart
   - AddQuotationActivity → add_quotation_screen.dart
   - QuotationDetailsActivity → quotation_details_screen.dart
   - PaymentActivity → payment_screen.dart
   - RatingAndReviewActivity → rating_review_screen.dart
   - MapActivity → map_screen.dart
   - WebViewActivity → webview_screen.dart
   - PageDetailActivity → page_detail_screen.dart
   - AdminNotificationActivity → admin_notification_screen.dart
   - SellerNotificationListActivity → seller_notification_list_screen.dart
   - AddNotificationActivity → add_notification_screen.dart
   - MyProductsActivity → my_products_screen.dart
   - AddProductActivity → add_product_screen.dart
   - MyOfferActivity → my_offer_screen.dart
   - AddOfferActivity → add_offer_screen.dart
   - UpdateOfferActivity → update_offer_screen.dart
   - OfferDetailActivity → offer_detail_screen.dart
   - ThankYouForRegistrationActivity → thank_you_registration_screen.dart
   - ThankYouForAddActivity → thank_you_add_screen.dart

## Conversion Pattern

For each screen, follow this pattern:

### 1. Read XML Layout
```bash
# Read the corresponding XML file
app/src/main/res/layout/activity_*.xml
```

### 2. Read Kotlin Activity
```bash
# Read the corresponding Kotlin file
app/src/main/java/.../activities/*Activity.kt
```

### 3. Create Flutter Screen
```dart
// lib/screens/[screen_name]_screen.dart
import 'package:flutter/material.dart';
import '../utils/app_colors.dart';
import '../utils/app_text_styles.dart';

class [ScreenName]Screen extends StatefulWidget {
  const [ScreenName]Screen({Key? key}) : super(key: key);

  @override
  State<[ScreenName]Screen> createState() => _[ScreenName]ScreenState();
}

class _[ScreenName]ScreenState extends State<[ScreenName]Screen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.yellow,
      // Convert XML layout to Flutter widgets
    );
  }
}
```

### 4. XML to Flutter Mapping

- `ConstraintLayout` → `Stack` + `Positioned` or `Column`/`Row` with `Expanded`
- `LinearLayout vertical` → `Column`
- `LinearLayout horizontal` → `Row`
- `TextView` → `Text`
- `EditText` → `TextField`
- `Button` → `ElevatedButton`/`TextButton`
- `ImageView` → `Image.asset()`
- `RecyclerView` → `ListView.builder` or `GridView.builder`
- `ScrollView` → `SingleChildScrollView`
- `CardView` → `Card`
- `setOnClickListener` → `onPressed`/`GestureDetector`

## Dependencies Needed

Add to `pubspec.yaml`:

```yaml
dependencies:
  flutter:
    sdk: flutter
  connectivity_plus: ^5.0.0
  country_code_picker: ^3.0.0
  firebase_core: ^2.24.0
  firebase_firestore: ^4.13.0
  firebase_messaging: ^14.7.0
  shared_preferences: ^2.2.0
  image_picker: ^1.0.0
  carousel_slider: ^5.0.0
  google_maps_flutter: ^2.5.0
  webview_flutter: ^4.4.0
  url_launcher: ^6.2.0
```

## Assets Setup

1. Copy all images from `app/src/main/res/drawable/` to `assets/images/`
2. Update `pubspec.yaml`:
```yaml
flutter:
  assets:
    - assets/images/
```

## Next Steps

1. Complete Home Screen implementation (full service grid with images)
2. Convert remaining screens one by one following the pattern
3. Implement navigation between screens
4. Add Firebase integration
5. Test each screen thoroughly

## Notes

- Maintain exact UI/UX - no redesigns
- Use Stack + Positioned for complex layouts to match Android positioning
- Extract reusable widgets to `lib/widgets/`
- Keep colors and styles in utility files
- Test navigation flow matches Android app
