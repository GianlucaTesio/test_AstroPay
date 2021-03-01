package com.android.test.testastropay.presenter

import android.location.Geocoder
import android.location.LocationManager
import com.android.test.testastropay.R
import com.android.test.testastropay.adapter.City
import com.android.test.testastropay.model.*
import com.android.test.testastropay.view.WeatherView
import com.google.android.gms.location.FusedLocationProviderClient
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.Spy

class WeatherPresenterTest {
    private lateinit var weatherData: WeatherData

    @Mock
    private var view: WeatherView = mock(WeatherView::class.java)

    @Mock
    private var interactor: WeatherInteractor = mock(WeatherInteractor::class.java)

    @Mock
    private var fusedLocationProviderClient: FusedLocationProviderClient = mock(FusedLocationProviderClient::class.java)

    @Mock
    private var geocoder: Geocoder = mock(Geocoder::class.java)

    @Mock
    private var locationManager: LocationManager = mock(LocationManager::class.java)

    @Spy
    private lateinit var presenter: WeatherPresenter

    @Before
    fun setUp() {
        weatherData = getWeatherDataMock()
    }

    @Test
    fun testWeatherPresenter_fetchWeatherDataFromCity_WorkingOk() {
        val observable = Single.just(weatherData)
        val testObserver: TestObserver<WeatherData> = TestObserver.create()
        observable.subscribe(testObserver)

        `when`(interactor.getWeatherRemoteDataFromCity(anyString()))
        .thenReturn(observable)
        `when`(view.getCitySelected()).thenReturn(getCityMock())
        presenter = WeatherPresenter(view, interactor, fusedLocationProviderClient, geocoder, locationManager)

        verify(view, times(1))
            .showWeatherData(weatherData)
    }

    @Test
    fun testWeatherPresenter_fetchWeatherDataFromLatLon_WorkingOk() {
        val weatherListData = WeatherListData(arrayListOf(weatherData))
        val observable = Single.just(weatherListData)
        val testObserver: TestObserver<WeatherListData> = TestObserver.create()
        observable.subscribe(testObserver)

        `when`(interactor.getWeatherRemoteDataFromLatLon(anyDouble(), anyDouble()))
            .thenReturn(observable)

        `when`(view.isLocationActivated()).thenReturn(true)
        presenter = WeatherPresenter(view, interactor, fusedLocationProviderClient, geocoder, locationManager)


        verify(view, times(1))
            .checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        verify(view, times(1))
            .showLocationDisabled()
    }

    private fun getWeatherDataMock(): WeatherData {
        return WeatherData(Main(23.5, 18.2, 27.6), Wind(34.1, 45.0), arrayListOf<Weather>(Weather("01n")))
    }

    private fun getCityMock(): City {
        return City("Buenos Aires", R.drawable.weather_ic_argentina)
    }
}
