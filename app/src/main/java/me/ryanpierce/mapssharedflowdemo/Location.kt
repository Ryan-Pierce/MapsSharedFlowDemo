package me.ryanpierce.mapssharedflowdemo

data class Location(
    val name: String,
    val coordinate: Coordinate,
    val description: String?,
    val imageId: Int?
)

infix fun String.at(that: Coordinate) = Location(this, that, null, null)

infix fun Location.withImage(imageId: Int?) =
    Location(this.name, this.coordinate, this.description, imageId)

infix fun Location.withDescription(description: String?) =
    Location(this.name, this.coordinate, description, this.imageId)