package com.mog.bondoman.data

import android.content.Context
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource, val context: Context) {
    private val sessionManager = SessionManager.getInstance()

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        return withContext(Dispatchers.IO) {
            val result = dataSource.login(username, password)

            if (result is Result.Success) {
                setLoggedInUser(result.data)
            }

            result
        }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser?) {
        this.user = loggedInUser
        if (loggedInUser == null) {
            sessionManager.removeAuthToken()
        } else {
            sessionManager.saveAuthToken(loggedInUser.nim, loggedInUser.token)
        }
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}