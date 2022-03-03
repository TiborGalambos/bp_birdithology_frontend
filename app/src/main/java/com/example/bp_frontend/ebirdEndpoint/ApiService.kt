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


//    @FormUrlEncoded
//    @POST("user/login/")
//    fun login(
//        @Field("username") username:String,
//        @Field("password") password: String
//    ):Call<LoginResponse>

//    @FormUrlEncoded
//    @POST("register/")
//    fun register(
//        @Field("username") username:String,
//        @Field("email") email:String,
//        @Field("password") password: String
//    ):Call<RegisterResponse>


//    @GET("api/user/")
//    fun userInfo(@Header("Authorization") token: String
//    ):Call<User>


//    @POST("user/logout/")
//    fun logOut(@Header("Authorization") token: String
//    ):Call<User>


//    @Multipart
//    @POST("item/")
//    fun postItem(
//        @Header("Authorization") token: String,
//        @Part("category") category:String,
//        @Part("title") title:String,
//        @Part("content") content: String,
//        @Part("price") price:Int,
//        @Part("address") address:String,
//        //@Part("photo") photo:MultipartBody.Part,
//        @Part photo: MultipartBody.Part
//    ):Call<ItemResponse>


//    @ExperimentalMultiplatform
//    @GET("item/all/")
//    fun showAllItems(
//        @Header("Authorization") token: String
//    ):Call<ItemList>
//
//    @ExperimentalMultiplatform
//    @GET("myitems/")
//    fun showAllMyItems(
//        @Header("Authorization") token: String
//    ):Call<ItemList>
//
//
//    @GET("item/{item_id_url}/")
//    fun getThisItem(
//        @Path("item_id_url") item_id_url:Int,
//        @Header("Authorization") token: String
//    ):Call<ItemResponse>
//
//    @DELETE("myitems/delete/{item_id_url}/")
//    fun deleteThisItem(
//        @Path("item_id_url") item_id_url:Int,
//        @Header("Authorization") token: String
//    ):Call<DeleteResponse>
//
//    @Multipart
//    @PUT("myitems/update/{item_id_url}/")
//    fun editThisItem(
//        @Path("item_id_url") item_id_url:Int,
//        @Header("Authorization") token: String,
//        @Part("category") category:String,
//        @Part("title") title:String,
//        @Part("content") content: String,
//        @Part("price") price:Int,
//        @Part("address") address:String,
//    ):Call<ItemResponse>
//
//    @Multipart
//    @POST("search/")
//    fun searchThisItemByKeyword(
//        @Header("Authorization") token: String,
//        @Part("query") query: RequestBody,
//    ):Call<ItemList>
//
//    @Multipart
//    @POST("category/")
//    fun searchThisItemByCategory(
//        @Header("Authorization") token: String,
//        @Part("query") query: RequestBody,
//    ):Call<ItemList>


}