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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class JwtService : Service() {
    private val interval: Long = 60 * 1000      // per minute
    private val apiClient = ApiClient()

    @Inject
    lateinit var sessionManager: SessionManager
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceScope.launch {
            while (true) {
                val tokenValidation = validateToken()
                Log.d("JWT", "Process ${android.os.Process.myPid()}")
                Log.d("JWT", tokenValidation?.nim ?: "no nim")
                Log.d("JWT", tokenValidation?.token ?: "no token")

                val broadcaster = Intent().apply {
                    action = "TOKEN_CHECK"
                    putExtra("TOKEN_CHECK_IS_VALID", tokenValidation != null)
                    putExtra("TOKEN_CHECK_TOKEN", tokenValidation?.token)
                    putExtra("TOKEN_CHECK_NIM", tokenValidation?.nim)
                }

//                TODO: fix jwt service
                Log.d("JWT", "Send broadcast")
                sendBroadcast(broadcaster)

//                if (tokenValidation == null) {
//                    sessionManager.removeAuthToken()
//                }

                Thread.sleep(interval)
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
            if (token == null) {
                null
            } else {
                try {
                    val response =
                        apiClient.getApiService().checkToken(token)

                    LoggedInUser(response.nim, token)
                } catch (e: Throwable) {
                    null
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
}