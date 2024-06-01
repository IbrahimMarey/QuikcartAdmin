package com.example.quikcartadmin.ui.products.createproduct.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.products.SingleImageBody
import com.example.quikcartadmin.models.entities.products.SingleImageResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadImageViewModel@Inject constructor(
    val iRepo : IAdminRepository
) : ViewModel() {

    private val _uploadImageState = MutableStateFlow<UiState<SingleImageResponse>>(UiState.Loading())
    val uploadImageState: StateFlow<UiState<SingleImageResponse>> = _uploadImageState

    fun uploadImageToProduct(productId: Long, imageBody: SingleImageBody) {
        _uploadImageState.value = UiState.Loading()
        viewModelScope.launch {
            try {
                val uploadedImage = iRepo.uploadImageToProduct(productId, imageBody)
                _uploadImageState.value = UiState.Success(uploadedImage)
            } catch (e: Exception) {
                _uploadImageState.value = UiState.Failed(e)
            }
        }
    }

}