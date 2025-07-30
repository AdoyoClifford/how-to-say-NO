package com.adoyo.howtosayno.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adoyo.howtosayno.ui.components.AdaptiveSnackbar
import com.adoyo.howtosayno.ui.components.EnhancedOfflineIndicator
import com.adoyo.howtosayno.ui.components.ErrorStateCard
import com.adoyo.howtosayno.ui.components.NetworkStatusIndicator
import com.adoyo.howtosayno.ui.components.EnhancedErrorSnackbar
import com.adoyo.howtosayno.ui.state.NoReasonUiState
import com.adoyo.howtosayno.ui.theme.CustomExpressiveShapes
import com.adoyo.howtosayno.ui.theme.ExpressiveSpacing
import com.adoyo.howtosayno.ui.theme.HowToSayNOTheme
import com.adoyo.howtosayno.ui.viewmodel.NoReasonViewModel

/**
 * Main screen composable for displaying "No" reasons
 * 
 * This screen implements Material 3 Expressive design with:
 * - Large, prominent text display for reasons
 * - Expressive button for fetching new reasons
 * - Loading indicators with smooth animations
 * - Error handling with retry functionality
 * - Offline support indicators
 * 
 * Requirements addressed:
 * - 1.3: Display reason text to user
 * - 3.1, 3.2: Loading indicator display
 * - 4.1, 4.2: Button interaction and state management
 * - 6.1, 6.2, 6.3, 6.4: Material 3 Expressive design
 */
@Composable
fun NoReasonScreen(
    modifier: Modifier = Modifier,
    viewModel: NoReasonViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error messages in snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                withDismissAction = true
            )
            viewModel.clearError()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        NoReasonContent(
            uiState = uiState,
            onFetchNewReason = viewModel::fetchNewReason,
            onRetry = viewModel::retry,
            modifier = Modifier.fillMaxSize()
        )

        // Enhanced snackbar for error messages
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            AdaptiveSnackbar(
                snackbarData = snackbarData,
                onRetry = viewModel::retry
            )
        }
    }
}

@Composable
private fun NoReasonContent(
    uiState: NoReasonUiState,
    onFetchNewReason: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ExpressiveSpacing.ScreenPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Main content area with reason display
        ReasonDisplayCard(
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
        )

        Spacer(modifier = Modifier.height(ExpressiveSpacing.SectionSpacing))

        // Action buttons
        ActionButtons(
            uiState = uiState,
            onFetchNewReason = onFetchNewReason,
            onRetry = onRetry,
            modifier = Modifier.fillMaxWidth()
        )

        // Enhanced offline indicator
        if (uiState.isOffline && uiState.hasCache) {
            Spacer(modifier = Modifier.height(ExpressiveSpacing.Medium))
            EnhancedOfflineIndicator(
                isVisible = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Network status indicator for better user awareness
        if (uiState.hasError || uiState.isOffline) {
            Spacer(modifier = Modifier.height(ExpressiveSpacing.Small))
            NetworkStatusIndicator(
                isOnline = !uiState.isOffline,
                isVisible = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ReasonDisplayCard(
    uiState: NoReasonUiState,
    modifier: Modifier = Modifier
) {
    // Animate card elevation based on state
    val cardElevation by animateFloatAsState(
        targetValue = if (uiState.isLoading) 8f else 4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardElevation"
    )

    Card(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveContainer,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ExpressiveSpacing.ExpressiveExtraLarge),
            contentAlignment = Alignment.Center
        ) {
            // Animated content transitions between different states
            AnimatedContent(
                targetState = when {
                    uiState.isLoading -> ContentState.Loading
                    uiState.hasError && !uiState.hasContent -> ContentState.Error
                    uiState.hasContent -> ContentState.Content
                    else -> ContentState.Placeholder
                },
                transitionSpec = {
                    fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        )
                    ) + slideInVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = FastOutSlowInEasing
                        ),
                        initialOffsetY = { it / 4 }
                    ) togetherWith fadeOut(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    ) + slideOutVertically(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        ),
                        targetOffsetY = { -it / 4 }
                    )
                },
                label = "contentTransition"
            ) { contentState ->
                when (contentState) {
                    ContentState.Loading -> {
                        LoadingIndicator()
                    }
                    ContentState.Error -> {
                        ErrorStateCard(
                            errorMessage = uiState.error ?: "Something went wrong",
                            onRetry = { /* Handled by ActionButtons */ },
                            isOffline = uiState.isOffline,
                            showRetryButton = false, // Retry button is handled separately
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ContentState.Content -> {
                        ReasonText(
                            reason = uiState.reason,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ContentState.Placeholder -> {
                        PlaceholderText()
                    }
                }
            }
        }
    }
}

// Enum to represent different content states for animation
private enum class ContentState {
    Loading, Error, Content, Placeholder
}

@Composable
private fun LoadingIndicator() {
    // Animated scale for breathing effect
    val infiniteTransition = rememberInfiniteTransition(label = "loadingAnimation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loadingScale"
    )

    // Animated alpha for text pulsing
    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "textAlpha"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp)
                .scale(scale),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(ExpressiveSpacing.Medium))
        Text(
            text = "Finding a reason...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.alpha(textAlpha)
        )
    }
}

