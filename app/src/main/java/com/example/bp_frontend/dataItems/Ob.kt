package com.example.bp_frontend.dataItems

data class Ob(
    val author_id: Int,
    val bird_count: Int,
    val bird_name: String,
    val bird_photo: String,
    val bird_size: Any,
    val id: Int,
    val obs_author: String,
    val obs_description: Any,
    val obs_is_confirmed: Boolean,
    val obs_is_simple: Boolean,
    val obs_place: String,
    val obs_time: String,
    val obs_x_coords: Double,
    val obs_y_coords: Double
)