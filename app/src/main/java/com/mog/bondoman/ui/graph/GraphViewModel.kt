package com.mog.bondoman.ui.graph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraphViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Graf Fragment"
    }
    val text: LiveData<String> = _text
}