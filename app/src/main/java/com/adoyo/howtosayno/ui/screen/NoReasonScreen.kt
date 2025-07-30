package com.adoyo.howtosayno.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
    Card(
        modifier = modifier,
        shape = CustomExpressiveShapes.ExpressiveContainer,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ExpressiveSpacing.ExpressiveExtraLarge),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    LoadingIndicator()
                }
                uiState.hasError && !uiState.hasContent -> {
                    // Show error state when there's an error and no cached content
                    ErrorStateCard(
                        errorMessage = uiState.error ?: "Something went wrong",
                        onRetry = { /* Handled by ActionButtons */ },
                        isOffline = uiState.isOffline,
                        showRetryButton = false, // Retry button is handled separately
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                uiState.hasContent -> {
                    ReasonText(
                        reason = uiState.reason,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                else -> {
                    PlaceholderText()
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
        Spacer(modifier = Modifier.height(ExpressiveSpacing.Medium))
        Text(
            text = "Finding a reason...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ReasonText(
    reason: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = reason.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(600)),
        exit = fadeOut(animationSpec = tween(300))
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
    Text(
        text = "Tap the button below to get a creative reason for saying no!",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
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
        if (uiState.shouldShowRetry) {
            // Show retry button when there's an error
            OutlinedButton(
                onClick = onRetry,
                enabled = !uiState.isLoading,
                shape = CustomExpressiveShapes.ExpressiveButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ExpressiveSpacing.ExpressiveTouchTarget)
            ) {
                Text(
                    text = "Try Again",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        } else {
            // Main fetch button
            Button(
                onClick = onFetchNewReason,
                enabled = uiState.isButtonEnabled && !uiState.isLoading,
                shape = CustomExpressiveShapes.ExpressiveButton,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ExpressiveSpacing.ExpressiveTouchTarget)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (uiState.hasContent) "Get Another Reason" else "Get a Reason",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
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