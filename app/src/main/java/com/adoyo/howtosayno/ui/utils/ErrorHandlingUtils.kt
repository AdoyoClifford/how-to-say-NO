package com.adoyo.howtosayno.ui.utils

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Utility functions for consistent error handling across the app
 * 
 * These utilities provide:
 * - Standardized error message formatting
 * - Consistent snackbar behavior
 * - Error categorization and handling
 * - Retry logic coordination
 * 
 * Requirements addressed:
 * - 2.1, 2.2, 2.3, 2.4: Consistent error handling
 * - 6.1, 6.2: Material 3 feedback patterns
 */

/**
 * Error categories for different types of errors
 */
enum class ErrorCategory {
    NETWORK,
    TIMEOUT,
    CACHE,
    GENERIC
}

/**
 * Data class representing an error with its category and user-friendly message
 */
data class AppError(
    val category: ErrorCategory,
    val message: String,
    val isRetryable: Boolean = true,
    val originalException: Throwable? = null
)

/**
 * Converts exceptions to user-friendly AppError objects
 */
fun Throwable.toAppError(): AppError {
    return when (this) {
        is java.net.UnknownHostException -> AppError(
            category = ErrorCategory.NETWORK,
            message = "No internet connection",
            isRetryable = true,
            originalException = this
        )
        is java.net.SocketTimeoutException -> AppError(
            category = ErrorCategory.TIMEOUT,
            message = "Request timed out, please try again",
            isRetryable = true,
            originalException = this
        )
        is java.io.IOException -> AppError(
            category = ErrorCategory.NETWORK,
            message = "No internet connection",
            isRetryable = true,
            originalException = this
        )
        else -> {
            // Check if it's a cache-related error by message content
            val isCacheError = message?.contains("cache", ignoreCase = true) == true
            AppError(
                category = if (isCacheError) ErrorCategory.CACHE else ErrorCategory.GENERIC,
                message = when {
                    isCacheError -> "Could not find cached content. Please connect to the internet."
                    else -> "Something went wrong, please try again"
                },
                isRetryable = true,
                originalException = this
            )
        }
    }
}

/**
 * Shows an error message in a snackbar with appropriate styling and actions
 */
fun showErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    error: AppError,
    coroutineScope: CoroutineScope,
    onRetry: (() -> Unit)? = null
) {
    coroutineScope.launch {
        val result = snackbarHostState.showSnackbar(
            message = error.message,
            actionLabel = if (error.isRetryable && onRetry != null) "Retry" else null,
            withDismissAction = true,
            duration = when (error.category) {
                ErrorCategory.NETWORK -> SnackbarDuration.Long
                ErrorCategory.TIMEOUT -> SnackbarDuration.Long
                ErrorCategory.CACHE -> SnackbarDuration.Long
                ErrorCategory.GENERIC -> SnackbarDuration.Short
            }
        )
        
        if (result == SnackbarResult.ActionPerformed && onRetry != null) {
            onRetry()
        }
    }
}

/**
 * Shows an error message in a snackbar using a Throwable
 */
fun showErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    throwable: Throwable,
    coroutineScope: CoroutineScope,
    onRetry: (() -> Unit)? = null
) {
    val appError = throwable.toAppError()
    showErrorSnackbar(snackbarHostState, appError, coroutineScope, onRetry)
}

/**
 * Determines if an error should be shown as a persistent error state vs transient snackbar
 */
fun AppError.shouldShowPersistentError(): Boolean {
    return when (category) {
        ErrorCategory.CACHE -> true // Cache errors should be persistent until resolved
        ErrorCategory.NETWORK -> false // Network errors can be transient
        ErrorCategory.TIMEOUT -> false // Timeout errors can be transient
        ErrorCategory.GENERIC -> false // Generic errors can be transient
    }
}

/**
 * Gets appropriate error recovery suggestions based on error category
 */
