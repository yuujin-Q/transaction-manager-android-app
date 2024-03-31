package com.mog.bondoman.ui.transaction

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mog.bondoman.model.Transaction
import java.util.Date

class TransactionViewModel : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()

//    TODO REMOVE THIS METHOD
    fun setdatadummy() {
        val dummyList = ArrayList<Transaction>()
        dummyList.add(
            Transaction(1,
                "title", "category",
                100.0, "indonesia",
                Location("100,100"), Date()
            )
        )
        dummyList.add(
            Transaction(2,
                "title2", "category2",
                101.0, "indonesia",
                Location("100,100"), Date()
            )
        )
        dummyList.add(
            Transaction(2,
                "title2", "category2",
                101.0, "indonesiaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                Location("100,100"), Date()
            )
        )
        _transactions.value = dummyList
    }

    val texts: LiveData<List<Transaction>> = _transactions
}