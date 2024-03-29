package com.mog.bondoman.ui.transaksi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransaksiViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is Transaksi Fragment"
    }
    val text: LiveData<String> = _text
}