package lab.sd.pojos

import kotlinx.serialization.Serializable
import lab.sd.utils.Direction

@Serializable
data class WeatherForecastData(
    val location: String            = "Undefined",
    val date: String                = "Undefined",
    val weatherCode: String         = "Undefined",
    val minTemp: Double             = 0.0,
    val maxTemp: Double             = 0.0,
    val windDirection: Direction    = Direction.UNKNOWN
)
