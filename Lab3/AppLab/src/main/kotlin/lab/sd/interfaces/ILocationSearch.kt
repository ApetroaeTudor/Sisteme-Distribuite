package lab.sd.interfaces

import lab.sd.pojos.LocationCoordinatesData

interface ILocationSearch {
    fun getLocationCoordinates() : LocationCoordinatesData
}