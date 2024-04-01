package com.mog.bondoman.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val nim: String,
    val token: String
)