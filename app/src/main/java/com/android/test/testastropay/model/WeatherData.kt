package com.android.test.testastropay.model

import com.google.gson.annotations.SerializedName

data class WeatherData (val main: Main,
                        val wind: Wind,
                        val weather: ArrayList<Weather>)

data class WeatherListData(val list: ArrayList<WeatherData>)

data class Main (@SerializedName("temp") val currentTemp: Double = 0.0,
                 @SerializedName("temp_min") val minTemp: Double = 0.0,
                 @SerializedName("temp_max") val maxTemp: Double = 0.0)

data class Wind (val speed: Double = 0.0,
                 val deg: Double = 0.0)

data class Weather (val icon: String = "")
