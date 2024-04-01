package com.mog.bondoman.data

import com.mog.bondoman.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        return withContext(Dispatchers.IO) {
            val result = dataSource.login(username, password)

            result
        }
    }
}