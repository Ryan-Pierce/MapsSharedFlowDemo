package me.ryanpierce.mapssharedflowdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*
import me.ryanpierce.mapssharedflowdemo.MapsDataSource.Companion.CHICAGO

class MapsActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val repository = MapsRepository(MapsDataSource())
        viewModel = ViewModelProvider(this, MapsViewModel.FACTORY(repository))
            .get(MapsViewModel::class.java)

        tourNormalMap()
        tourHybridMap()
        tourListView()
    }

    fun tourNormalMap() {
        val normalMap = supportFragmentManager.findFragmentById(R.id.normalMap) as SupportMapFragment
        normalMap.getMapAsync { map ->
            val marker = map.addMarker(MarkerOptions().position(.0 x 180.0))
            observeLocations(
                viewModel.gpsResults,
                map,
                setup = {
                    mapType = MAP_TYPE_NORMAL
                    isBuildingsEnabled = true
                    moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition(
                                CHICAGO.coordinate, // Coordinate
                                13.2f, // Zoom
                                80f, // Tilt
                                0f // Bearing
                            )
                        )
                    )
                },
                onEachLocation = { location ->
                    map.drawPath(marker, location.coordinate)
                }
            )
        }
    }

    fun tourHybridMap() {
        val hybridMap = supportFragmentManager.findFragmentById(R.id.hybridMap) as SupportMapFragment
        hybridMap.getMapAsync { map ->
            observeLocations(
                viewModel.gpsResults,
                map,
                setup = {
                    mapType = MAP_TYPE_HYBRID
                    isBuildingsEnabled = true
                },
                onEachLocation = { location ->
                    map.moveCamera(
                        CameraUpdateFactory.newCameraPosition(
                            CameraPosition(
                                location.coordinate, // Coordinate
                                15f, // Zoom
                                80f, // Tilt
                                270f // Bearing
                            )
                        )
                    )
                }
            )
        }
    }

    fun CoroutineScope.tourListView() {
        val listView = findViewById<RecyclerView>(R.id.information)
        val adapter = LocationListAdapter().apply { listView.adapter = this }
        viewModel
            .gpsResults
            .filterIsInstance<GpsResult.NewLocation>()
            .map { it.location }
            .scan(emptyList<Location>()) { acc, value -> acc + value }
            .onEach { adapter.submitList(it) }
            .launchIn(this)
    }

    fun CoroutineScope.observeLocations(
        gpsResults: SharedFlow<GpsResult>,
        map: GoogleMap,
        setup: GoogleMap.() -> Unit,
        onEachLocation: suspend (Location) -> Unit
    ) = gpsResults
            .onStart { map.setup() }
            .filterIsInstance<GpsResult.NewLocation>()
            .map { it.location }
            .onEach { onEachLocation(it) }
            .launchIn(this)

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}