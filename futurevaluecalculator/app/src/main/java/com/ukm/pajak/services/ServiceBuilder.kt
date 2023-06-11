package com.ukm.pajak.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceBuilder {
    private val client = OkHttpClient.Builder()

        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://gvd4re7sewa2oxxw.anvil.app/BAHMQGCS63FVTEZ2VOBZW3ZO/_/api/") // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}