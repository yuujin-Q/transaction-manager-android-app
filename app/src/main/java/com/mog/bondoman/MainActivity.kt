package com.mog.bondoman

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.databinding.ActivityMainBinding
import com.mog.bondoman.model.database.TransactionDatabase
import com.mog.bondoman.repository.TransactionRepository
import com.mog.bondoman.ui.transaction.TransactionViewModel

class MainActivity : AppCompatActivity() {

    //    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]
        val transactionDB = TransactionDatabase.getInstance(baseContext)
        val transactionRepository = TransactionRepository(transactionDB)
        transactionViewModel.setRepository(transactionRepository)
//        TODO("REMOVE SETDATADUMMY")
        transactionViewModel.setdatadummy()
    }
}