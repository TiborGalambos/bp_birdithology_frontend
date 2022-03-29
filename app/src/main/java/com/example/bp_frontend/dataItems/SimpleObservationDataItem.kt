package com.example.bp_frontend.dataItems

data class SimpleObservationDataItem(
    val bird_count: Int,
    val bird_photo: Any,
    val bird_size: String,
    val id: Int,
    val obs_author: Int,
    val obs_author_name: String,
    val obs_description: Any,
    val obs_time: String,
    val obs_x_coords: Double,
    val obs_y_coords: Double
)