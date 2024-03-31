package com.mog.bondoman.data.connection

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.mog.bondoman.R


class SessionManager {
    companion object {
        const val USER_TOKEN = "user_token"
        const val NIM = "user_nim"
        private lateinit var applicationContext: Context

        fun initialize(context: Context) {
            if (!::applicationContext.isInitialized) {
                applicationContext = context.applicationContext
            }
        }

        fun getInstance(): SessionManager {
            if (!::applicationContext.isInitialized) {
                throw IllegalStateException("SessionManager must be initialized before use")
            }
            return SessionManager()
        }
    }

    private var prefs: SharedPreferences =
        applicationContext.getSharedPreferences(
            applicationContext.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )

    fun saveAuthToken(nim: String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(NIM, nim)
        editor.apply()

        Log.d("SessionMan save", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan save", this.fetchAuthToken() ?: "empty token on session")
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchNim(): String? {
        return prefs.getString(NIM, null)
    }

    fun removeAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(NIM)
        editor.apply()

        Log.d("SessionMan remove", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan remove", this.fetchAuthToken() ?: "empty token on session")
    }
}