fun AppError.getRecoverySuggestion(): String {
    return when (category) {
        ErrorCategory.NETWORK -> "Check your network connection and try again"
        ErrorCategory.TIMEOUT -> "The server is taking too long to respond. Try again in a moment"
        ErrorCategory.CACHE -> "Connect to the internet to fetch new content"
        ErrorCategory.GENERIC -> "Please try again in a moment"
    }
}

/**
 * Determines if an error indicates the user is offline
 */
fun AppError.isOfflineError(): Boolean {
    return category == ErrorCategory.NETWORK || category == ErrorCategory.CACHE
}

/**
 * Gets a short title for the error suitable for UI display
 */
fun AppError.getTitle(): String {
    return when (category) {
        ErrorCategory.NETWORK -> "No Internet Connection"
        ErrorCategory.TIMEOUT -> "Request Timed Out"
        ErrorCategory.CACHE -> "No Cached Content"
        ErrorCategory.GENERIC -> "Something Went Wrong"
    }
}

/**
 * Extension function to handle common error scenarios in ViewModels
 */
fun handleCommonError(
    throwable: Throwable,
    onNetworkError: (AppError) -> Unit,
    onTimeoutError: (AppError) -> Unit,
    onCacheError: (AppError) -> Unit,
    onGenericError: (AppError) -> Unit
) {
    val appError = throwable.toAppError()
    when (appError.category) {
        ErrorCategory.NETWORK -> onNetworkError(appError)
        ErrorCategory.TIMEOUT -> onTimeoutError(appError)
        ErrorCategory.CACHE -> onCacheError(appError)
        ErrorCategory.GENERIC -> onGenericError(appError)
    }
}

/**
 * Enhanced error state management for UI components
 */
data class ErrorState(
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val errorCategory: ErrorCategory? = null,
    val isRetryable: Boolean = true,
    val showPersistentError: Boolean = false,
    val showSnackbar: Boolean = false
) {
    companion object {
        fun fromAppError(appError: AppError): ErrorState {
            return ErrorState(
                hasError = true,
                errorMessage = appError.message,
                errorCategory = appError.category,
                isRetryable = appError.isRetryable,
                showPersistentError = appError.shouldShowPersistentError(),
                showSnackbar = !appError.shouldShowPersistentError()
            )
        }
        
        fun none(): ErrorState = ErrorState()
    }
}

/**
 * Utility function to determine the best error display strategy
 */
fun AppError.getDisplayStrategy(): ErrorDisplayStrategy {
    return when {
        shouldShowPersistentError() -> ErrorDisplayStrategy.PERSISTENT_CARD
        category == ErrorCategory.NETWORK -> ErrorDisplayStrategy.SNACKBAR_WITH_RETRY
        category == ErrorCategory.TIMEOUT -> ErrorDisplayStrategy.SNACKBAR_WITH_RETRY
        else -> ErrorDisplayStrategy.SNACKBAR_SIMPLE
    }
}

/**
 * Enum representing different error display strategies
 */
enum class ErrorDisplayStrategy {
    PERSISTENT_CARD,
    SNACKBAR_WITH_RETRY,
    SNACKBAR_SIMPLE,
    INLINE_MESSAGE
}

/**
 * Enhanced error recovery suggestions with actionable steps
 */
fun AppError.getDetailedRecoverySuggestion(): List<String> {
    return when (category) {
        ErrorCategory.NETWORK -> listOf(
            "Check your Wi-Fi or mobile data connection",
            "Try moving to an area with better signal",
            "Restart your network connection"
        )
        ErrorCategory.TIMEOUT -> listOf(
            "The server is taking too long to respond",
            "Try again in a few moments",
            "Check your internet connection speed"
        )
        ErrorCategory.CACHE -> listOf(
            "Connect to the internet to fetch new content",
            "Previously saved content is not available",
            "Try refreshing when online"
        )
        ErrorCategory.GENERIC -> listOf(
            "An unexpected error occurred",
            "Please try again in a moment",
            "Contact support if the problem persists"
        )
    }
}