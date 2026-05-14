package com.example.demopaginationapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demopaginationapp.databinding.ItemDealsPagerBinding

class DealsPagerAdapter(private val images: List<String>) : RecyclerView.Adapter<DealsPagerAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemDealsPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class PagerViewHolder(private val binding: ItemDealsPagerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUrl: String) {
            Glide.with(binding.ivDeal.context)
                .load(imageUrl)
                .centerCrop()
                .into(binding.ivDeal)
        }
    }
}