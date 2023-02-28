package com.example.yeojeong.api

import com.example.yeojeong.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface tourImgInterface {
    @GET("detailImage1?serviceKey="+BuildConfig.API_KEY+"&MobileOS=AND&MobileApp=YeoJeong&_type=json&imageYN=Y&subImageYN=Y&numOfRows=100&pageNo=1")
    fun getTourimg(
        @Query("contentId")
        contentId: String
    ) : Call<TOURIMG>

}