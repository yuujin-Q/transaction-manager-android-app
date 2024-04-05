package com.mog.bondoman.data.connection

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mog.bondoman.data.Result
import com.mog.bondoman.data.model.LoggedInUser
import com.mog.bondoman.data.payload.LoginPayload
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SessionManagerProvider {
    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }
}

class SessionManager(context: Context) {
    private var prefs: SharedPreferences
    private val _isValidSession = MutableLiveData<Boolean>()
    val isValidSession: LiveData<Boolean> = _isValidSession
    private val apiClient = ApiClient()


    companion object {
        const val USER_TOKEN = "user_token"
        const val NIM = "user_nim"
        const val PREF_KEY = "AUTH_PREF"
    }

    init {
        prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    fun saveAuthToken(nim: String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(NIM, nim)
        editor.apply()

        _isValidSession.postValue(true)
        Log.d("SessionMan save", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan save", this.fetchAuthToken() ?: "empty token on session")
    }

    fun removeAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(NIM)
        editor.apply()

        _isValidSession.postValue(false)
        Log.d("SessionMan remove", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan remove", this.fetchAuthToken() ?: "empty token on session")
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchNim(): String? {
        return prefs.getString(NIM, null)
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        // handle login
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
            } catch (e: Exception) {
                Log.e("LoginDataSource", e.message ?: "Error Login")
                Result.Error(e)
            }
        }
    }

    suspend fun tokenCheck(): Boolean {
        return withContext(Dispatchers.IO) {
            val token = fetchAuthToken()
            try {
                val response = apiClient.getApiService().checkToken("Bearer $token")

                Log.d("SessionMan Token", "Session valid, " + response.nim)
                _isValidSession.postValue(true)
                return@withContext true
            } catch (e: Throwable) {
                Log.e("SessionMan Token", e.message ?: "Error Validating JWT")
                _isValidSession.postValue(false)
                return@withContext false
            }
        }
    }

}