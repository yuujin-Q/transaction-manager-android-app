package com.mog.bondoman

import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.data.connection.SessionManager
import com.mog.bondoman.databinding.ActivityMainBinding
import com.mog.bondoman.model.database.TransactionDatabase
import com.mog.bondoman.repository.TransactionRepository
import com.mog.bondoman.service.JwtService
import com.mog.bondoman.ui.transaction.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var jwtServiceIntent: Intent
    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager


    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Main", "Process ${android.os.Process.myPid()}")

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // setup jwt service for checking saved session token
        jwtServiceIntent = Intent(this.applicationContext, JwtService::class.java)
        startService(jwtServiceIntent)

        // initialize transaction repository
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        val transactionDB = TransactionDatabase.getInstance(baseContext)
        val transactionRepository = TransactionRepository(transactionDB)
        transactionViewModel.setRepository(transactionRepository)

        // connectivity manager for checking network
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        networkCallback = object : NetworkCallback() {
            override fun onLost(network: Network) {
                sessionManager.removeAuthToken()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.disconnected_network),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(jwtServiceIntent)
        TransactionDatabase.closeDb()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}