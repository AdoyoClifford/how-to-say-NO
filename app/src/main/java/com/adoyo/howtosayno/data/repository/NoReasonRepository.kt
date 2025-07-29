package com.adoyo.howtosayno.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing No Reason data
 * Provides reactive data streams using Kotlin Flow for real-time updates
 * Implements offline-first strategy with local caching
 */
interface NoReasonRepository {
    
    /**
     * Fetches a new reason from the API and emits the result
     * Automatically caches successful responses for offline access
     * 
     * @return Flow<Result<String>> emitting the reason text or error states
     */
    fun fetchNoReason(): Flow<Result<String>>
    
    /**
     * Observes the cached reason data
     * Emits updates whenever the cached reason changes
     * 
     * @return Flow<String?> emitting the cached reason or null if no cache exists
     */
    fun observeCachedReason(): Flow<String?>
    
    /**
     * Retrieves the most recently cached reason as a one-time operation
     * 
     * @return String? the cached reason or null if no cache exists
     */
    suspend fun getCachedReason(): String?
    
    /**
     * Manually cache a reason for offline access
     * This will trigger emissions on observeCachedReason()
     * 
     * @param reason The reason text to cache
     */
    suspend fun cacheReason(reason: String)
    
    /**
     * Observes whether cached reasons are available
     * 
     * @return Flow<Boolean> emitting true if cache exists, false otherwise
     */
    fun observeHasCachedReason(): Flow<Boolean>
    
    /**
     * Checks if there are any cached reasons available as a one-time operation
     * 
     * @return Boolean true if cache exists, false otherwise
     */
    suspend fun hasCachedReason(): Boolean
    
    /**
     * Clears all cached reasons
     * This will trigger emissions on observeCachedReason() and observeHasCachedReason()
     */
    suspend fun clearCache()
}