package lab.sd.services

import lab.sd.interfaces.IAlert
import lab.sd.pojos.WeatherForecastData
import org.springframework.stereotype.Service

@Service
class AlertService : IAlert {
    // dummy value for testing purposes
    private val m_maxTemp = 20

    override fun getAlertWeather(weatherForecastData: WeatherForecastData): String? {
        val registeredMaxTemp = weatherForecastData.maxTemp
        return when(registeredMaxTemp > m_maxTemp) {
            true -> weatherForecastData.toString() + "<br /> Registered temperature of ${weatherForecastData.maxTemp} is greater than the defined limit of $m_maxTemp"
            else -> null
        }
    }
}