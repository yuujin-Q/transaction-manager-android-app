package com.mog.bondoman.data

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

                val loggedInUser = LoggedInUser(email, token)
                Result.Success(loggedInUser)
            } catch (e: Throwable) {
                Result.Error(IOException("Error logging in", e))
            }
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}