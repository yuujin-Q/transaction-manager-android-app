package com.mog.bondoman.ui.transaction.modify

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mog.bondoman.databinding.FragmentEditTransactionBinding
import com.mog.bondoman.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTransactionFragment : ModifyTransactionFragment() {
    private var _binding: FragmentEditTransactionBinding? = null

    private val binding get() = _binding!!

    private val args: EditTransactionFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTransactionBinding.inflate(inflater, container, false)
        // TURNS OUT CATEGORY IS NOT TO BE CHANGED
        binding.transactionInputField.editTransactionCategory.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionInputViewModel = ViewModelProvider(this)[TransactionInputViewModel::class.java]

        val itemPosition = args.itemPosition
        Log.d("EditTransactionFragment", "current item pos: $itemPosition")
        val ongoingUpdateTransaction = transactionViewModel.transactions.value!![itemPosition]
        transactionViewModel.setOngoingUpdate(itemPosition)
        // set bindings
        val transactionInputBinding = TransactionInputBinding(
            binding.transactionInputField.editTransactionTitle,
            binding.transactionInputField.editTransactionCategory,
            binding.transactionInputField.editTransactionNominal,
            binding.transactionInputField.editTransactionLocation,
        )
        TransactionInputViewModel.applyDataToView(transactionInputBinding, ongoingUpdateTransaction)
        val updateTransactionButton = binding.updateTransactionButton
        // set form listener to validate input
        setFormInputListeners(
            transactionInputBinding,
            updateTransactionButton
        )
        setButtonClickListeners(transactionInputBinding, ongoingUpdateTransaction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setButtonClickListeners(
        transactionInputBinding: TransactionInputBinding,
        ongoingUpdateTransaction: Transaction
    ) {
        val updateTransactionButton = binding.updateTransactionButton
        val deleteTransactionButton = binding.deleteTransactionButton
        updateTransactionButton.setOnClickListener {
//            TODO optimize: don't update DB if no change
            TransactionInputViewModel.updateTransaction(
                ongoingUpdateTransaction,
                transactionInputBinding
            )
            CoroutineScope(Dispatchers.IO).launch {
                transactionViewModel.update()
            }
            parentFragmentManager.popBackStack()
        }
        deleteTransactionButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                transactionViewModel.delete()
            }
            parentFragmentManager.popBackStack()
        }
    }
}