package com.example.demopaginationapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demopaginationapp.R
import com.example.demopaginationapp.databinding.ItemProductHorizontalBinding
import com.example.demopaginationapp.model.dataclasses.Product

class FavAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onFavClick: (Product) -> Unit
) : ListAdapter<Product, FavAdapter.FavViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemProductHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavViewHolder(private val binding: ItemProductHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Product) {
            binding.tvProductTitle.text = item.title
            binding.tvProductBrand.text = item.brand ?: "Unknown"
            binding.tvProductPrice.text = "Price: $${item.price}"
            binding.tvProductRating.text = String.format("%.1f", item.rating)
            
            Glide.with(binding.ivProduct.context)
                .load(item.images.firstOrNull())
                .into(binding.ivProduct)

            binding.ivFav.setImageResource(
                if (item.isFav) android.R.drawable.btn_star_big_on 
                else android.R.drawable.btn_star_big_off
            )

            binding.root.setOnClickListener {
                onItemClick(item)
            }
            
            binding.ivFav.setOnClickListener {
                onFavClick(item)
            }
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
}
