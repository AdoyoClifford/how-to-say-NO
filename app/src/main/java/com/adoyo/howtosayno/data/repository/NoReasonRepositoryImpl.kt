package com.adoyo.howtosayno.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.adoyo.howtosayno.data.api.NoReasonApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// DataStore extension for Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "no_reason_cache")

/**
 * Implementation of NoReasonRepository using Retrofit API service and DataStore caching
 * Provides offline-first data access with reactive error handling
 */
@Singleton
class NoReasonRepositoryImpl @Inject constructor(
    private val apiService: NoReasonApiService,
    private val context: Context
) : NoReasonRepository {
    
    companion object {
        private val KEY_CACHED_REASON = stringPreferencesKey("cached_reason")
        private val KEY_CACHE_TIMESTAMP = longPreferencesKey("cache_timestamp")
        private const val CACHE_EXPIRY_HOURS = 1L // Cache expiry time in hours
    }
    
    private val dataStore = context.dataStore
    
    override fun fetchNoReason(): Flow<Result<String>> = flow {
        // Make API call directly - rely on reactive error handling with catch operator
        val response = apiService.getNoReason()
        val reason = response.reason

        // Cache the successful response
        cacheReason(reason)

        // Emit success result
        emit(Result.success(reason))

    }.catch { exception ->
        // Handle different types of exceptions with reactive error handling
        val errorMessage = when (exception) {
            is java.net.SocketTimeoutException -> "Request timed out, please try again"
            is java.net.UnknownHostException -> "Unable to connect to server"
            is java.io.IOException -> "Network error occurred"
            else -> "Something went wrong: ${exception.message}"
        }

        // Try to fallback to cached reason on error
        val cachedReason = getCachedReason()
        if (cachedReason != null) {
            emit(Result.success(cachedReason))
        } else {
            emit(Result.failure(ApiException(errorMessage, exception)))
        }
    }
    
    override fun observeCachedReason(): Flow<String?> {
        return dataStore.data
            .map { preferences ->
                val cachedReason = preferences[KEY_CACHED_REASON]
                val cacheTimestamp = preferences[KEY_CACHE_TIMESTAMP] ?: 0L
                
                // Check if cache is expired (optional - can be removed if not needed)
                val currentTime = System.currentTimeMillis()
                val cacheAge = currentTime - cacheTimestamp
                val cacheExpiryMillis = CACHE_EXPIRY_HOURS * 60 * 60 * 1000
                
                when {
                    cachedReason != null && cacheAge < cacheExpiryMillis -> cachedReason
                    cachedReason != null -> cachedReason // Cache expired but still return it for offline scenarios
                    else -> null
                }
            }
            .catch { emit(null) } // Handle any DataStore errors gracefully
    }
    
    override suspend fun getCachedReason(): String? {
        return try {
            observeCachedReason().first()
        } catch (e: Exception) {
            null // Handle any DataStore errors gracefully
        }
    }
    
    override suspend fun cacheReason(reason: String) {
        try {
            dataStore.edit { preferences ->
                preferences[KEY_CACHED_REASON] = reason
                preferences[KEY_CACHE_TIMESTAMP] = System.currentTimeMillis()
            }
        } catch (e: Exception) {
            // Handle DataStore write errors gracefully - don't crash the app
        }
    }
    
    override fun observeHasCachedReason(): Flow<Boolean> {
        return observeCachedReason().map { it != null }
    }
    
    override suspend fun hasCachedReason(): Boolean {
        return getCachedReason() != null
    }
    
    override suspend fun clearCache() {
        try {
            dataStore.edit { preferences ->
                preferences.remove(KEY_CACHED_REASON)
                preferences.remove(KEY_CACHE_TIMESTAMP)
            }
        } catch (e: Exception) {
            // Handle DataStore write errors gracefully - don't crash the app
        }
    }
    

}



/**
 * Custom exception for API-related errors
 */
class ApiException(message: String, cause: Throwable? = null) : Exception(message, cause)