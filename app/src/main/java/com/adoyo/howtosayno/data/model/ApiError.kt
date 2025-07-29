package com.adoyo.howtosayno.data.model

/**
 * Sealed class representing different types of API errors
 * Used for consistent error handling throughout the app
 */
sealed class ApiError : Exception() {
    
    /**
     * Network connectivity issues
     */
    object NetworkError : ApiError() {
        override val message: String = "No internet connection"
    }
    
    /**
     * Request timeout errors
     */
    object TimeoutError : ApiError() {
        override val message: String = "Request timed out, please try again"
    }
    
    /**
     * Server errors (5xx status codes)
     */
    data class ServerError(val code: Int) : ApiError() {
        override val message: String = "Service temporarily unavailable"
    }
    
    /**
     * Client errors (4xx status codes)
     */
    data class ClientError(val code: Int) : ApiError() {
        override val message: String = "Request failed with error code: $code"
    }
    
    /**
     * JSON parsing or serialization errors
     */
    object ParseError : ApiError() {
        override val message: String = "Something went wrong"
    }
    
    /**
     * Unknown or unexpected errors
     */
    data class UnknownError(override val message: String) : ApiError()
}