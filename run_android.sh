#!/bin/bash
# Run the app on Android. Start the emulator manually first if needed (see README).
set -e
cd "$(dirname "$0")"

# Use exact device id if available to avoid "number expected" / parsing issues
if flutter devices 2>/dev/null | grep -q "emulator"; then
  echo "Android emulator found. Running app..."
  flutter run -d android
else
  echo "No Android emulator running."
  echo "Start it from Android Studio: Tools → Device Manager → run Pixel 9 Pro XL"
  echo "Or run this in a separate terminal and wait for boot:"
  echo "  flutter emulators --launch Pixel_9_Pro_XL"
  echo ""
  echo "Then run: flutter run -d android"
  exit 1
fi
