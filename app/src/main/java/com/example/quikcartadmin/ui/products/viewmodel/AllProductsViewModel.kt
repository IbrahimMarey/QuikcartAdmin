package com.example.quikcartadmin.ui.products.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllProductsViewModel @Inject constructor(
    private val adminRepo: IAdminRepository
) : ViewModel() {

    private val _allProduct = MutableStateFlow<UiState<ProductsResponse>>(UiState.Loading())
    val allProduct: StateFlow<UiState<ProductsResponse>> = _allProduct

    init {
        fetchAllProduct()
    }

    private fun fetchAllProduct() {
        viewModelScope.launch(Dispatchers.IO) {
            adminRepo.getAllProducts()
                .catch { e ->
                    _allProduct.value = UiState.Failed(e)
                    Log.e("HomeViewModel", "Error fetching product count: ${e.message}")
                }
                .collect {
                    _allProduct.value = UiState.Success(it)
                }
        }
    }
}