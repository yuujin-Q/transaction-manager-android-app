package com.mog.bondoman.ui.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mog.bondoman.R
import com.mog.bondoman.data.LoginRepository
import com.mog.bondoman.data.Result
import com.mog.bondoman.data.model.LoggedInUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _credentials = MutableLiveData<LoggedInUser>()
    val credentials: LiveData<LoggedInUser> = _credentials

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = loginRepository.login(username, password)) {
                is Result.Success -> {
                    _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = result.data.nim)))
                    _credentials.postValue(
                        LoggedInUser(
                            nim = result.data.nim,
                            token = result.data.token
                        )
                    )
                }

                is Result.Error -> {
                    Log.d("LoginVM", result.exception.message ?: "Error VM Login")
                    _loginResult.postValue(
                        LoginResult(
                            error = if (result.exception.message!!.startsWith(
                                    "HTTP 4"
                                )
                            ) R.string.invalid_login else R.string.login_failed
                        )
                    )
                    _credentials.postValue(LoggedInUser(nim = "", token = ""))
                }

            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}