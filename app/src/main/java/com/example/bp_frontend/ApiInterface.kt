package com.example.bp_frontend
import com.example.bp_frontend.dataItems.EbirdDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("ref/taxonomy/ebird?locale=sk&fmt=json")
    fun getData(): Call<List<EbirdDataItem>>
}