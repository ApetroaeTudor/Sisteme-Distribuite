package lab.sd.interfaces

import lab.sd.pojos.LocationCoordinatesData
import lab.sd.pojos.WeatherForecastData

interface IWeatherForecast : IChainable {
    fun getForecastData(locationCoordinates: LocationCoordinatesData) : WeatherForecastData
}