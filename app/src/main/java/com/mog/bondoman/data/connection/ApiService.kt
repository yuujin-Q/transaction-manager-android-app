package com.mog.bondoman.data.connection

import com.mog.bondoman.data.payload.LoginPayload
import com.mog.bondoman.data.payload.LoginResponse
import com.mog.bondoman.data.payload.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// retrofit connection
interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body payload: LoginPayload): LoginResponse

    @POST("/api/auth/token")
    suspend fun checkToken(@Header("Authorization") token: String): TokenResponse
}