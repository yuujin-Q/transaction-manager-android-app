package com.mog.bondoman.ui.transaction.modify

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mog.bondoman.R
import com.mog.bondoman.model.Transaction
import java.util.Date

class TransactionInputViewModel : ViewModel() {

    private val _transactionFormState = MutableLiveData<TransactionFormState>()
    val transactionFormState: LiveData<TransactionFormState> = _transactionFormState

    fun transactionDataChanged(transactionInputBinding: TransactionInputBinding) {
        val title = transactionInputBinding.titleEditText.text.toString()
        val category = transactionInputBinding.categoryEditText.text.toString()
        val nominal = transactionInputBinding.nominalEditText.text.toString()
        val location = transactionInputBinding.locationEditText.text.toString()
        if (title.isBlank()) {
            _transactionFormState.value = TransactionFormState(titleError = R.string.empty_field)
        } else if (category.isBlank()) {
            _transactionFormState.value = TransactionFormState(categoryError = R.string.empty_field)
        } else if (checkPositiveNominal(nominal)) {
            _transactionFormState.value =
                TransactionFormState(nominalError = R.string.invalid_positive_number)
        } else if (location.isBlank()) {
            _transactionFormState.value = TransactionFormState(locationError = R.string.empty_field)
        } else {
            _transactionFormState.value = TransactionFormState(isDataValid = true)
        }
    }

    private fun checkPositiveNominal(nominal: String): Boolean {
        if (nominal.isBlank()) return false
        val doubleNominal = nominal.toDouble()
        return doubleNominal.isNaN() or (doubleNominal < 0)
    }

    companion object {
        fun updateError(
            transactionFormState: TransactionFormState,
            transactionInputBinding: TransactionInputBinding,
            fragment: Fragment
        ) {
            transactionFormState.titleError?.let {
                transactionInputBinding.titleEditText.error = fragment.getString(it)
            }
            transactionFormState.categoryError?.let {
                transactionInputBinding.categoryEditText.error = fragment.getString(it)
            }
            transactionFormState.nominalError?.let {
                transactionInputBinding.nominalEditText.error = fragment.getString(it)
            }
            transactionFormState.locationError?.let {
                transactionInputBinding.locationEditText.error = fragment.getString(it)
            }
        }

        fun parseTransaction(
            transactionInputBinding: TransactionInputBinding,
            userId: Long
        ): Transaction {
            val title = transactionInputBinding.titleEditText.text.toString()
            val category = transactionInputBinding.categoryEditText.text.toString()
            val nominal = transactionInputBinding.nominalEditText.text.toString().toDouble()
            val location = transactionInputBinding.locationEditText.text.toString()
            return Transaction(
                ownerId = userId,
                title = title,
                category = category,
                nominal = nominal,
                location = location,
                date = Date()
            )
        }

        fun updateTransaction(
            transaction: Transaction,
            transactionInputBinding: TransactionInputBinding
        ) {
            val title = transactionInputBinding.titleEditText.text.toString()
            val category = transactionInputBinding.categoryEditText.text.toString()
            val nominal = transactionInputBinding.nominalEditText.text.toString().toDouble()
            val location = transactionInputBinding.locationEditText.text.toString()
            transaction.title = title
            transaction.category = category
            transaction.nominal = nominal
            transaction.location = location
        }
    }
}