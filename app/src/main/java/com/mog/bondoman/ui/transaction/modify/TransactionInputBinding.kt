package com.mog.bondoman.ui.transaction.modify

import android.text.TextWatcher
import android.widget.EditText

data class TransactionInputBinding(
    val titleEditText: EditText,
    val categoryEditText: EditText,
    val nominalEditText: EditText,
    val locationEditText: EditText
) {
    fun addTextChangedListener(textWatcher: TextWatcher) {
        titleEditText.addTextChangedListener(textWatcher)
        categoryEditText.addTextChangedListener(textWatcher)
        nominalEditText.addTextChangedListener(textWatcher)
        locationEditText.addTextChangedListener(textWatcher)
    }
}