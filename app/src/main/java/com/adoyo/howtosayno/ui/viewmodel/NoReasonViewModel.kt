package com.adoyo.howtosayno.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adoyo.howtosayno.domain.usecase.GetNoReasonUseCase
import com.adoyo.howtosayno.domain.usecase.NoReasonCachedException
import com.adoyo.howtosayno.ui.state.NoReasonUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing No Reason screen state and business logic
 * 
 * This ViewModel orchestrates the interaction between the UI and the domain layer,
 * managing all UI states including loading, content, error, and offline states.
 * It implements proper coroutine scoping and error handling.
 * 
 * Requirements addressed:
 * - 1.3: Display reason text to user
 * - 2.1, 2.2, 2.3, 2.4: Error handling and retry logic
 * - 3.1, 3.2, 3.3, 3.4: Loading state management
 * - 4.1, 4.2, 4.3, 4.4: Button interaction and state management
 * - 5.1, 5.2, 5.3, 5.4: Offline support and caching
 */
@HiltViewModel
class NoReasonViewModel @Inject constructor(
    private val getNoReasonUseCase: GetNoReasonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoReasonUiState())
    val uiState: StateFlow<NoReasonUiState> = _uiState.asStateFlow()

    init {
        // The use case's offline-first logic handles both initial cache load and network fetch.
        fetchNewReason()
    }

    fun fetchNewReason() {
        viewModelScope.launch {
            getNoReasonUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, isButtonEnabled = false) }
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { reason ->
                            _uiState.update {
                                it.copy(
                                    reason = reason,
                                    isLoading = false,
                                    error = null,
                                    isOffline = false,
                                    hasCache = true,
                                    isButtonEnabled = true
                                )
                            }
                        },
                        onFailure = { throwable ->
                            handleError(throwable)
                        }
                    )
                }
        }
    }

    fun retry() {
        fetchNewReason()
    }

    private fun handleError(throwable: Throwable) {
        val isOffline = when (throwable) {
            is java.net.SocketTimeoutException -> false // Timeout is not necessarily offline
            is java.net.UnknownHostException -> true
            is java.io.IOException -> true
            else -> false
        }

        val errorMessage = when (throwable) {
            is java.net.SocketTimeoutException -> "Request timed out, please try again"
            is java.net.UnknownHostException -> "No internet connection"
            is java.io.IOException -> "No internet connection"
            is NoReasonCachedException -> "Could not find a reason. Please connect to the internet."
            else -> "Something went wrong, please try again"
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                error = errorMessage,
                isOffline = isOffline,
                isButtonEnabled = true
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}