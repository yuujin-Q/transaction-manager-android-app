package com.mog.bondoman.ui.scan

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import com.mog.bondoman.data.connection.ApiService

class ScanViewModel(application: Application) : AndroidViewModel(application) {

    // Inisialisasi Retrofit
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            // Ganti baseUrl dengan URL base endpoint server Anda
            .baseUrl("https://pbd-backend-2024.vercel.app/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Inisialisasi ApiService
        apiService = retrofit.create(ApiService::class.java)
    }

    // Metode untuk mengunggah gambar ke server
    fun uploadImage(imageBitmap: Bitmap) {
        // Konversi Bitmap ke File
        val file = bitmapToFile(imageBitmap, getApplication())
        // Konversi File ke RequestBody
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        // Buat bagian multipart dari gambar
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // Kirim permintaan mengunggah gambar ke server
        apiService.uploadImage(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                // Tangani respons dari server jika diperlukan
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Tangani kesalahan jika ada
            }
        })
    }

    // Metode untuk mengubah Bitmap menjadi File
    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
        val file = File(context.cacheDir, "temp_image.jpg")
        file.createNewFile()

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file
    }
}

