package com.adoyo.howtosayno.ui.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Unit tests for error handling utilities
 * 
 * These tests verify:
 * - Correct error categorization
 * - Proper error message formatting
 * - Error recovery suggestions
 * - Offline error detection
 * 
 * Requirements addressed:
 * - 2.1, 2.2, 2.3, 2.4: Error handling verification
 */
class ErrorHandlingUtilsTest {

    @Test
    fun `toAppError converts UnknownHostException to network error`() {
        val exception = UnknownHostException("Unable to resolve host")
        val appError = exception.toAppError()
        
        assertEquals(ErrorCategory.NETWORK, appError.category)
        assertEquals("No internet connection", appError.message)
        assertTrue(appError.isRetryable)
        assertTrue(appError.isOfflineError())
    }

    @Test
    fun `toAppError converts SocketTimeoutException to timeout error`() {
        val exception = SocketTimeoutException("Read timed out")
        val appError = exception.toAppError()
        
        assertEquals(ErrorCategory.TIMEOUT, appError.category)
        assertEquals("Request timed out, please try again", appError.message)
        assertTrue(appError.isRetryable)
        assertFalse(appError.isOfflineError())
    }

    @Test
    fun `toAppError converts IOException to network error`() {
        val exception = IOException("Network is unreachable")
        val appError = exception.toAppError()
        
        assertEquals(ErrorCategory.NETWORK, appError.category)
        assertEquals("No internet connection", appError.message)
        assertTrue(appError.isRetryable)
        assertTrue(appError.isOfflineError())
    }

    @Test
    fun `toAppError converts cache-related exception to cache error`() {
        val exception = RuntimeException("Cache not found")
        val appError = exception.toAppError()
        
        assertEquals(ErrorCategory.CACHE, appError.category)
        assertEquals("Could not find cached content. Please connect to the internet.", appError.message)
        assertTrue(appError.isRetryable)
        assertTrue(appError.isOfflineError())
    }

    @Test
    fun `toAppError converts generic exception to generic error`() {
        val exception = RuntimeException("Something unexpected happened")
        val appError = exception.toAppError()
        
        assertEquals(ErrorCategory.GENERIC, appError.category)
        assertEquals("Something went wrong, please try again", appError.message)
        assertTrue(appError.isRetryable)
        assertFalse(appError.isOfflineError())
    }

    @Test
    fun `getTitle returns correct titles for different error categories`() {
        val networkError = AppError(ErrorCategory.NETWORK, "Network error")
        val timeoutError = AppError(ErrorCategory.TIMEOUT, "Timeout error")
        val cacheError = AppError(ErrorCategory.CACHE, "Cache error")
        val genericError = AppError(ErrorCategory.GENERIC, "Generic error")
        
        assertEquals("No Internet Connection", networkError.getTitle())
        assertEquals("Request Timed Out", timeoutError.getTitle())
        assertEquals("No Cached Content", cacheError.getTitle())
        assertEquals("Something Went Wrong", genericError.getTitle())
    }

    @Test
    fun `getRecoverySuggestion returns appropriate suggestions`() {
        val networkError = AppError(ErrorCategory.NETWORK, "Network error")
        val timeoutError = AppError(ErrorCategory.TIMEOUT, "Timeout error")
        val cacheError = AppError(ErrorCategory.CACHE, "Cache error")
        val genericError = AppError(ErrorCategory.GENERIC, "Generic error")
        
        assertEquals("Check your network connection and try again", networkError.getRecoverySuggestion())
        assertEquals("The server is taking too long to respond. Try again in a moment", timeoutError.getRecoverySuggestion())
        assertEquals("Connect to the internet to fetch new content", cacheError.getRecoverySuggestion())
        assertEquals("Please try again in a moment", genericError.getRecoverySuggestion())
    }

    @Test
    fun `shouldShowPersistentError returns correct values`() {
        val networkError = AppError(ErrorCategory.NETWORK, "Network error")
        val timeoutError = AppError(ErrorCategory.TIMEOUT, "Timeout error")
        val cacheError = AppError(ErrorCategory.CACHE, "Cache error")
        val genericError = AppError(ErrorCategory.GENERIC, "Generic error")
        
        assertFalse(networkError.shouldShowPersistentError())
        assertFalse(timeoutError.shouldShowPersistentError())
        assertTrue(cacheError.shouldShowPersistentError())
        assertFalse(genericError.shouldShowPersistentError())
    }

    @Test
    fun `isOfflineError correctly identifies offline errors`() {
        val networkError = AppError(ErrorCategory.NETWORK, "Network error")
        val timeoutError = AppError(ErrorCategory.TIMEOUT, "Timeout error")
        val cacheError = AppError(ErrorCategory.CACHE, "Cache error")
        val genericError = AppError(ErrorCategory.GENERIC, "Generic error")
        
        assertTrue(networkError.isOfflineError())
        assertFalse(timeoutError.isOfflineError())
        assertTrue(cacheError.isOfflineError())
        assertFalse(genericError.isOfflineError())
    }
}