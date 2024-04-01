package com.mog.bondoman.data.connection

import com.mog.bondoman.data.payload.LoginPayload
import com.mog.bondoman.data.payload.LoginResponse
import com.mog.bondoman.data.payload.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.Part

// retrofit connection
interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body payload: LoginPayload): LoginResponse

    @Multipart
    @POST("api/bill/upload") // Sesuaikan dengan endpoint sebenarnya di server Anda
    fun uploadImage(@Part image: MultipartBody.Part): Call<ResponseBody>

    @POST("/api/auth/token")
    suspend fun checkToken(@Header("Authorization") token: String): TokenResponse
}