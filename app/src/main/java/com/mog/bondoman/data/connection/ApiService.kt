package com.mog.bondoman.data.connection

import com.mog.bondoman.data.model.LoginResponse
import com.mog.bondoman.data.payload.LoginPayload
import retrofit2.http.Body
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
}