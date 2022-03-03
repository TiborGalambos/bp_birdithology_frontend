package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class EbirdBirdModel (

    @SerializedName("comName")
    var birdName: String,

//    @SerializedName("username")
//    var username: String,
//
//    @SerializedName("email")
//    var email: String

)

//{
//    "sciName": "Struthio camelus",
//    "comName": "pštros dvojprstý",
//    "speciesCode": "ostric2",
//    "category": "species",
//    "taxonOrder": 1.0,
//    "bandingCodes": [],
//    "comNameCodes": [
//    "COOS"
//    ],
//    "sciNameCodes": [
//    "STCA"
//    ],
//    "order": "Struthioniformes",
//    "familyCode": "struth1",
//    "familyComName": "Ostriches",
//    "familySciName": "Struthionidae"
//}