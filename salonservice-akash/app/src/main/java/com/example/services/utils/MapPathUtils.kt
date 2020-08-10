package com.example.services.utils

import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import com.example.services.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline


object MapPathUtils {
    var polylineFinal: Polyline? = null
    // var delegate: GeocodingResponseListener? = null


    fun removePolyline(): Polyline? {
        return polylineFinal
    }


    fun bearingBetweenLocations(sourceLatLng: LatLng, destinationLatLng: LatLng): Float {
        val source = Location("source")
        source.latitude = sourceLatLng.latitude
        source.longitude = sourceLatLng.longitude

        val destination = Location("destination")
        destination.latitude = destinationLatLng.latitude
        destination.longitude = destinationLatLng.longitude
        return source.bearingTo(destination)
    }

}