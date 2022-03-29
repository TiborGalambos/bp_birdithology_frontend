package com.example.bp_frontend.backendEndpoints

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BackendApiClient {
    private lateinit var apiService: BackendApiService

    val BASE_URL = "https://birdithology.azurewebsites.net/"

    fun getApiService(context: Context): BackendApiService {

        if (!::apiService.isInitialized) {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
//                .client(okhttpClient(context))

            apiService = retrofit.create(BackendApiService::class.java)
        }

        return apiService
    }

//    private fun okhttpClient(context: Context): OkHttpClient {
////        return OkHttpClient.Builder()
////            .addInterceptor(AuthInterceptor(context))
////            .build()
////    }

}