package com.example.quikcartadmin.ui.products.createproduct.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    val iRepo : IAdminRepository)
    : ViewModel() {

    private val _updateProductState = MutableStateFlow<UiState<SingleProductsResponse>>(UiState.Loading())
    val updateProductState: StateFlow<UiState<SingleProductsResponse>> = _updateProductState

    fun updateProduct(productId: Long, product: SingleProductsResponse) {
        _updateProductState.value = UiState.Loading()
        viewModelScope.launch {
            try {
                val updatedProduct = iRepo.updateProduct(productId, product)
                _updateProductState.value = UiState.Success(updatedProduct)
            } catch (e: Exception) {
                _updateProductState.value = UiState.Failed(e)
            }
        }
    }

}



