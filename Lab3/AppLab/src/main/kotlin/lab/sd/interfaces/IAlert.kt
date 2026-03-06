package lab.sd.interfaces

import lab.sd.pojos.WeatherForecastData

interface IAlert {
    fun getAlertWeather(weatherForecastData: WeatherForecastData) : String?
}