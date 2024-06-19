package com.example.quikcartadmin.ui.coupons.allrulesprice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.coupons.PriceRule
import com.example.quikcartadmin.models.repository.IAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllPriceRulesViewModel @Inject constructor(
    val repository: IAdminRepository
) : ViewModel() {

    private var _ruleState: MutableStateFlow<UiState<List<PriceRule>>> = MutableStateFlow(
        UiState.Loading()
    )
    var ruleState: StateFlow<UiState<List<PriceRule>>> = _ruleState

    private var _deleteRuleState: MutableStateFlow<UiState<String>> = MutableStateFlow(
        UiState.Loading()
    )
    var deleteRuleState: StateFlow<UiState<String>> = _deleteRuleState

    fun getPriceRules() {
        viewModelScope.launch {
            try {
                repository.getPriceRules().collect {
                    _ruleState.value = UiState.Success(it)
                }
            } catch (e: java.lang.Exception) {
                _ruleState.value = UiState.Failed(e)
            }
        }
    }


    fun deletePriceRule(ruleId : Long) {
        viewModelScope.launch {
            try {
                repository.deletePriceRule(ruleId).collect {
                    _deleteRuleState.value = UiState.Success(it!!)
                }
            } catch (e: java.lang.Exception) {
                _deleteRuleState.value = UiState.Failed(e)
            }
        }
    }
}