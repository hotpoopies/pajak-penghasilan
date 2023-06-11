package com.ukm.pajak.services

import com.ukm.pajak.models.PajakAppModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvmApiService {


    fun getTvms( onResult: (ArrayList<PajakAppModel>?) -> Unit){
        val retrofit = ServiceBuilder.buildService(TvmApiInterface::class.java)
        retrofit.getParameters("pajak").enqueue(
            object : Callback<ArrayList<PajakAppModel>> {
                override fun onFailure(call: Call<ArrayList<PajakAppModel>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<ArrayList<PajakAppModel>>, response: Response<ArrayList<PajakAppModel>>) {
                    val parameters = response.body()
                    onResult(parameters)
                }
            }
        )
    }
}