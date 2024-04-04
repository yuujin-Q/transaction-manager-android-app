package com.mog.bondoman.data.connection

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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


    companion object {
        const val USER_TOKEN = "user_token"
        const val NIM = "user_nim"
        const val PREF_KEY = "AUTH_PREF"
    }

    init {
        prefs = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    fun saveAuthToken(nim: String, token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(NIM, nim)
        editor.commit()

        _isValidSession.postValue(true)
        Log.d("SessionMan save", this.fetchNim() ?: "empty nim on session")
        Log.d("SessionMan save", this.fetchAuthToken() ?: "empty token on session")
    }

    @SuppressLint("ApplySharedPref")
    fun removeAuthToken() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(NIM)
        editor.commit()

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
}