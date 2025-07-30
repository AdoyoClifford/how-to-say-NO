package com.adoyo.howtosayno.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(400)),
        exit = fadeOut(animationSpec = tween(200))
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
                // Error icon
                Icon(
                    imageVector = errorType.icon,
                    contentDescription = "Error indicator",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(48.dp)
                )
                
                Spacer(modifier = Modifier.height(ExpressiveSpacing.Medium))
                
                // Error title
                Text(
                    text = errorType.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(ExpressiveSpacing.Small))
                
                // Error message
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
                
                // Recovery suggestion
                if (errorType.suggestion.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(ExpressiveSpacing.Small))
                    Text(
                        text = errorType.suggestion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
                
                // Retry button
                if (showRetryButton) {
                    Spacer(modifier = Modifier.height(ExpressiveSpacing.Large))
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

/**
 * Enhanced offline indicator with more prominent styling
 */
@Composable
fun EnhancedOfflineIndicator(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200))
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
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Offline indicator",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(200))
    ) {
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
                Icon(
                    imageVector = if (isOnline) Icons.Default.Info else Icons.Default.Warning,
                    contentDescription = if (isOnline) "Online" else "Offline",
                    tint = if (isOnline) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
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