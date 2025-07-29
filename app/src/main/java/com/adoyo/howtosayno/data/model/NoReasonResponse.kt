package com.adoyo.howtosayno.data.model

import kotlinx.serialization.Serializable

/**
 * Data class representing the response from the No Reason API
 * API endpoint: https://naas.isalman.dev/no
 * Response format: {"reason": "This feels like something Future Me would yell at Present Me for agreeing to."}
 */
@Serializable
data class NoReasonResponse(
    val reason: String
)