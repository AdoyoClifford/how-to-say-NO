package com.adoyo.howtosayno.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive Shape System
 * Enhanced shapes for expressive design
 */
val ExpressiveShapes = Shapes(
    // Extra small components like chips, badges
    extraSmall = RoundedCornerShape(4.dp),
    
    // Small components like buttons, text fields
    small = RoundedCornerShape(8.dp),
    
    // Medium components like cards, dialogs
    medium = RoundedCornerShape(12.dp),
    
    // Large components like bottom sheets, navigation drawers
    large = RoundedCornerShape(16.dp),
    
    // Extra large components like full-screen dialogs
    extraLarge = RoundedCornerShape(28.dp)
)

/**
 * Custom expressive shapes for enhanced emotional engagement
 */
object CustomExpressiveShapes {
    val ExpressiveButton = RoundedCornerShape(20.dp)
    val ExpressiveCard = RoundedCornerShape(16.dp)
    val ExpressiveContainer = RoundedCornerShape(24.dp)
    val ExpressiveDialog = RoundedCornerShape(28.dp)
}