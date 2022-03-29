package com.example.bp_frontend.backendEndpoints

import com.google.gson.annotations.SerializedName


data class NormalObservationResponse(

    @SerializedName("bird_count")
    var bird_count: Int,

    @SerializedName("bird_name")
    var bird_name: String,

    @SerializedName("bird_photo")
    var bird_photo: String,

    @SerializedName("id")
    var id: Int,

    @SerializedName("obs_author")
    var obs_author: Int,

    @SerializedName("obs_author_name")
    var obs_author_name: String,

    @SerializedName("obs_time")
    var obs_time: String,

    @SerializedName("obs_x_coords")
    var obs_x_coords: Double,

    @SerializedName("obs_y_coords")
    var obs_y_coords: Double
)