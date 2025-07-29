# Technology Stack

## Build System
- **Gradle** with Kotlin DSL (`.gradle.kts` files)
- **Android Gradle Plugin**: 8.12.0-alpha08
- **Kotlin**: 2.0.21

## Core Technologies
- **Android SDK**: compileSdk 36, minSdk 24, targetSdk 36
- **Kotlin**: Primary language with JVM target 11
- **Jetpack Compose**: Modern UI toolkit for native Android
- **Material Design 3**: UI components and theming

## Key Dependencies
- **AndroidX Core KTX**: Core Android extensions
- **Lifecycle Runtime KTX**: Lifecycle-aware components
- **Activity Compose**: Compose integration with activities
- **Compose BOM**: Bill of materials for Compose versions
- **Material3**: Material Design 3 components
- **Retrofit**: HTTP client for API communication (2.9.0)
- **OkHttp**: HTTP client library (4.12.0)
- **Kotlinx Serialization**: JSON parsing and serialization (1.6.3)
- **Lifecycle ViewModel**: ViewModel components for UI state management (2.8.7)
- **Coroutines**: Asynchronous programming support (1.7.3)

## Testing Framework
- **JUnit 4**: Unit testing
- **AndroidX Test**: Instrumented testing
- **Espresso**: UI testing
- **Compose UI Test**: Compose-specific testing

## Common Commands
```bash
# Build the project
./gradlew build

# Install debug APK
./gradlew installDebug

# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Clean build
./gradlew clean

# Generate release APK
./gradlew assembleRelease
```

## Configuration Notes
- Uses version catalog (`gradle/libs.versions.toml`) for dependency management
- Kotlin code style set to "official"
- AndroidX enabled
- ProGuard disabled in debug builds