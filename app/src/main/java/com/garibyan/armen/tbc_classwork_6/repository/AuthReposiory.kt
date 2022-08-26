package com.garibyan.armen.tbc_classwork_6.repository

import com.garibyan.armen.tbc_classwork_6.network.ApiService
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) : BaseRepository() {

    suspend fun login(requestBody: LoginRegisterRequest) = apiCall { apiService.login(requestBody) }

    suspend fun register(requestBody: LoginRegisterRequest) = apiCall { apiService.register(requestBody) }

}