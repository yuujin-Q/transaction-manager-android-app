package com.mog.bondoman.ui.transaction.modify

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.ui.transaction.TransactionViewModel

abstract class ModifyTransactionFragment : Fragment() {
    protected val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    protected lateinit var transactionInputViewModel: TransactionInputViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionInputViewModel = ViewModelProvider(this)[TransactionInputViewModel::class.java]
    }

    protected fun setFormInputListeners(
        transactionInputBinding: TransactionInputBinding,
        confirmButton: Button
    ) {
        // set observer for input validation
        transactionInputViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState == null) {
                    return@Observer
                }
                confirmButton.isEnabled = transactionFormState.isDataValid
                TransactionInputViewModel.updateError(
                    transactionFormState,
                    transactionInputBinding,
                    this
                )
            })

        // check for data change
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                transactionInputViewModel.transactionDataChanged(transactionInputBinding)
            }
        }
        transactionInputBinding.addTextChangedListener(afterTextChangedListener)
    }
}