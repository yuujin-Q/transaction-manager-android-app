package com.mog.bondoman.data.payload

data class TokenResponse(
    val nim: String,
    val iat: Long,
    val exp: Long
)
