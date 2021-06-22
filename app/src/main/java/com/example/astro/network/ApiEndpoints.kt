package com.example.astro.network


import com.example.astro.model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoints {

    @GET("data/2.5/onecall")
    fun getWeather(
        @Query("lat") lat: String,
        @Query("lon") long: String,
        @Query("units") units: String,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String
    ): Call<Root>

}

