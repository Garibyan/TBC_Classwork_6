package com.garibyan.armen.tbc_classwork_6.viewmodel

import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.garibyan.armen.tbc_classwork_6.network.Resource
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import com.garibyan.armen.tbc_classwork_6.network.models.LoginResponse
import com.garibyan.armen.tbc_classwork_6.repository.AuthRepository
import com.garibyan.armen.tbc_classwork_6.repository.DataStore
import com.garibyan.armen.tbc_classwork_6.utils.PreferenceKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStore: DataStore
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

    fun saveToken(token: String){
        viewModelScope.launch {
            dataStore.save(PreferenceKeys.KEY_AUTH, token)
        }
    }

    fun getToken(key: Preferences.Key<String>) = dataStore.getPreferences(key)

}