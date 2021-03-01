package com.android.test.testastropay.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.android.test.testastropay.R
import com.android.test.testastropay.adapter.CitiesAdapter
import com.android.test.testastropay.adapter.City
import com.android.test.testastropay.base.BaseActivity
import com.android.test.testastropay.model.WeatherData
import com.android.test.testastropay.model.WeatherInteractor
import com.android.test.testastropay.presenter.WeatherPresenter
import com.android.test.testastropay.utils.Constants
import com.android.test.testastropay.utils.Constants.DEGREES
import com.android.test.testastropay.utils.Constants.UNIT_WIND_SPEED
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.weather_activity.*
import kotlinx.android.synthetic.main.weather_connection_error.*
import kotlinx.android.synthetic.main.weather_location_disabled.*
import kotlinx.android.synthetic.main.weather_permission_denied.*
import java.util.*

const val PERMISSION_REQUEST_CODE = 8
const val BASE_URL_IMAGES = "http://openweathermap.org/img/w/"
const val IMAGES_EXTENSION = ".png"

class WeatherActivity : BaseActivity<WeatherPresenter>(), WeatherView {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mAdapter: CitiesAdapter? = null
    private var viewSectionList = listOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.weather_activity)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        presenter = WeatherPresenter(this, WeatherInteractor(), fusedLocationClient,
            Geocoder(this, Locale.getDefault()), getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        super.onCreate(savedInstanceState)

        viewSectionList = listOf(llWeatherDetails, llConnectionError, llLocationDisabled, llPermissionDenied)
    }

    override fun setClickListener() {
        tvRetry.setOnClickListener {
            presenter.getWeather()
        }
        tvEnableLocation.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        fbRefresh.setOnClickListener {
            presenter.getWeather()
        }
    }

    private fun viewVisibility(viewVisible: View?, showFloatButton: Boolean) {
        viewSectionList.filterNot { view -> view == viewVisible }.forEach { view -> view.visibility = View.GONE }
        viewVisible?.visibility = View.VISIBLE
        if (showFloatButton) { fbRefresh.visibility = View.VISIBLE } else { fbRefresh.visibility = View.GONE }
    }

    override fun setupSpinnerCities(weatherCities: ArrayList<City>) {
        mAdapter = CitiesAdapter(this, weatherCities)
        spCities.adapter = mAdapter
        spCities.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                presenter.getWeather()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun setupRadioButton() {
        rgSourceSelector.setOnCheckedChangeListener { _, _ ->
            viewVisibility(null, false)
            presenter.getWeather()
        }
    }

    override fun checkSelfPermission(permission: String): Int{
        return ContextCompat.checkSelfPermission(this, permission)
    }

    override fun isLocationActivated(): Boolean {
        return rgSourceSelector.checkedRadioButtonId == R.id.rbLocation
    }

    override fun showLocationSection() {
        llCitiesSection.visibility = View.GONE
        llLocationSection.visibility = View.VISIBLE
    }

    override fun showCitiesSection() {
        llLocationSection.visibility = View.GONE
        llCitiesSection.visibility = View.VISIBLE
    }

    override fun getCitySelected(): City {
        return spCities.selectedItem as City
    }

    override fun showLocation(location: String) {
        tvLocation.text = if (location.isEmpty()) {
            resources.getString(R.string.weather_location_unknown)
        } else {
            location
        }
    }

    override fun showLocationDisabled() {
        tvLocation.text = Constants.STRING_EMPTY
        viewVisibility(llLocationDisabled, true)
    }

    override fun requestPermission(listPermission: Array<String>, permissionRequestcode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(listPermission, permissionRequestcode)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        presenter.getWeather()
                    }
                    if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            tvGoToSettings.text = resources.getString(R.string.weather_go_to_settings)
                            tvGoToSettings.setOnClickListener {
                                startActivityForResult(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", packageName, null)
                                }, PERMISSION_REQUEST_CODE)
                            }
                        } else {
                            tvGoToSettings.text = resources.getString(R.string.weather_activate_permission)
                            tvGoToSettings.setOnClickListener {
                                presenter.getWeather()
                            }
                        }
                        viewVisibility(llPermissionDenied, true)
                    }
                }
                return
            }
        }
    }

    override fun showWeatherData(weatherData: WeatherData) {
        viewVisibility(llWeatherDetails, true)
        val currentTemp = "${weatherData.main.currentTemp.toInt()}${DEGREES}"
        tvTemp.text = currentTemp
        val imageUrl = BASE_URL_IMAGES + weatherData.weather[0].icon + IMAGES_EXTENSION
        Glide.with(applicationContext)
            .load(imageUrl)
            .apply(RequestOptions.centerCropTransform())
            .into(ivWeather)
        val minTemp = "${weatherData.main.minTemp.toInt()}${DEGREES}"
        tvTempMin.text = minTemp
        val maxTemp = "${weatherData.main.maxTemp.toInt()}${DEGREES}"
        tvTempMax.text = maxTemp
        val windSpeed = "${weatherData.wind.speed}${UNIT_WIND_SPEED}"
        tvWindSpeed.text = windSpeed
        val windDeg = "${weatherData.wind.deg.toInt()}${DEGREES}"
        tvWindDeg.text = windDeg
    }

    override fun showFetchError() {
        viewVisibility(llConnectionError, false)
    }
}
