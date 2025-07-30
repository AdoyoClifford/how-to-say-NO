package com.adoyo.howtosayno.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adoyo.howtosayno.ui.theme.CustomExpressiveShapes
import com.adoyo.howtosayno.ui.theme.HowToSayNOTheme

/**
 * Enhanced Snackbar components with Material 3 Expressive styling
 * 
 * These components provide enhanced snackbar functionality with:
 * - Error type-specific styling
 * - Custom actions (retry, dismiss)
 * - Material 3 Expressive shapes and colors
 * - Accessibility support
 * 
 * Requirements addressed:
 * - 2.1, 2.2, 2.3, 2.4: Transient error message display
 * - 6.1, 6.2, 6.3: Material 3 styling and feedback
 */

/**
 * Enhanced error snackbar with retry functionality
 */
@Composable
fun ErrorSnackbar(
    snackbarData: SnackbarData,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveCard,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        actionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        action = if (onRetry != null) {
            {
                TextButton(
                    onClick = {
                        onRetry()
                        snackbarData.dismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Retry",
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text("Retry")
                }
            }
        } else null,
        dismissAction = if (snackbarData.visuals.withDismissAction) {
            {
                IconButton(
                    onClick = { snackbarData.dismiss() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss"
                    )
                }
            }
        } else null
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Warning snackbar for less critical issues
 */
@Composable
fun WarningSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveCard,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        actionContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        dismissActionContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        dismissAction = if (snackbarData.visuals.withDismissAction) {
            {
                IconButton(
                    onClick = { snackbarData.dismiss() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss"
                    )
                }
            }
        } else null
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Success snackbar for positive feedback
 */
@Composable
fun SuccessSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier
) {
    Snackbar(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveCard,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        actionContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        dismissActionContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        dismissAction = if (snackbarData.visuals.withDismissAction) {
            {
                IconButton(
                    onClick = { snackbarData.dismiss() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss"
                    )
                }
            }
        } else null
    ) {
        Text(
            text = snackbarData.visuals.message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Adaptive snackbar that chooses styling based on message content
 */
@Composable
fun AdaptiveSnackbar(
    snackbarData: SnackbarData,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val message = snackbarData.visuals.message
    
    when {
        isErrorMessage(message) -> {
            ErrorSnackbar(
                snackbarData = snackbarData,
                onRetry = onRetry,
                modifier = modifier
            )
        }
        isWarningMessage(message) -> {
            WarningSnackbar(
                snackbarData = snackbarData,
                modifier = modifier
            )
        }
        isSuccessMessage(message) -> {
            SuccessSnackbar(
                snackbarData = snackbarData,
                modifier = modifier
            )
        }
        else -> {
            // Default Material 3 snackbar with expressive shape
            Snackbar(
                snackbarData = snackbarData,
                modifier = modifier,
                shape = CustomExpressiveShapes.ExpressiveCard
            )
        }
    }
}

/**
 * Enhanced error snackbar with accessibility improvements and better error categorization
 */
@Composable
fun EnhancedErrorSnackbar(
    message: String,
    onRetry: (() -> Unit)? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    isOffline: Boolean = false
) {
    val errorType = determineSnackbarErrorType(message)
    
    Snackbar(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveCard,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        actionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        dismissActionContentColor = MaterialTheme.colorScheme.onErrorContainer,
        action = if (onRetry != null && errorType.isRetryable) {
            {
                AnimatedRetryButton(
                    onRetry = onRetry,
                    onDismiss = onDismiss
                )
            }
        } else null,
        dismissAction = {
            AnimatedDismissButton(
                onDismiss = onDismiss
            )
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated error icon with pulsing effect
            AnimatedSnackbarIcon(
                icon = errorType.icon,
                modifier = Modifier
                    .size(18.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun AnimatedRetryButton(
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessHigh
        ),
        label = "retryButtonScale"
    )

    TextButton(
        onClick = {
            onRetry()
            onDismiss()
        },
        interactionSource = interactionSource,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Retry action",
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(
            text = "Retry",
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun AnimatedDismissButton(
    onDismiss: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by androidx.compose.animation.core.animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = androidx.compose.animation.core.spring(
            dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
            stiffness = androidx.compose.animation.core.Spring.StiffnessHigh
        ),
        label = "dismissButtonScale"
    )

    IconButton(
        onClick = onDismiss,
        interactionSource = interactionSource,
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Dismiss error message"
        )
    }
}

@Composable
private fun AnimatedSnackbarIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Subtle pulsing animation for snackbar icon
    val infiniteTransition = rememberInfiniteTransition(label = "snackbarIconAnimation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "snackbarIconAlpha"
    )

    Icon(
        imageVector = icon,
        contentDescription = "Error type indicator",
        modifier = modifier.alpha(alpha)
    )
}

/**
 * Data class representing different snackbar error types
 */
private data class SnackbarErrorType(
    val icon: ImageVector,
    val isRetryable: Boolean
)

/**
 * Determines the snackbar error type based on the message
 */
private fun determineSnackbarErrorType(message: String): SnackbarErrorType {
    return when {
        message.contains("internet connection", ignoreCase = true) -> {
            SnackbarErrorType(
                icon = Icons.Default.Info,
                isRetryable = true
            )
        }
        message.contains("timeout", ignoreCase = true) -> {
            SnackbarErrorType(
                icon = Icons.Default.Warning,
                isRetryable = true
            )
        }
        else -> {
            SnackbarErrorType(
                icon = Icons.Default.Info,
                isRetryable = true
            )
        }
    }
}

/**
 * Helper functions to determine message types
 */
private fun isErrorMessage(message: String): Boolean {
    val errorKeywords = listOf("error", "failed", "connection", "timeout", "wrong")
    return errorKeywords.any { message.contains(it, ignoreCase = true) }
}

private fun isWarningMessage(message: String): Boolean {
    val warningKeywords = listOf("warning", "cache", "offline", "slow")
    return warningKeywords.any { message.contains(it, ignoreCase = true) }
}

private fun isSuccessMessage(message: String): Boolean {
    val successKeywords = listOf("success", "completed", "saved", "updated")
    return successKeywords.any { message.contains(it, ignoreCase = true) }
}

// Preview composables for testing
@Preview(showBackground = true)
@Composable
private fun ErrorSnackbarPreview() {
    HowToSayNOTheme {
        // Note: This is a simplified preview - actual SnackbarData would come from SnackbarHostState
        Snackbar(
            shape = CustomExpressiveShapes.ExpressiveCard,
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            action = {
                TextButton(onClick = {}) {
                    Text("Retry")
                }
            }
        ) {
            Text("No internet connection")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WarningSnackbarPreview() {
    HowToSayNOTheme {
        Snackbar(
            shape = CustomExpressiveShapes.ExpressiveCard,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ) {
            Text("Showing cached content")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnhancedErrorSnackbarPreview() {
    HowToSayNOTheme {
        EnhancedErrorSnackbar(
            message = "No internet connection",
            onRetry = {},
            onDismiss = {},
            isOffline = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}