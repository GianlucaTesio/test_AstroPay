package com.android.test.testastropay.presenter

import android.annotation.SuppressLint
import com.android.test.testastropay.base.BasePresenter
import com.android.test.testastropay.model.WeatherInteractor
import com.android.test.testastropay.view.WeatherView

class WeatherPresenter constructor(private val view: WeatherView, private val weatherInteractor: WeatherInteractor) : BasePresenter() {

    init {
        setupSpinnerCities()
        setupRadioButton()
        setClickListener()
    }

    @SuppressLint("CheckResult")
    fun fetchWeatherDataFromCity(cityName: String) {
        weatherInteractor.getWeatherRemoteDataFromCity(cityName).subscribe({
            view.showWeatherData(it)
        }) {
            view.showFetchError()
        }
    }

    @SuppressLint("CheckResult")
    fun fetchWeatherDataFromLatLon(lat: Double, lon: Double) {
        weatherInteractor.getWeatherRemoteDataFromLatLon(lat, lon).subscribe({
            if (it.list.isNotEmpty()) {
                view.showWeatherData(it.list[0])
            }
        }) {
            view.showFetchError()
        }
    }

    private fun setupSpinnerCities() {
        view.setupSpinnerCities(weatherInteractor.getWeatherCities())
    }

    private fun setupRadioButton() {
        view.setupRadioButton()
    }

    private fun setClickListener() {
        view.setClickListener()
    }

    fun getWeather() {
        view.getWeather()
    }
}
