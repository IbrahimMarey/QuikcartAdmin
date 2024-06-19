package com.example.quikcartadmin.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.CouponsCountResponse
import com.example.quikcartadmin.models.entities.inventory.InventoryCountResponse
import com.example.quikcartadmin.models.entities.products.ProductsCountResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val adminRepo: IAdminRepository) : ViewModel() {

    private val _productCount = MutableStateFlow<UiState<ProductsCountResponse>>(UiState.Loading())
    val productCount: StateFlow<UiState<ProductsCountResponse>> = _productCount


    private val _couponsCount = MutableStateFlow<UiState<CouponsCountResponse>>(UiState.Loading())
    val couponsCount: StateFlow<UiState<CouponsCountResponse>> = _couponsCount


    private val _inventoryCount = MutableStateFlow<UiState<InventoryCountResponse>>(UiState.Loading())
    val inventoryCount: StateFlow<UiState<InventoryCountResponse>> = _inventoryCount

    init {
        fetchProductCount()
        fetchCouponsCount()
        fetchInventoryCount()
    }

    private fun fetchProductCount() {
        viewModelScope.launch {
            adminRepo.getCountOfProducts()
                .catch { e ->
                    _productCount.value = UiState.Failed(e)
                    Log.e("HomeViewModel", "Error fetching product count: ${e.message}")
                }
                .collect { count ->
                    _productCount.value = UiState.Success(count)
                }
        }
    }


    private fun fetchCouponsCount() {
        viewModelScope.launch {
            adminRepo.getCountOfCoupons()
                .catch { e ->
                    _couponsCount.value = UiState.Failed(e)
                    Log.e("HomeViewModel", "Error fetching product count: ${e.message}")
                }
                .collect { count ->
                    _couponsCount.value = UiState.Success(count)
                }
        }
    }

    private fun fetchInventoryCount() {
        viewModelScope.launch {
            adminRepo.getCountOfInventory()
                .catch { e ->
                    _inventoryCount.value = UiState.Failed(e)
                    Log.e("HomeViewModel", "Error fetching product count: ${e.message}")
                }
                .collect { count ->
                    _inventoryCount.value = UiState.Success(count)
                }
        }
    }
}
