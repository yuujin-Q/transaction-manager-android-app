package com.mog.bondoman.repository

import com.mog.bondoman.model.Transaction
import com.mog.bondoman.model.database.TransactionDatabase

interface TransactionRepository {
    fun getTransactions(sortBy: String): MutableList<Transaction>

    fun updateTransaction(transaction: Transaction)

    fun insertTransaction(transaction: Transaction)

    fun deleteTransaction(transaction: Transaction)
}

class TransactionRepositoryImpl(
    private val transactionDB: TransactionDatabase) : TransactionRepository {

    override fun getTransactions(sortBy: String): MutableList<Transaction> {
        return transactionDB.transactionDao().getAll(sortBy)
    }

    override fun updateTransaction(transaction: Transaction) {
        transactionDB.transactionDao().update(transaction)
    }

    override fun insertTransaction(transaction: Transaction) {
        transactionDB.transactionDao().insert(transaction)
    }

    override fun deleteTransaction(transaction: Transaction) {
        transactionDB.transactionDao().delete(transaction)
    }
}