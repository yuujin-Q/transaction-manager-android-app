package com.mog.bondoman.ui.transaction

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mog.bondoman.R
import com.mog.bondoman.TransactionReceiver
import com.mog.bondoman.databinding.FragmentTransactionRecyclerviewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionRecyclerviewFragment : Fragment(), TransactionRecyclerViewOnClickListener {
    @Inject
    lateinit var transactionReceiver: TransactionReceiver

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
        transactionReceiver.setDestinationNavController(findNavController())

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
        val gmmIntentUri = Uri.parse("geo:0,0?q=$location")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}

interface TransactionRecyclerViewOnClickListener {
    fun editTransaction(position: Int)
    fun openMap(location: String)
}