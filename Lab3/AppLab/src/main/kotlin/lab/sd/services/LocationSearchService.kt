package lab.sd.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lab.sd.interfaces.ILocationSearch
import lab.sd.pojos.LocationCoordinatesData
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import javax.swing.UIManager.put

@Service
class LocationSearchService : ILocationSearch {
    private val m_locationURL = "https://ipinfo.io/json"
    private val m_restTemplate = RestTemplate()

    override fun getLocationCoordinates(): LocationCoordinatesData {
        val rawJsonResponse: String = try {
            m_restTemplate.getForObject<String>(m_locationURL, String::class.java)
                ?: run { "Invalid" }
        } catch (e: Exception) {
            "Invalid"
        }
        return parseRawResponse(rawJsonResponse)
    }

    private fun parseRawResponse(rawJson: String): LocationCoordinatesData {
        val json = try {
            Json.parseToJsonElement(rawJson)
        } catch (e: Exception) {
            null
        }

        json?.let {
            val jsonObj = json.jsonObject
            val (latitude, longitude) = jsonObj["loc"]
                ?.jsonPrimitive?.content
                ?.split(",")
                ?.let { it[0] to it[1] }
                ?: ("0" to "0")

            return LocationCoordinatesData(longitude.toDouble(), latitude.toDouble())
        }

        return LocationCoordinatesData()
    }
}