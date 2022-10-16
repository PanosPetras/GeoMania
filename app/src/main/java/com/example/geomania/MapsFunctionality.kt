package com.example.geomania

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer

object MapsFunctionality {
    private var marker: Marker? = null
    private var layer: GeoJsonLayer? = null

    fun highlightArea(googleMap: GoogleMap){

    }

    fun setMapStyle(googleMap: GoogleMap, context: Context, style: Int){
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                context, style
            )
        )
    }

    fun zoomToLocation(googleMap: GoogleMap, location: LatLng, zoom: Float = 6f){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))
    }

    fun addMarker(googleMap: GoogleMap, location: LatLng) {
        marker = googleMap.addMarker(MarkerOptions().position(location))!!
    }

    fun removeMarker(){
        marker?.remove()
    }
}