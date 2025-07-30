package com.adoyo.howtosayno.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adoyo.howtosayno.ui.theme.CustomExpressiveShapes
import com.adoyo.howtosayno.ui.theme.ExpressiveSpacing
import com.adoyo.howtosayno.ui.theme.HowToSayNOTheme

/**
 * Enhanced error handling UI components with Material 3 styling
 * 
 * These components provide comprehensive error state handling with:
 * - Specific error type indicators
 * - User-friendly error messages
 * - Recovery suggestions
 * - Retry functionality
 * - Offline state indicators
 * 
 * Requirements addressed:
 * - 2.1, 2.2, 2.3, 2.4: Comprehensive error handling
 * - 5.3: Offline state indicators
 * - 6.1, 6.2: Material 3 styling and components
 */

/**
 * Enhanced error state composable with specific error type handling
 */
@Composable
fun ErrorStateCard(
    errorMessage: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isOffline: Boolean = false,
    showRetryButton: Boolean = true
) {
    val errorType = determineErrorType(errorMessage, isOffline)
    
    // Animate card entrance with scale and fade
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            initialScale = 0.8f
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            initialOffsetY = { it / 4 }
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        ) + scaleOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            targetScale = 0.9f
        )
    ) {
        Card(
            modifier = modifier,
            shape = CustomExpressiveShapes.ExpressiveContainer,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(ExpressiveSpacing.Large),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animated error icon with subtle pulsing
                AnimatedErrorIcon(
                    icon = errorType.icon,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(ExpressiveSpacing.Medium))
                
                // Error title with fade-in animation
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 800,
                            delayMillis = 200,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Text(
                        text = errorType.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
                
                Spacer(modifier = Modifier.height(ExpressiveSpacing.Small))
                
                // Error message with fade-in animation
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(
                        animationSpec = tween(
                            durationMillis = 800,
                            delayMillis = 400,
                            easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Recovery suggestion with fade-in animation
                if (errorType.suggestion.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(ExpressiveSpacing.Small))
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 800,
                                delayMillis = 600,
                                easing = FastOutSlowInEasing
                            )
                        )
                    ) {
                        Text(
                            text = errorType.suggestion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                // Retry button with fade-in and scale animation
                if (showRetryButton) {
                    Spacer(modifier = Modifier.height(ExpressiveSpacing.Large))
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 600,
                                delayMillis = 800,
                                easing = FastOutSlowInEasing
                            )
                        ) + scaleIn(
                            animationSpec = tween(
                                durationMillis = 600,
                                delayMillis = 800,
                                easing = FastOutSlowInEasing
                            ),
                            initialScale = 0.8f
                        )
                    ) {
                        OutlinedButton(
                            onClick = onRetry,
                            shape = CustomExpressiveShapes.ExpressiveButton,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(ExpressiveSpacing.Small))
                            Text(
                                text = "Try Again",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedErrorIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Subtle pulsing animation for error icon
    val infiniteTransition = rememberInfiniteTransition(label = "errorIconAnimation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "errorIconAlpha"
    )

    Icon(
        imageVector = icon,
        contentDescription = "Error indicator",
        tint = MaterialTheme.colorScheme.onErrorContainer,
        modifier = modifier.alpha(alpha)
    )
}

/**
 * Enhanced offline indicator with more prominent styling
 */
@Composable
fun EnhancedOfflineIndicator(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    // Enhanced entrance animation with slide and scale
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            initialOffsetY = { it / 2 }
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            initialScale = 0.8f
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            )
        ) + slideOutVertically(
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing
            ),
            targetOffsetY = { it / 2 }
        )
    ) {
        Card(
            modifier = modifier,
            shape = CustomExpressiveShapes.ExpressiveCard,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ExpressiveSpacing.Medium,
                        vertical = ExpressiveSpacing.Small
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated icon with subtle pulsing
                AnimatedOfflineIcon(
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(ExpressiveSpacing.Small))
                Text(
                    text = "Showing cached reason (offline)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AnimatedOfflineIcon(
    modifier: Modifier = Modifier
) {
    // Subtle pulsing animation for offline icon
    val infiniteTransition = rememberInfiniteTransition(label = "offlineIconAnimation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offlineIconAlpha"
    )

    Icon(
        imageVector = Icons.Default.Info,
        contentDescription = "Offline indicator",
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.alpha(alpha)
    )
}

/**
 * Inline error message for less critical errors
 */
@Composable
fun InlineErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(ExpressiveSpacing.Small))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Network status indicator for showing connection state
 */
@Composable
fun NetworkStatusIndicator(
    isOnline: Boolean,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    // Enhanced entrance animation with color transition
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            )
        ) + slideInVertically(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            initialOffsetY = { it / 3 }
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            initialScale = 0.9f
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            )
        ) + slideOutVertically(
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            ),
            targetOffsetY = { it / 3 }
        )
    ) {
        // Animate elevation based on connection status
        val elevation by animateFloatAsState(
            targetValue = if (isOnline) 2f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "networkStatusElevation"
        )

        Card(
            modifier = modifier,
            shape = CustomExpressiveShapes.ExpressiveCard,
            colors = CardDefaults.cardColors(
                containerColor = if (isOnline) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.errorContainer
                }
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = elevation.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = ExpressiveSpacing.Medium,
                        vertical = ExpressiveSpacing.Small
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated network status icon
                AnimatedNetworkIcon(
                    isOnline = isOnline,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(ExpressiveSpacing.Small))
                Text(
                    text = if (isOnline) "Connected" else "No internet connection",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOnline) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    }
                )
            }
        }
    }
}

