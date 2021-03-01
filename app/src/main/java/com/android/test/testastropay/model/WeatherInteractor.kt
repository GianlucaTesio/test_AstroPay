package com.android.test.testastropay.model

import com.android.test.testastropay.R
import com.android.test.testastropay.adapter.City
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

const val UNITS = "metric"
const val APP_ID = "eb92d7406c647fde4173f7fbc248ecc9"

class WeatherInteractor {

    fun getWeatherRemoteDataFromCity(cityName: String): Single<WeatherData> {
        return RetrofitBuilder.apiService.getWeatherDataFromCity(APP_ID, UNITS, cityName)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())

    }

    fun getWeatherRemoteDataFromLatLon(latitude: Double, longitude: Double): Single<WeatherListData> {
        return RetrofitBuilder.apiService.getWeatherDataFromLatitudeAndLongitude(APP_ID, latitude, longitude, UNITS)
            .subscribeOn(io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWeatherCities(): ArrayList<City> {
        return arrayListOf(
            City("Buenos Aires", R.drawable.weather_ic_argentina),
            City("Montevideo", R.drawable.weather_ic_uruguay),
            City("Londres", R.drawable.weather_ic_reino_unido),
            City("San Paulo", R.drawable.weather_ic_brasil),
            City("Munich", R.drawable.weather_ic_alemania)
        )
    }
}
