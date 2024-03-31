package com.mog.bondoman.data.connection

import com.mog.bondoman.data.model.LoginResponse
import com.mog.bondoman.data.payload.LoginPayload
import retrofit2.http.Body
import retrofit2.http.POST

// retrofit connection
interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body payload: LoginPayload): LoginResponse
}