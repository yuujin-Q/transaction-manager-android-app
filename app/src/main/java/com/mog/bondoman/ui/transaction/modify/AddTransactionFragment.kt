package com.mog.bondoman.ui.transaction.modify

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.databinding.FragmentAddTransactionBinding
import com.mog.bondoman.ui.transaction.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTransactionFragment : Fragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    private lateinit var transactionInputViewModel: TransactionInputViewModel
    private var _binding: FragmentAddTransactionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionInputViewModel = ViewModelProvider(this)[TransactionInputViewModel::class.java]

        // set bindings
        val transactionInputBinding = TransactionInputBinding(
            binding.transactionInputField.editTransactionTitle,
            binding.transactionInputField.editTransactionCategory,
            binding.transactionInputField.editTransactionNominal,
            binding.transactionInputField.editTransactionLocation,
        )
        val addTransactionButton = binding.addTransactionButton

        // set observer for input validation
        transactionInputViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState == null) {
                    return@Observer
                }
                addTransactionButton.isEnabled = transactionFormState.isDataValid
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

        addTransactionButton.setOnClickListener {
            val newTransaction =
                TransactionInputViewModel.parseTransaction(transactionInputBinding)
            CoroutineScope(Dispatchers.IO).launch {
                transactionViewModel.insert(newTransaction)
            }
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}