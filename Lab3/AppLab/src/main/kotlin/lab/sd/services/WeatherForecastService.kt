package lab.sd.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lab.sd.interfaces.IWeatherForecast
import lab.sd.pojos.LocationCoordinatesData
import lab.sd.pojos.WeatherForecastData
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class WeatherForecastService : IWeatherForecast {
    private val m_URLTemplate =
        "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&daily=temperature_2m_max,temperature_2m_min,weathercode&timezone=auto&forecast_days=%d"
    private val m_restTemplate = RestTemplate()

    override fun getForecastData(locationCoordinates: LocationCoordinatesData): WeatherForecastData {
        val finalURL =
            m_URLTemplate.format(locationCoordinates.latitude, locationCoordinates.longitude, 1)
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
}