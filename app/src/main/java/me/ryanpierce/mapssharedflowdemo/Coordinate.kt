package me.ryanpierce.mapssharedflowdemo

import com.google.android.gms.maps.model.LatLng

typealias Coordinate = LatLng

infix fun Double.x(that: Double) = Coordinate(this, that)