package com.garibyan.armen.tbc_classwork_6.repository

import com.garibyan.armen.tbc_classwork_6.network.ApiService
import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest

class AuthRepository(
    private val api: ApiService
) : BaseRepository() {

    suspend fun login(requestBody: LoginRegisterRequest) = apiCall { api.login(requestBody) }

    suspend fun register(requestBody: LoginRegisterRequest) = apiCall { api.register(requestBody) }

}