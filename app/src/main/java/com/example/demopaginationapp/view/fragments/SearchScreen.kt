package com.example.demopaginationapp.view.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demopaginationapp.databinding.FragmentSearchBinding
import com.example.demopaginationapp.view.adapters.ProductAdapter
import com.example.demopaginationapp.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductViewModel by activityViewModels()

    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()
        setupClickListeners()

        // Initial data
        updateProducts("")
    }

    private fun setupRecyclerView() {

        productAdapter = ProductAdapter(isHorizontal = false)

        binding.recyclerView.apply {

            layoutManager = GridLayoutManager(
                requireContext(),
                2 // number of columns
            )

            adapter = productAdapter
        }
    }
    private fun setupSearch() {

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateProducts(s.toString())
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) = Unit
        })
    }

    private fun setupClickListeners() {

        // Back button
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Clear search
        binding.ivClear.setOnClickListener {
            binding.etSearch.text?.clear()
        }
    }

    private fun updateProducts(searchText: String) {

        val products = viewModel.products.value?.data?.products ?: emptyList()

        val filteredProducts = if (searchText.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.title.contains(searchText, ignoreCase = true) ||
                        product.brand?.contains(searchText, ignoreCase = true) == true
            }
        }

        productAdapter.updateList(filteredProducts)

        if (filteredProducts.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        binding.ivClear.visibility =
            if (searchText.isNotEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}