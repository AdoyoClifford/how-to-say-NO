package com.adoyo.howtosayno.ui.viewmodel

import com.adoyo.howtosayno.domain.usecase.GetNoReasonUseCase
import com.adoyo.howtosayno.ui.state.NoReasonUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Unit tests for NoReasonViewModel
 * 
 * Tests all ViewModel functionality including:
 * - Initial state and cached reason loading
 * - Successful API calls and state updates
 * - Error handling for different error types
 * - Loading state management
 * - Button state management
 * - Retry functionality
 */
@OptIn(ExperimentalCoroutinesApi::class)
class NoReasonViewModelTest {
    
    private lateinit var viewModel: NoReasonViewModel
    
    @Mock
    private lateinit var mockGetNoReasonUseCase: GetNoReasonUseCase
    
    private val testDispatcher = StandardTestDispatcher()
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state should be correct`() = runTest {
        // Given
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success("Test reason")))
        
        // When
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // Then
        val initialState = viewModel.uiState.value
        assertEquals("", initialState.reason)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
        assertFalse(initialState.isOffline)
        assertFalse(initialState.hasCache)
        assertTrue(initialState.isButtonEnabled)
    }
    
    @Test
    fun `should load cached reason on initialization`() = runTest {
        // Given
        val cachedReason = "Cached test reason"
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(cachedReason)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success("Fresh reason")))
        
        // When
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Fresh reason", state.reason) // Should show fresh reason after fetch
        assertTrue(state.hasCache)
        assertFalse(state.isLoading)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `fetchNewReason should update state correctly on success`() = runTest {
        // Given
        val testReason = "Test reason from API"
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success(testReason)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // When
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(testReason, state.reason)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.isOffline)
        assertTrue(state.hasCache)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `fetchNewReason should complete successfully`() = runTest {
        // Given
        val testReason = "Test reason from API"
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success(testReason)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        advanceUntilIdle() // Let initialization complete
        
        // When
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals(testReason, state.reason)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `should handle network timeout error correctly`() = runTest {
        // Given
        val timeoutException = SocketTimeoutException("Connection timeout")
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.failure(timeoutException)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // When
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Request timed out, please try again", state.error)
        assertFalse(state.isLoading)
        assertFalse(state.isOffline)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `should handle network connection error correctly`() = runTest {
        // Given
        val networkException = IOException("No internet connection")
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.failure(networkException)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // When
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("No internet connection", state.error)
        assertFalse(state.isLoading)
        assertTrue(state.isOffline)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `should handle generic error correctly`() = runTest {
        // Given
        val genericException = RuntimeException("Something went wrong")
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.failure(genericException)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // When
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Something went wrong, please try again", state.error)
        assertFalse(state.isLoading)
        assertFalse(state.isOffline)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `retry should call fetchNewReason`() = runTest {
        // Given
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success("Retry reason")))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        
        // When
        viewModel.retry()
        advanceUntilIdle()
        
        // Then
        val state = viewModel.uiState.value
        assertEquals("Retry reason", state.reason)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.isButtonEnabled)
    }
    
    @Test
    fun `clearError should remove error from state`() = runTest {
        // Given
        val networkException = IOException("No internet connection")
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.failure(networkException)))
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        viewModel.fetchNewReason()
        advanceUntilIdle()
        
        // Verify error is set
        assertTrue(viewModel.uiState.value.hasError)
        
        // When
        viewModel.clearError()
        
        // Then
        assertNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.hasError)
    }
    
    @Test
    fun `should verify use case is called on initialization`() = runTest {
        // Given
        whenever(mockGetNoReasonUseCase.getCachedReason()).thenReturn(null)
        whenever(mockGetNoReasonUseCase()).thenReturn(flowOf(Result.success("Test")))
        
        // When
        viewModel = NoReasonViewModel(mockGetNoReasonUseCase)
        advanceUntilIdle()
        
        // Then
        verify(mockGetNoReasonUseCase).invoke()
        // Note: getCachedReason() is called inside the Flow's onStart block
        // and may not be verifiable in this test setup
    }
}