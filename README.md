# aswack_shopkeeper

Zarooori – World Vehicle Services. Flutter Shopkeeper app.

## Run in Android Emulator (Debug)

Run **one command per line** in the terminal. Do not paste multiple lines as one (that causes "zsh: number expected" / "command not found: #").

**Step 1 – Start the emulator** (run only this command; wait until the emulator window is fully up):

```bash
flutter emulators --launch Pixel_9_Pro_XL
```

**Step 2 – When the emulator has booted, run the app** (same or new terminal):

```bash
flutter run -d android
```

**If the emulator fails to start** ("exited with code 1" / "Address these issues"):

- **"Your device does not have enough disk space"** – The Pixel 9 Pro XL AVD needs a lot of free space. Either **free up disk space** on your Mac, or create a **lighter AVD**: Android Studio → **Tools** → **Device Manager** → **Create Device** → choose **Pixel 6** (or any phone) → **Next** → pick a **non-Play Store** system image (smaller) → **Finish**. Then run `flutter emulators --launch <new_avd_id>`.
- Other issues: **Device Manager** → **⋮** on your AVD → **Cold Boot Now** or **Wipe Data**.

**Run on Chrome instead** (no emulator needed):

```bash
flutter run -d chrome
```

**If you see "No devices found":** Start the emulator (step 1), wait for it to boot, then run step 2.

**If you see "No macOS desktop project configured":** Select the **Android** device in the status bar (bottom-right in Cursor) or use the **Flutter (Android Emulator)** launch config.

## Getting Started

- [Flutter documentation](https://docs.flutter.dev/)
- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
