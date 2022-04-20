package com.example.bp_frontend.backendEndpoints

import com.example.bp_frontend.dataItems.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

// -------------------------------------------------------
// Here goes the FIELDS and HEADERS that are meant to SEND
// -------------------------------------------------------

interface BackendApiService {


    //    ---------------------------------------------
    //    USER operations
    //    ---------------------------------------------

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


    @GET("whoami/")
    fun whoAmI(@Header("Authorization") token: String
    ):Call<WhoAmIDataItem>


    @GET("amiadmin/")
    fun amIAdmin(@Header("Authorization") token: String
    ):Call<AdminVerification>



    //    ---------------------------------------------
    //    Adding new OBSERVATIONS - Normal
    //    ---------------------------------------------

    //photo, voice
    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationWithPhotoWithVoice(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part bird_photo: MultipartBody.Part,
        @Part bird_recording: MultipartBody.Part

    ):Call<ObservationDataItem>

    //photo
    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationWithPhotoNoVoice(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part bird_photo: MultipartBody.Part,

    ):Call<ObservationDataItem>


    //voice
    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationNoPhotoWithVoice(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part bird_recording: MultipartBody.Part

    ):Call<ObservationDataItem>


    //nothing
    @Multipart
    @POST("observation/normal/")
    fun newNormalObservationNoPhotoNoVoice(
        @Header("Authorization") token: String,

        @Part("bird_name") bird_name:String,
        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

    ):Call<ObservationDataItem>


    //    ---------------------------------------------
    //    Adding new OBSERVATIONS - Simple
    //    ---------------------------------------------


    // photo
    @Multipart
    @POST("observation/simple/")
    fun newSimpleObservationWithPhotoNoVoice(
        @Header("Authorization") token: String,

        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part("bird_size") bird_size: String,
        @Part("bird_color") bird_color: String,
        @Part("bird_beak") bird_beak: String,

        @Part bird_photo: MultipartBody.Part,
    ):Call<ObservationDataItem>


    // nothing
    @Multipart
    @POST("observation/simple/")
    fun newSimpleObservationNoPhotoNoVoice(
        @Header("Authorization") token: String,

        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part("bird_size") bird_size: String,
        @Part("bird_color") bird_color: String,
        @Part("bird_beak") bird_beak: String,


        ):Call<ObservationDataItem>

    // voice
    @Multipart
    @POST("observation/simple/")
    fun newSimpleObservationNoPhotoWithVoice(
        @Header("Authorization") token: String,

        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part("bird_size") bird_size: String,
        @Part("bird_color") bird_color: String,
        @Part("bird_beak") bird_beak: String,

        @Part bird_recording: MultipartBody.Part
    ):Call<ObservationDataItem>


    // photo voice
    @Multipart
    @POST("observation/simple/")
    fun newSimpleObservationWithPhotoWithVoice(
        @Header("Authorization") token: String,

        @Part("bird_count") bird_count: Int,

        @Part("obs_x_coords") obs_x_coords: Float,
        @Part("obs_y_coords") obs_y_coords: Float,

        @Part("obs_place") obs_place: String,

        @Part("bird_size") bird_size: String,
        @Part("bird_color") bird_color: String,
        @Part("bird_beak") bird_beak: String,

        @Part bird_recording: MultipartBody.Part,
        @Part bird_photo: MultipartBody.Part,

        ):Call<ObservationDataItem>




    //    ---------------------------------------------
    //    Adding new COMMENT on observation
    //    ---------------------------------------------

    @Multipart
    @POST("observation/newcomment/")
    fun addNewComment(

        @Header("Authorization") token: String,
        @Part("comment") comment:String,
        @Part("observation_id") observation_id:Int

    ):Call<Comment>




    //    ---------------------------------------------
    //    Fetching OBSERVATIONS
    //    ---------------------------------------------


    @GET("observation/unconfirmed/{page_number}")
    fun fetchUnconfirmedObservations(
        @Path("page_number") page_number:Int,
        @Header("Authorization") token: String,

        ):Call<ObservationList>

    @Multipart
    @PUT("observation/species/locations/")
    fun fetchObservationsForMap(
        @Part("bird_name") bird_name: String,
        @Header("Authorization") token: String,

        ):Call<Locations>

    @Multipart
    @PUT("observation/species/occurrence/")
    fun fetchObservationsForGraph(
        @Part("bird_name") bird_name: String,
        @Part("year") year: Int,
        @Header("Authorization") token: String,

        ):Call<GraphDataItem>


    @GET("observation/recent")
    fun fetchObservationsWithComments(
        @Header("Authorization") token: String,

        ):Call<ObservationList>


    @GET("observation/user/{page_number}")
    fun fetchUserObservations(
        @Path("page_number") page_number:Int,
        @Header("Authorization") token: String,

        ):Call<ObservationList>


    @GET("observation/comments/{page_number}")
    fun fetchAllConfirmedObservations(
        @Path("page_number") page_number:Int,
        @Header("Authorization") token: String,

        ):Call<ObservationList>




    //    ---------------------------------------------
    //    Fetching STAT data
    //    ---------------------------------------------

    @GET("stats/sum/")
    fun fetchStatsSum(
        @Header("Authorization") token: String,

        ):Call<GlobStatsSum>


    @GET("stats/sum/personal")
    fun fetchStatsSumPersonal(
        @Header("Authorization") token: String,

        ):Call<GlobStatsSum>


    @GET("stats/birds/")
    fun fetchBirdStatsSum(
        @Header("Authorization") token: String,

        ):Call<BirdSpeciesStats>



    //    ---------------------------------------------
    //    Admin UPDATE observation
    //    ---------------------------------------------

    @Multipart
    @PUT("observation/unconfirmed/update/{obs_number}")
    fun updateObservation(
        @Path("obs_number") obs_number:Int,
        @Part("bird_name") bird_name: String,
        @Header("Authorization") token: String,
        ):Call<UpdateConfirm>


    @DELETE("observation/unconfirmed/update/{obs_number}")
    fun deleteObservation(
        @Path("obs_number") obs_number:Int,
        @Header("Authorization") token: String,
    ):Call<UpdateConfirm>




}