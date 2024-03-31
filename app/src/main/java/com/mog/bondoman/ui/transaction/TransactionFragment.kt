package com.mog.bondoman.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mog.bondoman.databinding.FragmentTransactionBinding

class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
//        TODO("REMOVE SETDATADUMMY")
        transactionViewModel.setdatadummy()
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = binding.transactionRecyclerview
        val adapter = TransactionListAdapter()
        recyclerView.adapter = adapter
        transactionViewModel.texts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addTransactionButton.setOnClickListener {
            TODO("implement add transaction page and set button")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}