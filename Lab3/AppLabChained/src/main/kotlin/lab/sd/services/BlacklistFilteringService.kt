package lab.sd.services

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import lab.sd.interfaces.IBlacklistFiltering
import lab.sd.interfaces.IChainable
import lab.sd.interfaces.ITime
import lab.sd.pojos.LocationCoordinatesData
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class BlacklistFilteringService(private val m_timeService: ITime) : IBlacklistFiltering, IChainable{
    private val m_blacklist    = listOf("Bucharest")
    private val m_URLTemplate  = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=%f&longitude=%f&localityLanguage=en"
    private val m_restTemplate = RestTemplate()
    override fun checkLocation(location: LocationCoordinatesData): String? {
        val latitude  = location.latitude
        val longitude = location.longitude
        if(latitude == 0.0 || longitude == 0.0){
            return "Invalid Location"
        }
        val builtURL = m_URLTemplate.format(latitude, longitude)

        val rawJsonResponse = try {
            m_restTemplate.getForObject<String>(builtURL)
        } catch (e: Exception){
            return "Url GET for city location failed"
        }

        val json = try {
            Json.parseToJsonElement(rawJsonResponse)
        } catch (e: Exception) {
            null
        }

        val jsonObj = json?.jsonObject
        val foundCity = jsonObj?.get("city")?.jsonPrimitive?.content?:run {
            return "No found city in request"
        }

        return if(m_blacklist.contains(foundCity)){ "Your city is blacklisted" } else{ null }
    }

    override fun progress(dataToSend: String): String {
        val locationData = try { Json.decodeFromString<LocationCoordinatesData>(dataToSend) } catch (e: Exception) { null }
        val checkedLocation = locationData?.let { checkLocation(it) }
        checkedLocation?.let { return m_timeService.progress("ERROR - $checkedLocation") }?: run { return m_timeService.progress(dataToSend) }
    }
}