@Composable
private fun AnimatedNetworkIcon(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    // Different animations based on connection status
    if (isOnline) {
        // Subtle breathing for online status
        val infiniteTransition = rememberInfiniteTransition(label = "onlineIconAnimation")
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 2000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "onlineIconAlpha"
        )

        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Online",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier.alpha(alpha)
        )
    } else {
        // More prominent pulsing for offline status
        val infiniteTransition = rememberInfiniteTransition(label = "offlineIconAnimation")
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.9f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "offlineIconScale"
        )

        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Offline",
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = modifier.scale(scale)
        )
    }
}

/**
 * Data class representing different error types with their UI properties
 */
private data class ErrorType(
    val icon: ImageVector,
    val title: String,
    val suggestion: String
)

/**
 * Determines the error type based on the error message and context
 */
private fun determineErrorType(errorMessage: String, isOffline: Boolean): ErrorType {
    return when {
        errorMessage.contains("internet connection", ignoreCase = true) || isOffline -> {
            ErrorType(
                icon = Icons.Default.Warning,
                title = "No Internet Connection",
                suggestion = "Check your network connection and try again"
            )
        }
        errorMessage.contains("timeout", ignoreCase = true) -> {
            ErrorType(
                icon = Icons.Default.Warning,
                title = "Request Timed Out",
                suggestion = "The server is taking too long to respond"
            )
        }
        errorMessage.contains("cache", ignoreCase = true) -> {
            ErrorType(
                icon = Icons.Default.Info,
                title = "No Cached Content",
                suggestion = "Connect to the internet to fetch new content"
            )
        }
        else -> {
            ErrorType(
                icon = Icons.Default.Info,
                title = "Something Went Wrong",
                suggestion = "Please try again in a moment"
            )
        }
    }
}

// Preview composables
@Preview(showBackground = true)
@Composable
private fun ErrorStateCardPreview() {
    HowToSayNOTheme {
        ErrorStateCard(
            errorMessage = "No internet connection",
            onRetry = {},
            isOffline = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorStateCardTimeoutPreview() {
    HowToSayNOTheme {
        ErrorStateCard(
            errorMessage = "Request timed out, please try again",
            onRetry = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EnhancedOfflineIndicatorPreview() {
    HowToSayNOTheme {
        EnhancedOfflineIndicator(
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun InlineErrorMessagePreview() {
    HowToSayNOTheme {
        InlineErrorMessage(
            message = "Failed to load content",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkStatusIndicatorOnlinePreview() {
    HowToSayNOTheme {
        NetworkStatusIndicator(
            isOnline = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NetworkStatusIndicatorOfflinePreview() {
    HowToSayNOTheme {
        NetworkStatusIndicator(
            isOnline = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}