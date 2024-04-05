package com.mog.bondoman.data

import com.mog.bondoman.data.connection.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanPhotoRepository {
    private val apiService = ApiClient().getApiService()

    suspend fun uploadPhotos(token: String, file: File): Deferred<Boolean> {
        return CoroutineScope(Dispatchers.IO).async {
            try {
                val fileParts =
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        file.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                apiService.uploadImage(token = "Bearer $token", file = fileParts)

                return@async true
            } catch (e: Exception) {
                return@async false
            }
        }
    }
}