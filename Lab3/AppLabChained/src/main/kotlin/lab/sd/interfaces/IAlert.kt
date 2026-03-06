package lab.sd.interfaces

import lab.sd.pojos.WeatherForecastData

interface IAlert: IChainable {
    fun getAlertWeather(weatherForecastData: WeatherForecastData) : String?
}