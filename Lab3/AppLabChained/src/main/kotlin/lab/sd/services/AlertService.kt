package lab.sd.services

import kotlinx.serialization.json.Json
import lab.sd.interfaces.IAlert
import lab.sd.interfaces.IChainTerminal
import lab.sd.interfaces.IChainable
import lab.sd.pojos.WeatherForecastData
import org.springframework.stereotype.Service

@Service
class AlertService() : IAlert, IChainable {
    // dummy value for testing purposes
    private val m_maxTemp = 7

    override fun getAlertWeather(weatherForecastData: WeatherForecastData): String? {
        val registeredMaxTemp = weatherForecastData.maxTemp
        return when(registeredMaxTemp > m_maxTemp) {
            true -> weatherForecastData.toString() + "<br /> Registered temperature of ${weatherForecastData.maxTemp} is greater than the defined limit of $m_maxTemp"
            else -> null
        }
    }

    override fun progress(dataToSend: String):String {
        if(dataToSend.contains("ERROR")){
            return dataToSend
        }
        else{
            val weatherData = try { Json.decodeFromString<WeatherForecastData>(dataToSend)} catch (e: Exception) { null }
            val weatherAlert = weatherData?.let { getAlertWeather(weatherData) }
            weatherAlert?.let { return it }?:run { return weatherData.toString() }
        }
    }
}