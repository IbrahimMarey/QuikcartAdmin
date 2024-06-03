package com.example.quikcartadmin.ui.coupons.creatediscount.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.entities.coupons.DiscountCodeBody
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateDiscountViewModel @Inject constructor(
    val repo : IAdminRepository
) : ViewModel() {

    private var _createDiscountState: MutableStateFlow<UiState<DiscountCode>> = MutableStateFlow(
        UiState.Loading()
    )
    var createDiscountState: StateFlow<UiState<DiscountCode>> = _createDiscountState

    fun createDiscount(ruleID : Long, body: DiscountCodeBody) {
        viewModelScope.launch {
            try {
                repo.createDiscountCode(ruleID,body).collect {
                    _createDiscountState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _createDiscountState.value = UiState.Failed(e)
            }
        }
    }
}