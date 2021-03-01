package com.android.test.testastropay.presenter

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.android.test.testastropay.base.BasePresenter
import com.android.test.testastropay.model.WeatherInteractor
import com.android.test.testastropay.utils.Constants.STRING_EMPTY
import com.android.test.testastropay.view.PERMISSION_REQUEST_CODE
import com.android.test.testastropay.view.WeatherView
import com.google.android.gms.location.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class WeatherPresenter constructor(
    private val view: WeatherView,
    private val weatherInteractor: WeatherInteractor
) : BasePresenter() {
    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    init {
        setupSpinnerCities()
        setupRadioButton()
        setClickListener()
    }

    fun fetchWeatherDataFromCity(cityName: String) {
        val disposable :Disposable = weatherInteractor.getWeatherRemoteDataFromCity(cityName).subscribe({
            view.showWeatherData(it)
        }) {
            view.showFetchError()
        }
        compositeDisposable.add(disposable)
    }


    fun fetchWeatherDataFromLatLon(lat: Double, lon: Double) {
        val disposable :Disposable = weatherInteractor.getWeatherRemoteDataFromLatLon(lat, lon).subscribe(
            {
                if (it.list.isNotEmpty()) {
                    view.showWeatherData(it.list[0])
                }
            }) {
            view.showFetchError()
        }
        compositeDisposable.add(disposable)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(
        checkSelfLocationPermission: Int,
        locationManager: LocationManager,
        geocoder: Geocoder,
        fusedLocationClient: FusedLocationProviderClient
    ) {
        if (checkSelfLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

                val mLocationRequest = LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    interval = 10000
                    fastestInterval = 1000
                    numUpdates = 1
                }

                val mLocationCallback: LocationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        fetchWeatherDataFromLatLon(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)

                        val addresses = geocoder.getFromLocation(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude, 1)
                        view.showLocation(if (addresses.isEmpty()) {
                            STRING_EMPTY
                        } else {
                            addresses[0].getAddressLine(0)
                        })
                    }

                    @SuppressLint("MissingPermission")
                    override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                        if (!locationAvailability.isLocationAvailable) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                                location?.let {
                                    fetchWeatherDataFromLatLon(it.latitude, it.longitude)
                                }
                            }
                        }
                    }
                }
                fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
            } else {
                view.showLocationDisabled()
            }
        } else {
            view.requestPermission(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        compositeDisposable.dispose()
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
