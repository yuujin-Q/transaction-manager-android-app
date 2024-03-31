package com.mog.bondoman.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.data.LoginDataSource
import com.mog.bondoman.data.LoginRepository
import com.mog.bondoman.data.connection.ApiClient

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource(apiClient = ApiClient())
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}