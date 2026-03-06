package lab.sd

import lab.sd.interfaces.IAlert
import lab.sd.interfaces.IBlacklistFiltering
import lab.sd.interfaces.ILocationSearch
import lab.sd.interfaces.ITime
import lab.sd.interfaces.IWeatherForecast
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class WeatherOrchestrator(
    private val m_locationSearchService: ILocationSearch,
    private val m_timeService: ITime,
    private val m_weatherForecastService: IWeatherForecast,
    private val m_alertService: IAlert,
    private val m_blacklistService: IBlacklistFiltering
) {

    @GetMapping("/forecast")
    @ResponseBody
    fun receiveGetForecastRequest(): String {
        val locationCoordinates = m_locationSearchService.getLocationCoordinates()
        val validatedLocation   = m_blacklistService.checkLocation(locationCoordinates)?.let {
            return it
        }
        val currentTime         = m_timeService.getCurrentTime()
        val weatherForecast     = m_weatherForecastService.getForecastData(locationCoordinates)
        val alert               = m_alertService.getAlertWeather(weatherForecast)
        return alert?: weatherForecast.toString()
    }
}
