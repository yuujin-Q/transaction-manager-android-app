package com.mog.bondoman.ui.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mog.bondoman.databinding.TransactionItemBinding
import com.mog.bondoman.model.Transaction

class TransactionListAdapter() :
    ListAdapter<Transaction,
            TransactionListAdapter.TransactionViewHolder>(RowItemDiffCallback()) {
    class TransactionViewHolder(private val binding: TransactionItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.transaction = transaction
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = TransactionItemBinding.inflate(LayoutInflater.from(parent.context))
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
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