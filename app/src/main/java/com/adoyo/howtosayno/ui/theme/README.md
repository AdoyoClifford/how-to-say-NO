# Material 3 Expressive Theme Implementation

This directory contains the complete Material 3 Expressive theming system for the "How to Say NO" Android app.

## Overview

The theme implementation provides:
- **Material 3 Expressive typography scales** for enhanced readability and visual hierarchy
- **Enhanced dynamic color theming** with Material You support (Android 12+)
- **Expressive color tokens** for emotional engagement
- **Proper light and dark theme support** with automatic system theme detection
- **Enhanced spacing system** for consistent layout and improved accessibility

## Files Structure

### Core Theme Files
- **`Theme.kt`** - Main theme composable with Material You dynamic color support
- **`Color.kt`** - Complete Material 3 Expressive color palette for light and dark themes
- **`Type.kt`** - Material 3 Expressive typography scales with proper hierarchy
- **`Shape.kt`** - Material 3 shape system for consistent component styling
- **`Spacing.kt`** - Enhanced spacing tokens for expressive design
- **`ThemeExtensions.kt`** - Additional utilities and semantic color tokens

## Key Features

### 1. Material 3 Expressive Typography
- Complete typography scale from `displayLarge` (57sp) to `labelSmall` (11sp)
- Proper line heights and letter spacing for optimal readability
- Enhanced hierarchy for expressive content display

### 2. Dynamic Color Support
- **Material You integration** - Automatically adapts to user's wallpaper colors on Android 12+
- **Fallback color schemes** - Custom expressive colors for older Android versions
- **Proper contrast ratios** - Ensures accessibility compliance

### 3. Enhanced Color Palette
- **Primary colors** - Main brand colors with proper contrast
- **Secondary/Tertiary colors** - Supporting colors for variety
- **Semantic colors** - Success, warning, info, and error states
- **Expressive accents** - Warm, cool, energetic, and calm accent colors
- **Surface elevation** - Multiple surface levels for depth perception

### 4. Comprehensive Spacing System
- **Base spacing units** - From 4dp to 64dp for consistent layouts
- **Expressive spacing** - Enhanced spacing for emotional engagement
- **Component-specific spacing** - Optimized for buttons, cards, and screens
- **Accessibility compliance** - Minimum touch target sizes (48dp+)

### 5. Shape System
- **Rounded corners** - From 4dp (extra small) to 28dp (extra large)
- **Custom expressive shapes** - Enhanced shapes for emotional engagement
- **Component consistency** - Unified shape language across the app

## Usage Examples

### Basic Theme Usage
```kotlin
@Composable
fun MyApp() {
    HowToSayNOTheme {
        // Your app content here
        MyScreen()
    }
}
```

### With Custom Configuration
```kotlin
@Composable
fun MyApp() {
    HowToSayNOTheme(
        darkTheme = isSystemInDarkTheme(),
        dynamicColor = true // Enable Material You
    ) {
        MyScreen()
    }
}
```

### Using Expressive Colors
```kotlin
@Composable
fun MyButton() {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = ExpressiveColors.energetic
        )
    ) {
        Text("Energetic Button")
    }
}
```

### Using Expressive Typography
```kotlin
@Composable
fun MyContent() {
    Column {
        Text(
            text = "Main Heading",
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "Body content",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

### Using Expressive Spacing
```kotlin
@Composable
fun MyLayout() {
    Column(
        modifier = Modifier.padding(ExpressiveSpacing.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(ExpressiveSpacing.SectionSpacing)
    ) {
        // Content with consistent spacing
    }
}
```

## Theme Customization

### Adding Custom Colors
To add custom colors, extend the `ExpressiveColors` object in `ThemeExtensions.kt`:

```kotlin
object ExpressiveColors {
    // Existing colors...
    val customAccent: Color @Composable @ReadOnlyComposable get() = Color(0xFF123456)
}
```

### Custom Typography
To customize typography, modify the `ExpressiveTypography` in `Type.kt`:

```kotlin
val ExpressiveTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = YourCustomFont, // Add custom font
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        // ... other properties
    )
)
```

## Accessibility Features

- **High contrast ratios** - All color combinations meet WCAG AA standards
- **Large touch targets** - Minimum 48dp touch targets for interactive elements
- **Scalable typography** - Supports dynamic text sizing
- **Screen reader support** - Semantic color naming for accessibility services

## Material You Integration

The theme automatically detects Android 12+ devices and uses Material You dynamic colors when available:

- **Wallpaper-based colors** - Extracts colors from user's wallpaper
- **System theme detection** - Automatically switches between light/dark themes
- **Graceful fallback** - Uses custom expressive colors on older devices

## Performance Considerations

- **Lazy color evaluation** - Colors are computed only when needed
- **Efficient recomposition** - Stable color schemes prevent unnecessary recompositions
- **Memory optimization** - Shared color instances across the app

## Testing

The theme is designed to work seamlessly with Compose testing:

```kotlin
@Test
fun testThemeColors() {
    composeTestRule.setContent {
        HowToSayNOTheme {
            // Test your themed components
        }
    }
}
```

## Migration Guide

If migrating from the previous theme:
1. Replace `MaterialTheme` calls with `HowToSayNOTheme`
2. Update color references to use the new expressive color tokens
3. Update typography references to use the new expressive typography scales
4. Update spacing to use the new `ExpressiveSpacing` tokens

## Requirements Compliance

This implementation satisfies all requirements from the specification:

- ✅ **6.1** - Material 3 Expressive design components and styling
- ✅ **6.2** - Material 3 typography, colors, and spacing
- ✅ **6.3** - Material 3 button styles and animations support
- ✅ **6.4** - Material 3 feedback and transitions support