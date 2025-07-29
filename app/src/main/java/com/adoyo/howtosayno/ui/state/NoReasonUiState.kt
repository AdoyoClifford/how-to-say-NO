package com.adoyo.howtosayno.ui.state

/**
 * UI state for the No Reason screen
 * 
 * This data class represents all possible states of the UI:
 * - Loading state while fetching data
 * - Content state with reason text
 * - Error state with error message
 * - Offline state indicator
 * - Cache availability indicator
 * 
 * Requirements addressed:
 * - 1.3: Display reason text to user
 * - 2.1, 2.2, 2.3: Error handling states
 * - 3.1, 3.2, 3.3: Loading indicator states
 * - 4.3: Button state management
 * - 5.2, 5.3: Offline and cache states
 */
data class NoReasonUiState(
    val reason: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isOffline: Boolean = false,
    val hasCache: Boolean = false,
    val isButtonEnabled: Boolean = true
) {
    /**
     * Convenience property to check if we're in an error state
     */
    val hasError: Boolean get() = error != null
    
    /**
     * Convenience property to check if we have content to display
     */
    val hasContent: Boolean get() = reason.isNotEmpty() && !hasError
    
    /**
     * Convenience property to determine if we should show the retry button
     */
    val shouldShowRetry: Boolean get() = hasError && !isLoading
}