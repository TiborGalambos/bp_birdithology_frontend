package com.example.bp_frontend.dataItems

data class EbirdDataItem(
    val bandingCodes: List<String>,
    val category: String,
    val comName: String,
    val comNameCodes: List<Any>,
    val extinct: Boolean,
    val extinctYear: Int,
    val familyCode: String,
    val familyComName: String,
    val familySciName: String,
    val order: String,
    val reportAs: String,
    val sciName: String,
    val sciNameCodes: List<Any>,
    val speciesCode: String,
    val taxonOrder: Double
)
