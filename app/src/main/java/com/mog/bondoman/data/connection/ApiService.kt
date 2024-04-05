package com.mog.bondoman.data.connection

import com.mog.bondoman.data.payload.LoginPayload
import com.mog.bondoman.data.payload.LoginResponse
import com.mog.bondoman.data.payload.TokenResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

// retrofit connection
interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body payload: LoginPayload): LoginResponse

    @Multipart
    @POST("api/bill/upload")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): ResponseBody

    @POST("/api/auth/token")
    suspend fun checkToken(@Header("Authorization") token: String): TokenResponse
}