package com.adoyo.howtosayno.data.api

import com.adoyo.howtosayno.data.model.NoReasonResponse
import retrofit2.http.GET

/**
 * API service interface for the No Reason API
 * Base URL: https://naas.isalman.dev/
 * 
 * This interface defines the API endpoints for fetching creative reasons
 * for saying no to requests.
 */
interface NoReasonApiService {
    
    /**
     * Fetches a creative reason for saying no from the API
     * 
     * @return NoReasonResponse containing the reason text
     * @throws Exception for network errors, timeouts, or API errors
     */
    @GET("no")
    suspend fun getNoReason(): NoReasonResponse
}