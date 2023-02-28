package com.example.yeojeong.api

import com.google.gson.annotations.SerializedName

data class tourApiImgResponse(
    @SerializedName("contentid")
    var addr1: String = "",
    @SerializedName("originimgurl")
    var originimgurl: String = "",
    @SerializedName("serialnum")
    var serialnum: String = "",
)
data class TOURIMG (val response : imREPONSE)
data class imREPONSE(val header : imHEADER, val body : imBODY)
data class imHEADER(val resultCode : Int, val resultMsg : String)
data class imBODY(val items : imITEMS, val numOfRows : Int, val PageNo : Int, val totalCount : Int)
data class imITEMS(val item : List<imITEM>)

data class imITEM(
    val contentid: String,
    val originimgurl: String,
    val serialnum: String,
)