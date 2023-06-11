package com.ukm.pajak.services

import com.ukm.pajak.models.PajakAppModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface TvmApiInterface {


    @GET
    fun getParameters(@Url url: String) : Call<ArrayList<PajakAppModel>>
}