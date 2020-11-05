package me.ryanpierce.mapssharedflowdemo

sealed class GpsResult {

    object Standby : GpsResult()

    data class NewLocation(val location: Location) : GpsResult()
}