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

    private var _homeBinding: FragmentHomeBinding? = null
    private val binding get() = _homeBinding!!

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
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
                        binding.progressBar.visibility = View.VISIBLE
                        binding.cardCountOfProduct.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfProduct.visibility = View.VISIBLE
                        binding.countValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfProduct.visibility = View.VISIBLE
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
                        binding.progressBar.visibility = View.VISIBLE
                        binding.cardCountOfCoupons.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfCoupons.visibility = View.VISIBLE
                        binding.countCouponsValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfCoupons.visibility = View.VISIBLE
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
                        binding.progressBar.visibility = View.VISIBLE
                        binding.cardCountOfInventory.visibility = View.GONE
                    }
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfInventory.visibility = View.VISIBLE
                        binding.countInventoryValue.text = uiState.data.count.toString()
                    }
                    is UiState.Failed -> {
                        binding.progressBar.visibility = View.GONE
                        binding.cardCountOfInventory.visibility = View.VISIBLE
                        Log.e ("HomeFragment","Error: ${uiState.msg.message}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _homeBinding = null
    }
}
