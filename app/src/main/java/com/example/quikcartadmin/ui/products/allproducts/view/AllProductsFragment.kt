package com.example.quikcartadmin.ui.products.allproducts.view

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.quikcartadmin.R
import com.example.quikcartadmin.databinding.FragmentAllProductsBinding
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.ProductsItem
import com.example.quikcartadmin.ui.products.allproducts.viewmodel.AllProductsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class AllProductsFragment : Fragment() {
    private lateinit var listOfProduct: List<ProductsItem>
    lateinit var proudctsBinding: FragmentAllProductsBinding
    private val allProductViewModel: AllProductsViewModel by viewModels()
    private lateinit var allProductAdapter: AllProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        proudctsBinding = FragmentAllProductsBinding.inflate(inflater, container, false)
        return proudctsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        fetchAllProducts()
        proudctsBinding.addProduct.setOnClickListener {
            findNavController().navigate(R.id.action_all_products_to_createProductFragment)
        }


        proudctsBinding.searchEd.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val searchText = s.toString().toLowerCase(Locale.getDefault())
                val filteredList = listOfProduct.filter {
                    it.title?.toLowerCase(Locale.getDefault())!!.contains(searchText) ||
                            it.productType?.toLowerCase(Locale.getDefault())!!.contains(searchText) ||
                            it.vendor?.toLowerCase(Locale.getDefault())!!.contains(searchText)
                }

                if (filteredList.isEmpty()) {
                    proudctsBinding.noList.visibility = View.VISIBLE
                } else {
                    proudctsBinding.noList.visibility = View.GONE
                }

                allProductAdapter.submitList(filteredList)
            }
        })
    }

    private fun setUpRecyclerView(){

        allProductAdapter = AllProductAdapter(
            context = requireContext(),
            onClick = { productItem ->
                // send argument by product details
                val action = AllProductsFragmentDirections.actionAllProductsToProductDetailsFragment()
                action.setProductItem(productItem)
                findNavController().navigate(action)
            },
            onDeleteClick = { productItem ->
                showDeleteConfirmationDialog(productItem)
            }
        )
        proudctsBinding.productRecyclerView.apply {
            layoutManager = GridLayoutManager(context,2)
            adapter = allProductAdapter
        }
    }
    private fun fetchAllProducts(){
        viewLifecycleOwner.lifecycleScope.launch {
            allProductViewModel.allProduct.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        proudctsBinding.productRecyclerView.visibility = View.GONE
                        proudctsBinding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Success -> {
                        proudctsBinding.productRecyclerView.visibility = View.VISIBLE
                        proudctsBinding.progressBar.visibility = View.GONE
                        allProductAdapter.submitList(uiState.data.products)
                        listOfProduct = uiState.data.products
                    }
                    is UiState.Failed -> {
                        proudctsBinding.productRecyclerView.visibility = View.VISIBLE
                        proudctsBinding.progressBar.visibility = View.GONE
                        Log.e ("HomeFragment","Error: ${uiState.msg.message}")
                    }
                }
            }
        }
    }

    private fun showDeleteConfirmationDialog(productItem: ProductsItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Delete") { _, _ ->
                allProductViewModel.deleteProduct(productItem.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}