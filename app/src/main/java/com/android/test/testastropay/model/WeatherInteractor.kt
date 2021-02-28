package com.android.test.testastropay.model

import com.android.test.testastropay.R
import com.android.test.testastropay.adapter.City
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

const val UNITS = "metric"
const val APP_ID = "eb92d7406c647fde4173f7fbc248ecc9"
const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

class WeatherInteractor {

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    companion object {
        private lateinit var retrofit: Retrofit
    }

    interface onDataFetched {
        fun onSuccess(weatherData: WeatherData)
        fun onFailure()
    }

    fun getWeatherRemoteDataFromCity(listener: onDataFetched, cityName: String) {
        val service = retrofit.create(WeatherApiService::class.java)
        service.getWeatherDataFromCity(APP_ID, UNITS, cityName)
            .enqueue(object : Callback<WeatherData> {
                override fun onResponse(
                    call: Call<WeatherData>,
                    response: Response<WeatherData>
                ) {
                    if (!response.isSuccessful) {
                        listener.onFailure()
                        return
                    }

                    response.body()?.let {
                        listener.onSuccess(it)
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    listener.onFailure()
                }
            })
    }

    fun getWeatherRemoteDataFromLatLon(listener: onDataFetched, latitude: Double, longitude: Double) {
        val service = retrofit.create(WeatherApiService::class.java)
        service.getWeatherDataFromLatitudeAndLongitude(APP_ID, latitude, longitude, UNITS)
            .enqueue(object : Callback<WeatherListData> {
                override fun onResponse(
                    call: Call<WeatherListData>,
                    response: Response<WeatherListData>
                ) {
                    if (!response.isSuccessful) {
                        listener.onFailure()
                        return
                    }

                    response.body()?.let {
                        listener.onSuccess(it.list[0])
                    }
                }

                override fun onFailure(call: Call<WeatherListData>, t: Throwable) {
                    listener.onFailure()
                }
            })
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
