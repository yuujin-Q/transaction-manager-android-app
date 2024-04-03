package com.mog.bondoman.data

import android.util.Log
import com.mog.bondoman.data.connection.ApiClient
import com.mog.bondoman.data.model.LoggedInUser
import com.mog.bondoman.data.payload.LoginPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource(private val apiClient: ApiClient) {

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        return withContext(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiClient.getApiService()
                        .login(LoginPayload(email = email, password = password))
                }

                val token = response.token

                val tokenCheck = withContext(Dispatchers.IO) {
                    apiClient.getApiService()
                        .checkToken(token = "Bearer $token")
                }

                val loggedInUser = LoggedInUser(tokenCheck.nim, token)
                Result.Success(loggedInUser)
            } catch (e: Throwable) {
                Log.d("DataSource", e.message ?: "Error")
                Result.Error(IOException(e.message, e))
            }
        }
    }
}