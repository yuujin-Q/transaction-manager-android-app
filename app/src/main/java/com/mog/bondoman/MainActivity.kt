package com.mog.bondoman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mog.bondoman.data.connection.SessionManager
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.databinding.ActivityMainBinding
import com.mog.bondoman.service.JwtService
import com.mog.bondoman.model.database.TransactionDatabase
import com.mog.bondoman.repository.TransactionRepository
import com.mog.bondoman.ui.transaction.TransactionViewModel

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var jwtServiceIntent: Intent
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        sessionManager = SessionManager.getInstance(
            applicationContext.getSharedPreferences(
                SessionManager.PREF_KEY,
                MODE_PRIVATE
            )
        )
//        SessionManager.initialize(
//            applicationContext.getSharedPreferences(
//                SessionManager.PREF_KEY,
//                MODE_PRIVATE
//            )
//        )

        jwtServiceIntent = Intent(this, JwtService::class.java)
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