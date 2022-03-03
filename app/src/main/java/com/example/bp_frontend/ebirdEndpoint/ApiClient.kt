package com.example.bp_frontend.ebirdEndpoint

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    private lateinit var apiService: ApiService

    val BASE_URL = "https://api.ebird.org/v2/"

    fun getApiService(context: Context): ApiService {

        if (!::apiService.isInitialized) {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
//                .client(okhttpClient(context))

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

//    private fun okhttpClient(context: Context): OkHttpClient {
////        return OkHttpClient.Builder()
////            .addInterceptor(AuthInterceptor(context))
////            .build()
////    }

}