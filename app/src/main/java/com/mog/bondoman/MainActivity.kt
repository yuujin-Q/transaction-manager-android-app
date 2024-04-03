package com.mog.bondoman

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var jwtServiceIntent: Intent

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Main", "Process ${android.os.Process.myPid()}")

        binding = ActivityMainBinding.inflate(layoutInflater)

        jwtServiceIntent = Intent(this.applicationContext, JwtService::class.java)
        setContentView(binding.root)

        startService(jwtServiceIntent)

        // initialize transaction repository
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        val transactionDB = TransactionDatabase.getInstance(baseContext)
        val transactionRepository = TransactionRepository(transactionDB)
        transactionViewModel.setRepository(transactionRepository)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(jwtServiceIntent)
        TransactionDatabase.closeDb()
    }
}