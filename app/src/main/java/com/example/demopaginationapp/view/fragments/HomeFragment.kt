package com.example.demopaginationapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demopaginationapp.R
import com.example.demopaginationapp.databinding.FragmentHomeBinding
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.view.adapters.BrandAdapter
import com.example.demopaginationapp.view.adapters.Category
import com.example.demopaginationapp.view.adapters.CategoryAdapter
import com.example.demopaginationapp.view.adapters.DealsPagerAdapter
import com.example.demopaginationapp.view.adapters.ProductAdapter
import com.example.demopaginationapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.apply {
            btnSearch.setOnClickListener {
                findNavController().navigate(R.id.search_screen)
            }
            btnRepos.setOnClickListener {
                findNavController().navigate(R.id.quotes_list_screen)
            }
            root.setOnClickListener {
                findNavController().navigate(R.id.product_screen)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    val products = resource.data?.products ?: emptyList()
                    setupRecyclerViews(products)
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    // Handle error
                }
                else -> {}
            }
        }
    }

    private fun setupRecyclerViews(products: List<com.example.demopaginationapp.model.dataclasses.Product>) {
        // Top Categories
        val categoryAdapter = CategoryAdapter()
        binding.rvTopItems.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTopItems.adapter = categoryAdapter
        val topCategories = products.take(5).map { Category(it.title, it.images[0]) } // Using dummy icon
        categoryAdapter.submitList(topCategories)

        // Today's Deals ViewPager
        val dealImages = products.take(5).map { it.images[0] }
        val dealsAdapter = DealsPagerAdapter(dealImages)
        binding.vpDeals.adapter = dealsAdapter
        com.google.android.material.tabs.TabLayoutMediator(binding.tabIndicator, binding.vpDeals) { _, _ -> }.attach()

        // Most Demanded
        val demandedAdapter = ProductAdapter(isHorizontal = false)
        binding.rvDemanded.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDemanded.adapter = demandedAdapter
        demandedAdapter.submitList(products)

        // Top Brands (using same products for now as brands aren't separated in ViewModel)
        val brandAdapter = BrandAdapter { brand ->
            // Handle brand click
        }
        binding.rvBrands.layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), 2, androidx.recyclerview.widget.GridLayoutManager.HORIZONTAL, false)
        binding.rvBrands.adapter = brandAdapter
        brandAdapter.submitList(products)

        // Best Selling
        val bestSellingAdapter = ProductAdapter(isHorizontal = true)
        binding.rvBestSelling.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestSelling.adapter = bestSellingAdapter
        bestSellingAdapter.submitList(products.filter { it.brand != null })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
