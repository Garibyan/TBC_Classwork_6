package com.garibyan.armen.tbc_classwork_6.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import com.garibyan.armen.tbc_classwork_6.network.models.RegisterResponse
import com.garibyan.armen.tbc_classwork_6.repository.AuthRepository
import com.garibyan.armen.tbc_classwork_6.repository.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStore: DataStore
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