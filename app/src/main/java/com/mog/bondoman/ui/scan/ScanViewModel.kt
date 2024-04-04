package com.mog.bondoman.ui.scan

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

import androidx.lifecycle.LiveData

import kotlinx.coroutines.flow.asStateFlow

class ScanViewModel: ViewModel() {

    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap: Bitmap) {
        _bitmaps.value += bitmap
    }

    fun toggleCamera() {
        // Toggle camera logic
    }

    fun openGallery() {
        // Open gallery logic
    }

    fun takePhoto() {
        // Take photo logic
    }

    fun uploadImage(imageBitmap: Bitmap) {
//        // Konversi Bitmap ke File
//        val file = bitmapToFile(imageBitmap, getApplication())
//        // Konversi File ke RequestBody
//        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//        // Buat bagian multipart dari gambar
//        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
//
//        // Kirim permintaan mengunggah gambar ke server
//        apiService.uploadImage(body).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                // Tangani respons dari server jika diperlukan
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                // Tangani kesalahan jika ada
//            }
//        })
    }

    // Metode untuk mengubah Bitmap menjadi File
//    private fun bitmapToFile(bitmap: Bitmap, context: Context): File {
////        val file = File(context.cacheDir, "temp_image.jpg")
////        file.createNewFile()
////
////        val outputStream = FileOutputStream(file)
////        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
////        outputStream.flush()
////        outputStream.close()
////
////        return file
//    }

}
