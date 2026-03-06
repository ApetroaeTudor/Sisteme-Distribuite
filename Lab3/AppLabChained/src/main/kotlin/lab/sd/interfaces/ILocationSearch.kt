package lab.sd.interfaces

import lab.sd.pojos.LocationCoordinatesData

interface ILocationSearch : IChainable {
    fun getLocationCoordinates() : LocationCoordinatesData
}