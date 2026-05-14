package com.example.demopaginationapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demopaginationapp.databinding.FragmentFavBinding
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.view.adapters.FavAdapter
import com.example.demopaginationapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var adapter: FavAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = FavAdapter(
            onItemClick = { product ->
                // Handle item click, e.g., navigate to detail
                val bundle = Bundle().apply {
                    putString("data", product.id.toString())
                }
                findNavController().navigate(com.example.demopaginationapp.R.id.detail_screen, bundle)
            },
            onFavClick = { product ->
                // Handle fav click (toggle favorite)
                product.isFav = !product.isFav
                adapter.notifyDataSetChanged()
                updateEmptyState()
            }
        )
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    // Show loading if needed
                }
                Status.SUCCESS -> {
                    val favorites = resource.data?.products?.filter { it.isFav } ?: emptyList()
                    adapter.submitList(favorites)
                    updateEmptyState(favorites.isEmpty())
                }
                Status.ERROR -> {
                    // Handle error
                }
                else -> {}
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean = adapter.currentList.isEmpty()) {
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.rvFavorites.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
