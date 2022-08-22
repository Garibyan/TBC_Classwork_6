package com.garibyan.armen.tbc_classwork_6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_6.network.ApiClient
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import com.garibyan.armen.tbc_classwork_6.network.models.RegisterResponse
import com.garibyan.armen.tbc_classwork_6.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val repository: AuthRepository = AuthRepository(ApiClient.apiService)
) : ViewModel() {

    private val _registerFlow = MutableSharedFlow<Resource<RegisterResponse>>()
    val registerFlow = _registerFlow.asSharedFlow()

    fun register(email: String, password: String) {
        val registerRequest = LoginRegisterRequest(email = email, password = password)
        viewModelScope.launch {
            repository.register(registerRequest).collectLatest {
                _registerFlow.emit(it)
            }
        }
    }
}