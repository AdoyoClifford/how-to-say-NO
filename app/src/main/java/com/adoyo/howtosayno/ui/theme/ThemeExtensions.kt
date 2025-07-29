package com.adoyo.howtosayno.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Theme Extensions
 * Additional utilities and tokens for enhanced expressive design
 */

/**
 * Extended color palette for expressive design
 */
object ExpressiveColors {
    val warm: Color @Composable @ReadOnlyComposable get() = ExpressiveAccentWarm
    val cool: Color @Composable @ReadOnlyComposable get() = ExpressiveAccentCool
    val energetic: Color @Composable @ReadOnlyComposable get() = ExpressiveAccentEnergetic
    val calm: Color @Composable @ReadOnlyComposable get() = ExpressiveAccentCalm
}

/**
 * Semantic color tokens for specific use cases
 */
object SemanticColors {
    val success: Color @Composable @ReadOnlyComposable get() = Color(0xFF4CAF50)
    val warning: Color @Composable @ReadOnlyComposable get() = Color(0xFFFF9800)
    val info: Color @Composable @ReadOnlyComposable get() = Color(0xFF2196F3)
    
    val successContainer: Color @Composable @ReadOnlyComposable get() = Color(0xFFE8F5E8)
    val warningContainer: Color @Composable @ReadOnlyComposable get() = Color(0xFFFFF3E0)
    val infoContainer: Color @Composable @ReadOnlyComposable get() = Color(0xFFE3F2FD)
}

/**
 * Surface elevation colors for enhanced depth perception
 */
object SurfaceColors {
    val surface1: Color @Composable @ReadOnlyComposable get() = 
        if (MaterialTheme.colorScheme.background == ExpressiveBackgroundDark) {
            Color(0xFF1F1F23)
        } else {
            Color(0xFFF7F2FA)
        }
    
    val surface2: Color @Composable @ReadOnlyComposable get() = 
        if (MaterialTheme.colorScheme.background == ExpressiveBackgroundDark) {
            Color(0xFF232329)
        } else {
            Color(0xFFF1ECF4)
        }
    
    val surface3: Color @Composable @ReadOnlyComposable get() = 
        if (MaterialTheme.colorScheme.background == ExpressiveBackgroundDark) {
            Color(0xFF28282F)
        } else {
            Color(0xFFEBE6EE)
        }
}

/**
 * Animation duration constants for consistent motion design
 */
object ExpressiveAnimations {
    const val FastDuration = 150
    const val MediumDuration = 300
    const val SlowDuration = 500
    const val ExtraSlowDuration = 700
}

/**
 * Elevation values for consistent depth hierarchy
 */
object ExpressiveElevation {
    val Level0 = 0.dp
    val Level1 = 1.dp
    val Level2 = 3.dp
    val Level3 = 6.dp
    val Level4 = 8.dp
    val Level5 = 12.dp
}