package lab.sd

import lab.sd.interfaces.ILocationSearch
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody


// order is Controller -> LocationService -> BlacklistService -> TimeService -> WeatherForecast -> Alert -> Controller
@Controller
class WeatherChainMaster(
    private val m_locationSearchService: ILocationSearch,
) {

    @GetMapping("/forecast")
    @ResponseBody
    fun receiveGetForecastRequest(): String {
        return m_locationSearchService.progress("")
    }
}