@Composable
private fun ReasonText(
    reason: String,
    modifier: Modifier = Modifier
) {
    // Enhanced entrance animation with scale and fade
    AnimatedVisibility(
        visible = reason.isNotEmpty(),
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            )
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = 800,
                easing = FastOutSlowInEasing
            ),
            initialScale = 0.8f
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            )
        ) + scaleOut(
            animationSpec = tween(
                durationMillis = 400,
                easing = LinearEasing
            ),
            targetScale = 0.9f
        )
    ) {
        Text(
            text = reason,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}

@Composable
private fun PlaceholderText() {
    // Subtle breathing animation for placeholder text
    val infiniteTransition = rememberInfiniteTransition(label = "placeholderAnimation")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 3000,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "placeholderAlpha"
    )

    Text(
        text = "Tap the button below to get a creative reason for saying no!",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.alpha(alpha)
    )
}

@Composable
private fun ActionButtons(
    uiState: NoReasonUiState,
    onFetchNewReason: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated transition between retry and main button
        AnimatedContent(
            targetState = uiState.shouldShowRetry,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    ),
                    initialOffsetY = { it / 2 }
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    )
                ) + slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing
                    ),
                    targetOffsetY = { -it / 2 }
                )
            },
            label = "buttonTransition"
        ) { showRetry ->
            if (showRetry) {
                // Enhanced retry button with press animations
                ExpressiveRetryButton(
                    onClick = onRetry,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ExpressiveSpacing.ExpressiveTouchTarget)
                )
            } else {
                // Enhanced main fetch button with press animations
                ExpressiveFetchButton(
                    onClick = onFetchNewReason,
                    enabled = uiState.isButtonEnabled && !uiState.isLoading,
                    isLoading = uiState.isLoading,
                    hasContent = uiState.hasContent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ExpressiveSpacing.ExpressiveTouchTarget)
                )
            }
        }
    }
}

@Composable
private fun ExpressiveRetryButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animate button scale on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "retryButtonScale"
    )

    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = CustomExpressiveShapes.ExpressiveButton,
        interactionSource = interactionSource,
        modifier = modifier.scale(scale)
    ) {
        Text(
            text = "Try Again",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun ExpressiveFetchButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    hasContent: Boolean,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animate button scale on press
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "fetchButtonScale"
    )

    // Animate elevation on press
    val elevation by animateFloatAsState(
        targetValue = if (isPressed) 12f else 6f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fetchButtonElevation"
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = CustomExpressiveShapes.ExpressiveButton,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation.dp,
            pressedElevation = (elevation + 2f).dp
        ),
        interactionSource = interactionSource,
        modifier = modifier.scale(scale)
    ) {
        // Animated content transition for button text/loading
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ) togetherWith fadeOut(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            },
            label = "buttonContentTransition"
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = if (hasContent) "Get Another Reason" else "Get a Reason",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}



// Preview composables
@Preview(showBackground = true)
@Composable
private fun NoReasonScreenPreview() {
    HowToSayNOTheme {
        NoReasonContent(
            uiState = NoReasonUiState(
                reason = "This feels like something Future Me would yell at Present Me for agreeing to.",
                isLoading = false,
                hasCache = true
            ),
            onFetchNewReason = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoReasonScreenLoadingPreview() {
    HowToSayNOTheme {
        NoReasonContent(
            uiState = NoReasonUiState(
                isLoading = true,
                isButtonEnabled = false
            ),
            onFetchNewReason = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoReasonScreenErrorPreview() {
    HowToSayNOTheme {
        NoReasonContent(
            uiState = NoReasonUiState(
                error = "No internet connection",
                isButtonEnabled = true
            ),
            onFetchNewReason = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoReasonScreenOfflinePreview() {
    HowToSayNOTheme {
        NoReasonContent(
            uiState = NoReasonUiState(
                reason = "I'm already committed to being uncommitted that day.",
                isOffline = true,
                hasCache = true,
                isButtonEnabled = true
            ),
            onFetchNewReason = {},
            onRetry = {}
        )
    }
}