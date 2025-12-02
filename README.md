# CatCare (Kotlin Multiplatform Sample)

CatCare is a Kotlin Multiplatform sample app demonstrating a Compose UI, simple data persistence, and platform-specific integrations.

Highlights
- Kotlin Multiplatform: common UI + platform actuals (icons, file persistence). 
- Compose Multiplatform UI with Material3 components.
- Tabs: Home, Breed Classifier, Settings.
- Global FAB opens a modal bottom sheet to add a cat.
- One-time onboarding overlay (persisted across launches).

Build / Run

Android (preferred):
1. Open the project in Android Studio.
2. Sync Gradle, then run the `app` (or the ComposeApp module) on an Android device or emulator.

iOS (requires macOS):
1. Open `iosApp` Xcode workspace after building the shared framework using Gradle.

Notes
- The Breed Classifier currently uses a demo classifier. You can plug a real model (TFLite/CoreML) by adding platform-specific inference code.
- Material3 icons are provided by the Android actual; iOS falls back to emoji in this sample.

If you want, I can add a CI workflow, additional tests, or package screenshots/GIFs for your Kotlin Multiplatform Challenge submission.
