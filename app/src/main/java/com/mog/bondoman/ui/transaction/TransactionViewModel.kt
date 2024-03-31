package com.mog.bondoman.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mog.bondoman.model.Transaction
import com.mog.bondoman.repository.TransactionRepository
import java.util.Date
import java.util.UUID

class TransactionViewModel() : ViewModel() {

    private lateinit var transactionRepository: TransactionRepository

    private val _transactions = MutableLiveData<MutableList<Transaction>>()
    val transactions: LiveData<MutableList<Transaction>> = _transactions

    fun setRepository(transactionRepository: TransactionRepository) {
        this.transactionRepository = transactionRepository
    }

    //    TODO REMOVE THIS METHOD
    fun setdatadummy() {
        val dummyList = ArrayList<Transaction>()
        dummyList.add(
            Transaction(
                UUID.randomUUID().toString(), 1,
                "title", "category",
                100.0, "indonesia",
                Date()
            )
        )
        dummyList.add(
            Transaction(
                UUID.randomUUID().toString(), 1,
                "title2", "category2",
                101.0, "indonesia",
                Date()
            )
        )
        dummyList.add(
            Transaction(
                UUID.randomUUID().toString(), 1,
                "title2", "category2",
                101.0, "indonesiaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                Date()
            )
        )
        _transactions.value = dummyList
    }

    suspend fun insert(transaction: Transaction) {
        transactionRepository.insert(transaction)
        _transactions.value!!.add(0, transaction)
    }

}