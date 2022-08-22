package com.garibyan.armen.tbc_classwork_6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_6.network.ApiClient
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import com.garibyan.armen.tbc_classwork_6.network.models.LoginResponse
import com.garibyan.armen.tbc_classwork_6.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
    = AuthRepository(ApiClient.apiService)
) : ViewModel() {

    private val _loginFlow = MutableSharedFlow<Resource<LoginResponse>>()
    val loginFlow = _loginFlow.asSharedFlow()

    fun login(email: String, password: String) {
        val loginRequestBody = LoginRegisterRequest(email = email, password = password)
        viewModelScope.launch {
            repository.login(loginRequestBody).collect {
                _loginFlow.emit(it)
            }
        }
    }

}