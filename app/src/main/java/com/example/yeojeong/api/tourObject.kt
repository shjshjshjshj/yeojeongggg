package com.example.yeojeong.api

import com.example.yeojeong.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object tourObject {


    private fun getTour(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://apis.data.go.kr/B551011/KorService1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    fun getTourService(): tourApiPosInterface{
        return  getTour().create(tourApiPosInterface::class.java) //retrofit객체 만듦!
    }
    fun getTourImgService(): tourApiImgResponse{
        return getTour().create(tourApiImgResponse::class.java)
    }
}