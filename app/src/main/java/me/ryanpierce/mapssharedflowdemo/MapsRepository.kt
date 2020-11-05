package me.ryanpierce.mapssharedflowdemo

import kotlinx.coroutines.flow.map

class MapsRepository(dataSource: MapsDataSource) {

    // Pretend GPS locations from a hard-coded data source
    val gpsLocations = dataSource.locations.map { GpsResult.NewLocation(it) }
}