package lab.sd.interfaces

import lab.sd.pojos.LocationCoordinatesData

interface IBlacklistFiltering : IChainable{
    fun checkLocation(location: LocationCoordinatesData): String?
}