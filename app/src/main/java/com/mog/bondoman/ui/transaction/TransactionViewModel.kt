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

    private var ongoingUpdate: Int? = null

    fun setRepository(transactionRepository: TransactionRepository) {
        this.transactionRepository = transactionRepository
    }

    suspend fun fetchData(ownerId: Long, sortBy: String = "date") {
        transactionRepository.getAll(ownerId, sortBy)
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

    suspend fun update() {
        transactionRepository.update(_transactions.value!![ongoingUpdate!!])
    }

    suspend fun delete() {
        transactionRepository.delete(_transactions.value!![ongoingUpdate!!])
        _transactions.value!!.removeAt(ongoingUpdate!!)
    }

    fun clearOngoingUpdate() {
        ongoingUpdate = null
    }

    fun setOngoingUpdate(itemPosition: Int) {
        ongoingUpdate = itemPosition
    }
}