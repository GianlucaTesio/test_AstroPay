package com.android.test.testastropay.presenter

import com.android.test.testastropay.model.*
import com.android.test.testastropay.view.WeatherView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import junit.framework.TestCase
import kotlinx.android.synthetic.main.weather_activity.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Spy
import java.util.*

class WeatherPresenterTest {
    private lateinit var weatherData: WeatherData

    @Mock
    private var view: WeatherView = mock(WeatherView::class.java)

    @Mock
    private var interactor: WeatherInteractor = mock(WeatherInteractor::class.java)

    @Spy
    private var presenter: WeatherPresenter = WeatherPresenter(view, interactor)

    @Before
    fun setUp() {
        weatherData = getWeatherDataMock()
    }

    @Test
    fun testWeatherPresenter_fetchWeatherDataFromCity_WorkingOk() {
        val observable = Single.just(weatherData)
        val testObserver: TestObserver<WeatherData> = TestObserver.create()
        observable.subscribe(testObserver)

        `when`(interactor.getWeatherRemoteDataFromCity(Mockito.anyString()))
        .thenReturn(observable)

        presenter.fetchWeatherDataFromCity("Ciudad")

        verify(view, times(1))
            .showWeatherData(weatherData)
    }

    @Test
    fun testWeatherPresenter_fetchWeatherDataFromLatLon_WorkingOk() {
        val weatherListData = WeatherListData(arrayListOf(weatherData))
        val observable = Single.just(weatherListData)
        val testObserver: TestObserver<WeatherListData> = TestObserver.create()
        observable.subscribe(testObserver)

        `when`(interactor.getWeatherRemoteDataFromLatLon(Mockito.anyDouble(), Mockito.anyDouble()))
            .thenReturn(observable)

        presenter.fetchWeatherDataFromLatLon(0.0, 0.0)

        verify(view, times(1))
            .showWeatherData(weatherData)
    }

    private fun getWeatherDataMock(): WeatherData {
        return WeatherData(Main(23.5, 18.2, 27.6), Wind(34.1, 45.0), arrayListOf<Weather>(Weather("01n")))
    }
}
