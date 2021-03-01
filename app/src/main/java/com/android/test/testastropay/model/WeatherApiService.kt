package com.android.test.testastropay.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    fun getWeatherDataFromCity(@Query("appid") appId: String,
                               @Query("units") units: String,
                               @Query("q") nameCity: String = ""): Single<WeatherData>

    @GET("find")
    fun getWeatherDataFromLatitudeAndLongitude(@Query("appid") appId: String,
                                               @Query("lat") lat: Double,
                                               @Query("lon") lon: Double,
                                               @Query("units") units: String): Single<WeatherListData>
}
