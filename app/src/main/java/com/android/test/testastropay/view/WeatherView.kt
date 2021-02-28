package com.android.test.testastropay.view

import com.android.test.testastropay.adapter.City
import com.android.test.testastropay.model.WeatherData
import java.util.ArrayList

interface WeatherView {

    fun setupSpinnerCities(weatherCities: ArrayList<City>)

    fun showWeatherData(weatherData: WeatherData)

    fun showFetchError()
}
