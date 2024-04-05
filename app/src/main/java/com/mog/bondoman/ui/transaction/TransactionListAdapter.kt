package com.mog.bondoman.ui.transaction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mog.bondoman.data.model.Transaction
import com.mog.bondoman.databinding.TransactionItemBinding

class TransactionListAdapter(private val transactionRecyclerViewOnClickListener: TransactionRecyclerViewOnClickListener) :
    ListAdapter<Transaction,
            TransactionListAdapter.TransactionViewHolder>(RowItemDiffCallback()) {
    class TransactionViewHolder(private val binding: TransactionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            transaction: Transaction,
            transactionRecyclerViewOnClickListener: TransactionRecyclerViewOnClickListener
        ) {
            binding.transaction = transaction
            // bind click to edit transaction
            binding.transactionItemContainer.setOnClickListener {
                transactionRecyclerViewOnClickListener.editTransaction(layoutPosition)
            }

            // if item has no location listed, disable button
            if (transaction.location.isBlank()) {
                binding.locationSymbol.visibility = View.GONE
                binding.transactionItemLocation.visibility = View.GONE
            } else {
                // bind click location symbol & location name to maps intent
                val locationClickListener = View.OnClickListener {
                    transactionRecyclerViewOnClickListener.openMap(binding.transaction!!.location)
                }
                binding.locationSymbol.setOnClickListener(locationClickListener)
                binding.transactionItemLocation.setOnClickListener(locationClickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context))
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position), transactionRecyclerViewOnClickListener)
    }
}

private class RowItemDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}