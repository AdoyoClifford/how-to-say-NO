package com.adoyo.howtosayno.domain.usecase

import com.adoyo.howtosayno.data.repository.NoReasonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for getting a "No" reason with offline-first strategy
 * 
 * This use case orchestrates the repository calls to implement an offline-first approach:
 * 1. First checks if cached reasons are available
 * 2. If cache exists, emits cached reason immediately
 * 3. Then attempts to fetch fresh data from API
 * 4. Handles all error scenarios gracefully
 * 
 * Requirements addressed:
 * - 1.1: Fetch reason from API
 * - 1.2: Parse and display reason text
 * - 5.1: Cache successful responses
 * - 5.2: Display cached reasons when offline
 * - 5.3: Show appropriate message when no cache and offline
 * - 5.4: Load most recent cached reason on app start
 */
@Singleton
class GetNoReasonUseCase @Inject constructor(
    private val repository: NoReasonRepository
) {
    
    /**
     * Executes the offline-first strategy for getting a reason.
     * 
     * This Flow will first emit the cached reason if it exists, and then
     * execute the network fetch, emitting its result (either fresh data,
     * the same cached data on network failure, or a final error).
     * 
     * @return Flow<Result<String>> emitting reason text or error states
     */
    operator fun invoke(): Flow<Result<String>> {
        return repository.fetchNoReason()
            .onStart {
                // onStart runs before the main flow starts collecting.
                // We can emit the cached value here for an instant UI update.
                val cachedReason = repository.getCachedReason()
                if (cachedReason != null) {
                    emit(Result.success(cachedReason))
                }
            }
    }
    
    /**
     * Gets the most recent cached reason without making API calls
     * Useful for app startup to show last known reason immediately
     * 
     * @return String? the cached reason or null if no cache exists
     */
    suspend fun getCachedReason(): String? = repository.getCachedReason()
    
    /**
     * Observes cached reason changes reactively
     * Useful for UI that needs to react to cache updates
     * 
     * @return Flow<Result<String>> emitting cached reason updates or errors
     */
    fun observeCachedReason(): Flow<Result<String>> = 
        repository.observeCachedReason()
            .map { cachedReason ->
                if (cachedReason != null) {
                    Result.success(cachedReason)
                } else {
                    Result.failure(NoReasonCachedException("No cached reasons available"))
                }
            }
            .catch { error ->
                emit(Result.failure(error))
            }
    
    /**
     * Checks if cached reasons are available
     * 
     * @return Flow<Boolean> emitting true if cache exists, false otherwise
     */
    fun hasCachedReason(): Flow<Boolean> = repository.observeHasCachedReason()
    
    /**
     * Clears all cached reasons
     * Useful for testing or user-initiated cache clearing
     */
    suspend fun clearCache() {
        repository.clearCache()
    }
}

/**
 * Exception thrown when no cached reasons are available
 */
class NoReasonCachedException(message: String) : Exception(message)