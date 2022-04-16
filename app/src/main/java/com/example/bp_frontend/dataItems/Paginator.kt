package com.example.bp_frontend.dataItems

data class Paginator(
    val current_page: Int,
    val has_next: Boolean,
    val has_prev: Boolean,
    val total_pages: Int
)