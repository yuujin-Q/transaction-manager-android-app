package com.mog.bondoman.ui.transaction.modify

/**
 * Data validation state of the transaction form
 */
data class TransactionFormState(
    val titleError: Int? = null,
    val categoryError: Int? = null,
    val nominalError: Int? = null,
    val locationError: Int? = null,
    val isDataValid: Boolean = false
)