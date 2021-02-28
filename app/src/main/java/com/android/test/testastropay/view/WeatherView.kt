package com.android.test.testastropay.view

import com.android.test.testastropay.model.WeatherData

interface WeatherView {
    fun showWeatherData(weatherData: WeatherData)

    fun showFetchError()
}
