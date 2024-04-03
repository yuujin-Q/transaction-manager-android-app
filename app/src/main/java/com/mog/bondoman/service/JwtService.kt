package com.mog.bondoman.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.mog.bondoman.data.connection.ApiClient
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.data.model.LoggedInUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class JwtService : Service() {
    private val interval: Long = 60 * 1000      // per minute
    private val apiClient = ApiClient()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                Log.d("JWT Cached", sessionManager.fetchNim() ?: "no nim")
                Log.d("JWT Cached", sessionManager.fetchAuthToken() ?: "no token")
                val tokenValidation = validateToken()

                if (tokenValidation == null) {
                    Log.d("JWT Validate", "Invalid JWT")
                    sessionManager.removeAuthToken()
                }
                delay(interval)
            }

        }

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private suspend fun validateToken(): LoggedInUser? {
        return withContext(Dispatchers.IO) {
            val token = sessionManager.fetchAuthToken()
            Log.d("JWT Validate", token ?: "no token to validate")
            if (token == null) {
                null
            } else {
                try {
                    val response =
                        apiClient.getApiService().checkToken(token)

                    LoggedInUser(response.nim, token)
                } catch (e: Throwable) {
                    Log.e("JWT Validate", e.message ?: "error on validating jwt")
                    null
                }
            }
        }
//        TODO: fix error validate after 1 minute
    }
}