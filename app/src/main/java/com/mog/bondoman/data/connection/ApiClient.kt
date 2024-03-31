package com.mog.bondoman.data.connection

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// retrofit client
class ApiClient {
    private lateinit var apiService: ApiService
    private lateinit var moshi: Moshi
    private var baseUrl = "https://pbd-backend-2024.vercel.app/"

    fun getApiService(): ApiService {
        if (!::apiService.isInitialized) {
            moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val retrofit =
                Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }
}