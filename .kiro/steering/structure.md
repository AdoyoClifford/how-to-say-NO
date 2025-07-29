# Project Structure

## Root Level
```
├── app/                    # Main application module
├── gradle/                 # Gradle wrapper and version catalog
├── .gradle/               # Gradle cache (ignored)
├── .idea/                 # Android Studio/IntelliJ settings
├── .kiro/                 # Kiro AI assistant configuration
├── build.gradle.kts       # Root build configuration
├── settings.gradle.kts    # Project settings and modules
├── gradle.properties      # Global Gradle properties
├── gradlew & gradlew.bat  # Gradle wrapper scripts
└── local.properties       # Local SDK paths (ignored)
```

## App Module Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/com/adoyo/howtosayno/  # Main source code
│   │   │   ├── ui/                     # UI components and screens
│   │   │   └── MainActivity.kt         # Main activity
│   │   ├── res/                        # Android resources
│   │   │   ├── drawable/               # Images and vector drawables
│   │   │   ├── mipmap-*/              # App icons (various densities)
│   │   │   ├── values/                # Strings, colors, themes
│   │   │   └── xml/                   # XML configurations
│   │   └── AndroidManifest.xml        # App manifest
│   ├── test/                          # Unit tests
│   └── androidTest/                   # Instrumented tests
├── build.gradle.kts                   # App module build config
├── proguard-rules.pro                 # ProGuard configuration
└── .gitignore                         # Module-specific ignores
```

## Package Organization
- **Base package**: `com.adoyo.howtosayno`
- **UI package**: `com.adoyo.howtosayno.ui` - Contains Compose UI components
- **Main entry**: `MainActivity.kt` - Primary activity using Compose

## Conventions
- Use Kotlin for all source code
- Follow standard Android project structure
- Compose UI components should be organized in the `ui` package
- Test files mirror the main source structure
- Resources follow Android naming conventions (lowercase with underscores)

## Build Outputs
- Debug APK: `app/build/outputs/apk/debug/`
- Release APK: `app/build/outputs/apk/release/`
- Build intermediates: `app/build/intermediates/`