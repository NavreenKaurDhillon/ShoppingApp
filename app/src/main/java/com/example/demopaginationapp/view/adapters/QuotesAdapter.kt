package com.example.demopaginationapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demopaginationapp.databinding.ItemQuoteBinding
import com.example.demopaginationapp.model.dataclasses.ResponseListItem

class QuotesAdapter(private val onItemClick: (ResponseListItem) -> Unit) :
    ListAdapter<ResponseListItem, QuotesAdapter.QuoteViewHolder>(QuoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val binding = ItemQuoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class QuoteViewHolder(private val binding: ItemQuoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponseListItem) {
            binding.tvQuoteTitle.text = item.title
            Glide.with(binding.ivQuote.context)
                .load(item.image)
                .into(binding.ivQuote)
            
            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class QuoteDiffCallback : DiffUtil.ItemCallback<ResponseListItem>() {
        override fun areItemsTheSame(oldItem: ResponseListItem, newItem: ResponseListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ResponseListItem, newItem: ResponseListItem): Boolean {
            return oldItem == newItem
        }
    }
}
