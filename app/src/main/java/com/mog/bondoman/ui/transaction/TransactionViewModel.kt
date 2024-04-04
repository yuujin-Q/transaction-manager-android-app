package com.mog.bondoman.ui.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mog.bondoman.data.model.Transaction
import com.mog.bondoman.data.TransactionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel : ViewModel() {

    private lateinit var transactionRepository: TransactionRepository
    private var userId: Long? = null

    private val _transactions = MutableLiveData<MutableList<Transaction>>()
    val transactions: LiveData<MutableList<Transaction>> = _transactions

    private var ongoingUpdate: Int? = null

    fun setRepository(transactionRepository: TransactionRepository) {
        this.transactionRepository = transactionRepository
    }

    fun setUserId(userId: Long) {
        this.userId = userId
    }

    suspend fun fetchData(sortBy: String = "date") {
        CoroutineScope(Dispatchers.IO).launch {
            _transactions.postValue(
                transactionRepository.getAll(userId!!, sortBy)
            )
        }
    }

    suspend fun insert(transaction: Transaction) {
        Log.d("TransactionViewModel", "Current user id = $userId")
        transaction.ownerId = userId!!
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