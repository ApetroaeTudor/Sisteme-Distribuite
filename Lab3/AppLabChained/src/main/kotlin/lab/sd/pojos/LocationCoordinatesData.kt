package lab.sd.pojos

import kotlinx.serialization.Serializable

@Serializable
data class LocationCoordinatesData(
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)
