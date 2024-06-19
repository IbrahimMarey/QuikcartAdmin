package com.example.quikcartadmin.ui.coupons.updatediscount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdateDiscountViewModel @Inject constructor(
    val repo : IAdminRepository
) :ViewModel(){
    private var _updateDiscountState: MutableStateFlow<UiState<DiscountCodeResponse>> = MutableStateFlow(
        UiState.Loading()
    )
    var updateDiscountState: StateFlow<UiState<DiscountCodeResponse>> = _updateDiscountState


    fun updateDiscount(ruleId : Long,discountId: Long, body: DiscountCodeResponse) {
        viewModelScope.launch {
            try {
                repo.updateDiscount(ruleId,discountId,body).collect {
                    _updateDiscountState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateDiscountState.value = UiState.Failed(e)
            }
        }
    }
}