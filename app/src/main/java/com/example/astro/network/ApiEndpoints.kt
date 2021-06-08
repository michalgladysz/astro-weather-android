package com.example.astro.network


import com.example.astro.model.Root
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoints {

    @GET("onecall?lat=51&lon=22&units=metric&exclude=hourly,minutely,alerts&appid=ea96da650b548546f840ea1f5a8f13af")
    fun getWeather(): Call<Root>

}