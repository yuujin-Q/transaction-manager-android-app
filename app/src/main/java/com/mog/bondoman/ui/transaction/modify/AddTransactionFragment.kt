package com.mog.bondoman.ui.transaction.modify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mog.bondoman.databinding.FragmentAddTransactionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTransactionFragment : ModifyTransactionFragment() {
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

        // set bindings
        val transactionInputBinding = TransactionInputBinding(
            binding.transactionInputField.editTransactionTitle,
            binding.transactionInputField.editTransactionCategory,
            binding.transactionInputField.editTransactionNominal,
            binding.transactionInputField.editTransactionLocation,
        )
        val addTransactionButton = binding.addTransactionButton
        // set form listener to validate input
        setFormInputListeners(
            transactionInputBinding,
            addTransactionButton
        )

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