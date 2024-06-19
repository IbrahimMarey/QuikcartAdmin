package com.example.quikcartadmin.ui.coupons.alldiscounts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.DiscountCode
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllDiscountsViewModel @Inject constructor(
    private val repo: IAdminRepository
): ViewModel() {

    private var _getDiscountState: MutableStateFlow<UiState<List<DiscountCode>>> = MutableStateFlow(
        UiState.Loading()
    )
    var getDiscountState: StateFlow<UiState<List<DiscountCode>>> = _getDiscountState

    private var _deleteDiscountState: MutableStateFlow<UiState<String>> = MutableStateFlow(
        UiState.Loading()
    )
    var deleteDiscountState: StateFlow<UiState<String>> = _deleteDiscountState

    fun getDiscounts(ruleID : Long) {
        viewModelScope.launch {
            try {
                repo.getDiscounts(ruleID).collect {
                    _getDiscountState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _getDiscountState.value = UiState.Failed(e)
            }
        }
    }

    fun deleteDiscount(ruleId : Long,discountId: Long) {
        viewModelScope.launch {
            try {
                repo.deleteDiscount(ruleId,discountId).collect {
                    _deleteDiscountState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _deleteDiscountState.value = UiState.Failed(e)
            }
        }
    }
}