package com.example.quikcartadmin.ui.products.createproduct.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.ProductBody
import com.example.quikcartadmin.models.entities.products.SingleProductsResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val iRepo: IAdminRepository
) : ViewModel() {

    private val _createProductState = MutableStateFlow<UiState<SingleProductsResponse>>(UiState.Loading())
    val createProductState: StateFlow<UiState<SingleProductsResponse>> = _createProductState

    fun createProduct(body: ProductBody) {
        _createProductState.value = UiState.Loading()
        viewModelScope.launch {
            try {
                val newProduct = iRepo.createProduct(body)
                _createProductState.value = UiState.Success(newProduct)
            } catch (e: Exception) {
                _createProductState.value = UiState.Failed(e)
            }
        }
    }

}