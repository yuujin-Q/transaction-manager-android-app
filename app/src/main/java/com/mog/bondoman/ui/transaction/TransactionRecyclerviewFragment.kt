package com.mog.bondoman.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mog.bondoman.R
import com.mog.bondoman.databinding.FragmentTransactionRecyclerviewBinding

class TransactionRecyclerviewFragment : Fragment(), TransactionRecyclerViewOnClickListener {
    private val transactionViewModel: TransactionViewModel by activityViewModels<TransactionViewModel>()
    private var _binding: FragmentTransactionRecyclerviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionRecyclerviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionViewModel.clearOngoingUpdate()
        val recyclerView = binding.transactionRecyclerview
        val adapter = TransactionListAdapter(this)

        recyclerView.adapter = adapter
        transactionViewModel.transactions.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(R.id.action_transactionRecyclerviewFragment_to_addTransactionFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun editTransaction(position: Int) {
        val action =
            TransactionRecyclerviewFragmentDirections
                .actionTransactionRecyclerviewFragmentToEditTransactionFragment(position)
        findNavController().navigate(action)
    }

    override fun openMap(location: String) {
        TODO("Not yet implemented")
    }
}

interface TransactionRecyclerViewOnClickListener {
    fun editTransaction(position: Int)
    fun openMap(location: String)
}