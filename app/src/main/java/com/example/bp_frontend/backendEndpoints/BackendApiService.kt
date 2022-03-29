package com.example.bp_frontend.backendEndpoints

import com.example.bp_frontend.dataItems.RecentObservationsDataItem
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


// Here goes the FIELDS and HEADERS that are meant to SEND


interface BackendApiService {

    @FormUrlEncoded
    @POST("login/")
    fun login(
        @Field("username") username:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("register/")
    fun register(
        @Field("username") username:String,
        @Field("password") password: String
    ):Call<RegisterResponse>


//    @GET("api/user/")
//    fun userInfo(@Header("Authorization") token: String
//    ):Call<User>


    @POST("logout/")
    fun logOut(@Header("Authorization") token: String
    ):Call<LoginResponse>

    @Multipart
    @POST("observation/normal/")
    fun newNormalObservation(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_x_coords") obs_y_coords: Float,

        @Part bird_photo: MultipartBody.Part
    ):Call<NormalObservationResponse>


    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationWithoutMedia(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_x_coords") obs_y_coords: Float,

    ):Call<NormalObservationResponse>


    @GET("observation/recent/")
    fun fetchNormalObservations(
        @Header("Authorization") token: String,

        ):Call<RecentObservationsDataItem>


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