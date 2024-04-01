package com.mog.bondoman.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mog.bondoman.R
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {
    @Inject
    lateinit var sessionManager: SessionManager
    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
//    private val broadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            Log.d("Broadcast receiver", "receive")
//            if (intent?.action == "TOKEN_CHECK") {
//                val isValid = intent.getBooleanExtra("TOKEN_CHECK_IS_VALID", false)
//                val checkToken = intent.getStringExtra("TOKEN_CHECK_TOKEN")
//                val checkNim = intent.getStringExtra("TOKEN_CHECK_NIM")
//
//                Log.d("Broadcast receiver", "token ${checkToken ?: "no token"}")
//                Log.d("Broadcast receiver", "nim ${checkNim ?: "no nim"}")
////                if (isValid) {
////                    findNavController().navigate(R.id.navigate_to_home)
////                }
//            }
//        }
//    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(requireContext()))
            .get(LoginViewModel::class.java)

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        loginViewModel.credentials.observe(viewLifecycleOwner,
            Observer { creds ->
                creds ?: return@Observer
                if (creds.nim.isNotEmpty() && creds.token.isNotEmpty()) {
                    sessionManager.saveAuthToken(creds.nim, creds.token)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        val token = sessionManager.fetchAuthToken()
        if (token != null) {
//            TODO validate JWT from service?
            val nim = sessionManager.fetchNim()!!
            updateUiWithUser(LoggedInUserView(nim))
        }
    }
//
//    override fun onResume() {
//        super.onResume()
//        requireActivity().registerReceiver(
//            this.broadcastReceiver,
//            IntentFilter("TOKEN_CHECK"),
//            null,
//            null,
//            Context.RECEIVER_NOT_EXPORTED
//        )
//    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()

        findNavController().navigate(R.id.navigate_to_home)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}