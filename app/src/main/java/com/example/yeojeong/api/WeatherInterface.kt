package com.example.yeojeong.api

import com.example.yeojeong.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET("getUltraSrtFcst?serviceKey="+BuildConfig.API_KEY)

    fun getWeather(
        @Query("numOfRows") num_of_rows: Int,   // 한 페이지 경과 수
        @Query("pageNo") page_no: Int,          // 페이지 번호
        @Query("dataType") data_type: String,   // 응답 자료 형식
        @Query("base_date") base_date: String,  // 발표 일자
        @Query("base_time") base_time: String,  // 발표 시각
        @Query("nx") nx: Int,                // 예보지점 X 좌표
        @Query("ny") ny: Int,                 // 예보지점 Y 좌표
    ): Call<WEATHER>
}