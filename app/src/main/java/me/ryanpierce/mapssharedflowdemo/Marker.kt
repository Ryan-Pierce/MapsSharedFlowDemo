package me.ryanpierce.mapssharedflowdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Animates the marker after moving to a new location
 */
suspend fun Marker.moveTo(coordinate: Coordinate) = suspendCancellableCoroutine<Unit> { continuation ->
    // Animate movement
    val latitudeAnimation = ValueAnimator.ofFloat(
        position.latitude.toFloat(),
        coordinate.latitude.toFloat()
    ).apply {
        addUpdateListener { animation ->
            val latitude = (animation.animatedValue as Float).toDouble()
            val longitude = position.longitude
            position = latitude x longitude
        }
    }
    val longitudeAnimation = ValueAnimator.ofFloat(
        position.longitude.toFloat(),
        coordinate.longitude.toFloat()
    ).apply {
        addUpdateListener { animation ->
            val latitude = position.latitude
            val longitude = (animation.animatedValue as Float).toDouble()
            position = latitude x longitude
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                continuation.resume(Unit) {}
            }
        })
    }
    AnimatorSet().apply {
        playTogether(latitudeAnimation, longitudeAnimation)
        duration = 3000
        start()
    }
}

suspend fun GoogleMap.drawPath(marker: Marker, coordinate: Coordinate) {
    addPolyline(
        PolylineOptions()
            .add(marker.position, coordinate)
            .color(Color.rgb(128, 0, 128))
            .pattern(listOf(Dash(20f), Gap(20f)))
    )
    marker.moveTo(coordinate)
}