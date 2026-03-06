package lab.sd.services

import lab.sd.interfaces.IChainable
import lab.sd.interfaces.ITime
import lab.sd.interfaces.IWeatherForecast
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TimeService(private val m_weatherForecastService: IWeatherForecast) : ITime, IChainable{

    override fun getCurrentTime() : String {
        val currentDate = LocalDateTime.now()
        val localDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        return localDateFormatter.format(currentDate)
    }

    override fun progress(dataToSend: String):String {
        if(dataToSend.contains("ERROR")){
            return m_weatherForecastService.progress(dataToSend)
        }
        else{
            val currentTimeStr = getCurrentTime()
            return m_weatherForecastService.progress("$dataToSend<SPLITTER>$currentTimeStr")
        }
    }
}