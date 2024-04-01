package com.mog.bondoman.ui.transaction.modify

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.mog.bondoman.databinding.FragmentEditTransactionBinding
import com.mog.bondoman.ui.transaction.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditTransactionFragment : Fragment() {
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    private lateinit var transactionInputViewModel: TransactionInputViewModel
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
        val updateTransactionButton = binding.updateTransactionButton
        val deleteTransactionButton = binding.deleteTransactionButton

        TransactionInputViewModel.applyDataToView(transactionInputBinding, ongoingUpdateTransaction)
        // set observer for input validation
        transactionInputViewModel.transactionFormState.observe(viewLifecycleOwner,
            Observer { transactionFormState ->
                if (transactionFormState == null) {
                    return@Observer
                }
                updateTransactionButton.isEnabled = transactionFormState.isDataValid
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}