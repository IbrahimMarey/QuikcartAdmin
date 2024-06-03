package com.example.quikcartadmin.ui.coupons.createrule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRuleBody
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreatePriceRuleViewModel @Inject constructor(
    val repo : IAdminRepository
) : ViewModel(){

    private var _createRuleState: MutableStateFlow<UiState<PriceRuleResponse>> = MutableStateFlow(
        UiState.Loading()
    )
    var createRuleState: StateFlow<UiState<PriceRuleResponse>> = _createRuleState

    fun createPriceRule(body : PriceRuleBody) {
        viewModelScope.launch {
            try {
                repo.createPriceRule(body).collect {
                    _createRuleState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _createRuleState.value = UiState.Failed(e)
            }
        }
    }
}