package com.mog.bondoman.data.connection

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log


class SessionManager private constructor(private val prefs: SharedPreferences) {
    companion object {
        const val USER_TOKEN = "user_token"
        const val NIM = "user_nim"
        const val PREF_KEY = "AUTH_PREF"

        private var instance: SessionManager? = null
        
        fun getInstance(prefs: SharedPreferences) = instance ?: synchronized(this) {
            instance ?: SessionManager(prefs).also { instance = it }
        }
    }

    @SuppressLint("ApplySharedPref")
    fun saveAuthToken(nim: String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(NIM, nim)
        editor.commit()

        Log.d("SessionMan save", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan save", this.fetchAuthToken() ?: "empty token on session")
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchNim(): String? {
        return prefs.getString(NIM, null)
    }

    @SuppressLint("ApplySharedPref")
    fun removeAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(NIM)
        editor.commit()

        Log.d("SessionMan remove", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan remove", this.fetchAuthToken() ?: "empty token on session")
    }
}