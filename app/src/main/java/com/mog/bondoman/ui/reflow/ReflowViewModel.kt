package com.mog.bondoman.ui.reflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReflowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is scan Fragment"
    }
    val text: LiveData<String> = _text
}