package com.example.bp_frontend.ebirdEndpoint

import com.example.myapplication.models.EbirdBirdModel
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("ref/taxonomy/ebird?locale=sk&fmt=json&species=ostric2")
    fun getBirdNames():Call<EbirdBirdModel>



}