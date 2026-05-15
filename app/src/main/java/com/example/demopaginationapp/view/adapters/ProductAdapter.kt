package com.example.demopaginationapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demopaginationapp.databinding.ItemProductCardBinding
import com.example.demopaginationapp.databinding.ItemProductHorizontalBinding
import com.example.demopaginationapp.model.dataclasses.Product

class ProductAdapter(
    private val isHorizontal: Boolean = false
) : ListAdapter<Product, RecyclerView.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {

        return if (isHorizontal) {

            val binding = ItemProductHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            HorizontalViewHolder(binding)

        } else {

            val binding = ItemProductCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            CardViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {

        val product = getItem(position)

        when (holder) {
            is CardViewHolder -> holder.bind(product)
            is HorizontalViewHolder -> holder.bind(product)
        }
    }

    class CardViewHolder(
        private val binding: ItemProductCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.tvTitle.text = product.title
            binding.tvPrice.text = "$${product.price}"
            binding.tvRating.text = "★ ${product.rating}"

            Glide.with(binding.ivProduct.context)
                .load(product.thumbnail)
                .into(binding.ivProduct)
        }
    }

    class HorizontalViewHolder(
        private val binding: ItemProductHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.tvProductTitle.text = product.title
            binding.tvProductBrand.text = product.brand
            binding.tvProductPrice.text = "Price: $${product.price}"
            binding.tvProductRating.text = "★ ${product.rating}"

            Glide.with(binding.ivProduct.context)
                .load(product.thumbnail)
                .into(binding.ivProduct)
        }
    }

    fun updateList(newList: List<Product>) {
        submitList(newList)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }
    }
}