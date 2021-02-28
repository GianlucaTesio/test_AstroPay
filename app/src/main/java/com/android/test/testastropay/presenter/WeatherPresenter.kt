package com.android.test.testastropay.presenter

import com.android.test.testastropay.adapter.City
import com.android.test.testastropay.base.BasePresenter
import com.android.test.testastropay.model.WeatherData
import com.android.test.testastropay.model.WeatherInteractor
import com.android.test.testastropay.view.WeatherView
import java.util.ArrayList

class WeatherPresenter constructor(view: WeatherView, weatherInteractor: WeatherInteractor) : BasePresenter() {

    var view: WeatherView? = null
    var weatherInteractor: WeatherInteractor? = null

    init {
        this.view = view
        this.weatherInteractor = weatherInteractor
    }

    fun fetchWeatherDataFromCity(cityName: String) {
        weatherInteractor?.getWeatherRemoteDataFromCity(object : WeatherInteractor.onDataFetched {
            override fun onSuccess(weatherData: WeatherData) {
                view?.showWeatherData(weatherData)
            }

            override fun onFailure() {
                view?.showFetchError()
            }
        }, cityName)
    }

    fun fetchWeatherDataFromLatLon(lat: Double, lon: Double) {
        weatherInteractor?.getWeatherRemoteDataFromLatLon(object : WeatherInteractor.onDataFetched {
            override fun onSuccess(weatherData: WeatherData) {
                view?.showWeatherData(weatherData)
            }

            override fun onFailure() {
                view?.showFetchError()
            }
        }, lat, lon)
    }

    fun getCitiesWeather(): ArrayList<City> {
        return weatherInteractor?.getWeatherCities() ?: arrayListOf()
    }
}
