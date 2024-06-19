package com.example.quikcartadmin.ui.coupons.updaterule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRuleResponse
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UpdatePriceRuleViewModel @Inject constructor(
    val repo : IAdminRepository
) : ViewModel() {

    private var _updateRuleState: MutableStateFlow<UiState<PriceRuleResponse>> = MutableStateFlow(UiState.Loading())
    var updateRuleState: StateFlow<UiState<PriceRuleResponse>> = _updateRuleState


    fun updatePriceRule(ruleId : Long, body: PriceRuleResponse) {
        viewModelScope.launch {
            try {
                repo.updatePriceRule(ruleId,body).collect {
                    _updateRuleState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _updateRuleState.value = UiState.Failed(e)
            }
        }
    }
}