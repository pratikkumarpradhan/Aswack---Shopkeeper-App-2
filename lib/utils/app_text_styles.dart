import 'package:flutter/material.dart';
import 'app_colors.dart';

class AppTextStyles {
  // Title Styles
  static TextStyle otpTitle({Color? color}) => TextStyle(
    fontSize: 18,
    fontWeight: FontWeight.w600,
    color: color ?? AppColors.black,
  );

  // Text View Styles
  static TextStyle textView13ssp({Color? color}) => TextStyle(
    fontSize: 13,
    color: color ?? AppColors.black,
  );

  static TextStyle textView15ssp({Color? color}) => TextStyle(
    fontSize: 15,
    color: color ?? AppColors.black,
  );

  static TextStyle textView18ssp({Color? color}) => TextStyle(
    fontSize: 18,
    color: color ?? AppColors.black,
  );

  // Button Text Style
  static TextStyle buttonText({Color? color}) => TextStyle(
    fontSize: 13,
    fontWeight: FontWeight.w500,
    color: color ?? AppColors.yellow,
  );

  // Hint Text Style
  static TextStyle hintText({Color? color}) => TextStyle(
    fontSize: 13,
    color: color ?? AppColors.black,
  );
}
