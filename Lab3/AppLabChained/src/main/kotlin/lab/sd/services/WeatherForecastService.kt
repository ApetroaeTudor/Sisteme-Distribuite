package lab.sd.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lab.sd.interfaces.IAlert
import lab.sd.interfaces.IBlacklistFiltering
import lab.sd.interfaces.IChainable
import lab.sd.interfaces.IWeatherForecast
import lab.sd.pojos.LocationCoordinatesData
import lab.sd.pojos.WeatherForecastData
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class WeatherForecastService(private val m_alertService: IAlert) : IWeatherForecast, IChainable {
    private val m_URLTemplate =
        "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=auto&forecast_days=%d"
    private val m_restTemplate = RestTemplate()
    private val m_numberOfDays = 1

    override fun getForecastData(locationCoordinates: LocationCoordinatesData): WeatherForecastData {
        val finalURL = m_URLTemplate.format(locationCoordinates.latitude, locationCoordinates.longitude, m_numberOfDays)
        val rawResponse = try {
            m_restTemplate.getForObject<String>(finalURL)
        } catch (e: Exception) {
            "Invalid"
        }
        return parseRawResponse(rawResponse, locationCoordinates)
    }

    private fun parseRawResponse(rawJson:String, locationCoordinates: LocationCoordinatesData): WeatherForecastData {
        val json = try {
            Json.parseToJsonElement(rawJson)
        } catch (e: Exception) {
            null
        }

        val dailyJson    = json?.jsonObject?.get("daily")
        val dailyJsonObj = dailyJson?.jsonObject
        val timeDay      = dailyJsonObj?.get("time")?.jsonArray?.get(0)?.toString()?:""
        val tempMax      = dailyJsonObj?.get("temperature_2m_max")?.jsonArray?.get(0)?.toString()?:"0"
        val tempMin      = dailyJsonObj?.get("temperature_2m_min")?.jsonArray?.get(0)?.toString()?:"0"
        val weatherCode  = dailyJsonObj?.get("weathercode")?.jsonArray?.get(0)?.toString()?:"0"

        return WeatherForecastData(
            location    = locationCoordinates.toString(),
            date        = timeDay,
            weatherCode = weatherCode,
            minTemp     = tempMin.toDouble(),
            maxTemp     = tempMax.toDouble(),
        )
    }

    override fun progress(dataToSend: String): String  {
        if(dataToSend.contains("ERROR")){
            m_alertService.progress(dataToSend)
        }
        else{
            val splitData = dataToSend.split("<SPLITTER>")
            val currentTime = splitData[1]
            val jsonStringLocation = splitData[0]
            val locationInformation = try { Json.decodeFromString<LocationCoordinatesData>(jsonStringLocation) } catch(e: Exception) { null }
            val weatherForecast = locationInformation?.let { getForecastData(locationInformation) }
            weatherForecast?.let {
                return if (it.minTemp == 0.0 || it.maxTemp == 0.0){
                    m_alertService.progress("ERROR - Invalid weather data")
                } else{
                    m_alertService.progress(Json.encodeToString(weatherForecast))
                }
            }?: run{ return m_alertService.progress("ERROR - Invalid weather data") }
        }

        return dataToSend
    }
}