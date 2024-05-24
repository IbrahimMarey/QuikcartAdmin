package com.example.quikcartadmin.ui.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.quikcartadmin.databinding.FragmentHomeBinding
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.ui.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var homeBinding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchCountOfProduct()
        fetchCountOfCoupons()
        fetchCountOfInventory()
    }


    private fun fetchCountOfProduct(){

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.productCount.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        homeBinding.progressBar.visibility = View.VISIBLE
                        homeBinding.cardCountOfProduct.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfProduct.visibility = View.VISIBLE
                        homeBinding.countValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfProduct.visibility = View.VISIBLE
                        Log.e ("HomeFragment","Error: ${uiState.msg.message}")
                    }
                }
            }
        }
    }

    private fun fetchCountOfCoupons(){

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.couponsCount.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        homeBinding.progressBar.visibility = View.VISIBLE
                        homeBinding.cardCountOfCoupons.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfCoupons.visibility = View.VISIBLE
                        homeBinding.countCouponsValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfCoupons.visibility = View.VISIBLE
                        Log.e ("HomeFragment","Error: ${uiState.msg.message}")
                    }
                }
            }
        }
    }

    private fun fetchCountOfInventory() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.inventoryCount.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> {
                        homeBinding.progressBar.visibility = View.VISIBLE
                        homeBinding.cardCountOfInventory.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfInventory.visibility = View.VISIBLE
                        homeBinding.countInventoryValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        homeBinding.progressBar.visibility = View.GONE
                        homeBinding.cardCountOfInventory.visibility = View.VISIBLE
                        Log.e ("HomeFragment","Error: ${uiState.msg.message}")
                    }
                }
            }
        }
    }

}
