package com.example.bp_frontend.dataItems

data class NormalObservationDataItem(
    val bird_count: Int,
    val bird_name: String,
    val bird_photo: String,
//    val bird_photo: Any,
    val id: Int,
    val obs_author: Int,
    val obs_author_name: String,
    val obs_time: String,
    val obs_x_coords: Double,
    val obs_y_coords: Double
)