package com.mog.bondoman.repository

import com.mog.bondoman.model.Transaction
import com.mog.bondoman.model.database.TransactionDatabase

class TransactionRepository(
    private val transactionDB: TransactionDatabase
) {

    suspend fun getAll(userId: Long, sortBy: String = "date"): MutableList<Transaction> {
        return transactionDB.transactionDao().getAll(userId, sortBy)
    }

    suspend fun update(transaction: Transaction) {
//        TODO UPDATE DATA
//        TODO UPDATE LOCATION NAME
        transactionDB.transactionDao().update(transaction)
    }

    suspend fun insert(transaction: Transaction, userId: Long? = null) {
        if (userId != null) transaction.ownerId = userId
        transactionDB.transactionDao().insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDB.transactionDao().delete(transaction)
    }
}