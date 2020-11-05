package me.ryanpierce.mapssharedflowdemo

import kotlinx.coroutines.flow.flowOf

class MapsDataSource {

    companion object {
        val CHICAGO = "Chicago" at (41.8925 x -87.6250)
    }

    val locations = flowOf(
        /**
         * Photo Credit:
         * Melissa Askew
         * https://unsplash.com/@melissaaskew
         */
        "Navy Pier"
                at (41.8917 x -87.6090)
                withDescription "Tourist attraction in Chicago, Illinois"
                withImage R.drawable.navy_pier,

        /**
         * Photo Credit:
         * Matthew Mazzei
         * https://unsplash.com/@mattymazzei
         */
        "Lincoln Park Zoo"
                at (41.9210 x -87.6335)
                withDescription "Zoo in Chicago, Illinois"
                withImage R.drawable.lincoln_park_zoo,

        /**
         * Photo Credit:
         * George Bakos
         * https://unsplash.com/@georgebakos
         */
        "Adler Planetarium"
                at (41.8663 x -87.6069)
                withDescription "Museum in Chicago, Illinois"
                withImage R.drawable.adler_planetarium
    )
}