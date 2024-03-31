package com.mog.bondoman.data

import android.util.Log
import com.mog.bondoman.data.connection.ApiClient
import com.mog.bondoman.data.model.LoggedInUser
import com.mog.bondoman.data.payload.LoginPayload
import com.mog.bondoman.data.payload.TokenResponse
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

                Log.d("Datasource", "Before checkToken")
                val tokenCheck = withContext(Dispatchers.IO) {
                    apiClient.getApiService()
                        .checkToken(token = "Bearer $token")
                }
                Log.d("Datasource", "After checkToken")
                Log.d("Datasource", tokenCheck.nim)


                val loggedInUser = LoggedInUser(tokenCheck.nim, token)
                Result.Success(loggedInUser)
            } catch (e: Throwable) {
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    suspend fun tokenCheck(token: String): Result<TokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiClient.getApiService()
                        .checkToken(token)
                }

                Result.Success(response)
            } catch (e: Throwable) {
                Result.Error(IOException("Failed token check", e))
            }
        }
    }
}