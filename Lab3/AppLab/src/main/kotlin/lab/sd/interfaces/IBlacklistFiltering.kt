package lab.sd.interfaces

import lab.sd.pojos.LocationCoordinatesData

interface IBlacklistFiltering {
    fun checkLocation(location: LocationCoordinatesData): String?
}