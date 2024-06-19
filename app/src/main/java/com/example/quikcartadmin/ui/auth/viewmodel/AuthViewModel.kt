package com.example.quikcartadmin.ui.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quikcartadmin.helpers.UiState
import com.example.quikcartadmin.models.entities.user.User
import com.example.quikcartadmin.models.firbase.IAuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: IAuthenticationRepository
): ViewModel() {

    private val _authState = MutableStateFlow<UiState<Boolean>>(UiState.Loading())
    val authState : StateFlow<UiState<Boolean>> = _authState
    fun login(user: User){
        viewModelScope.launch {
            _authState.value = UiState.Loading()
            val success = authRepository.signInWithEmailAndPassword(user)
            _authState.value = UiState.Success(success)
        }
    }
}