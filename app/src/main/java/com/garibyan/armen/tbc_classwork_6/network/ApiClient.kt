package com.garibyan.armen.tbc_classwork_6.network

import com.garibyan.armen.tbc_classwork_6.network.models.LoginRegisterRequest
import com.garibyan.armen.tbc_classwork_6.network.models.LoginResponse
import com.garibyan.armen.tbc_classwork_6.network.models.RegisterResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object ApiClient {
    private const val BASE_URL = "https://reqres.in/api/"
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService {

    @POST("login")
    suspend fun login(@Body body: LoginRegisterRequest): LoginResponse

    @POST("register")
    suspend fun register(@Body body: LoginRegisterRequest): RegisterResponse
}