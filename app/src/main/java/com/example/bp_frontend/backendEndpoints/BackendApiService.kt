package com.example.bp_frontend.backendEndpoints

import com.example.bp_frontend.dataItems.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

// -------------------------------------------------------
// Here goes the FIELDS and HEADERS that are meant to SEND
// -------------------------------------------------------

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
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part bird_photo: MultipartBody.Part
    ):Call<ObservationDataItem>


    @Multipart
    @POST("observation/newcomment/")
    fun addNewComment(

        @Header("Authorization") token: String,
        @Part("comment") comment:String,
        @Part("observation_id") observation_id:Int

    ):Call<Comment>


    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationWithoutMedia(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

    ):Call<ObservationDataItem>


    @GET("observation/comments/")
    fun fetchObservationsWithComments(
        @Header("Authorization") token: String,

        ):Call<ObservationList>



    @GET("observation/user/")
    fun fetchUserObservations(
        @Header("Authorization") token: String,

        ):Call<RecentObservationsDataItem>


    @GET("whoami/")
    fun whoAmI(@Header("Authorization") token: String
    ):Call<WhoAmIDataItem>



}