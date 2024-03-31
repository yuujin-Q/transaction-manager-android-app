package com.mog.bondoman.ui.graf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GrafViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Graf Fragment"
    }
    val text: LiveData<String> = _text
}