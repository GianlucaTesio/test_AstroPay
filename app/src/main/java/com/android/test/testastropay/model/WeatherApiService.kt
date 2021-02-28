package com.android.test.testastropay.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeatherDataFromCity(@Query("appid") appId: String,
                               @Query("units") units: String,
                               @Query("q") nameCity: String = ""): Call<WeatherData>

    @GET("find")
    fun getWeatherDataFromLatitudeAndLongitude(@Query("appid") appId: String,
                                               @Query("lat") lat: Double,
                                               @Query("lon") lon: Double,
                                               @Query("units") units: String): Call<WeatherListData>
}