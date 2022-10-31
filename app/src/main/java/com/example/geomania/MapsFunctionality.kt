package com.example.geomania

import android.content.Context
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.data.geojson.GeoJsonLayer
import org.json.JSONObject

object MapsFunctionality {
    private var marker: Marker? = null
    private var layers: MutableList<GeoJsonLayer> = mutableListOf()

    fun highlightArea(googleMap: GoogleMap, country: String){
        val jsonData: String

        //Load the polygon data of the requested area
        try {
            jsonData = GeoMania.appContext!!.assets.open("GeoJson/Countries/$country.json").bufferedReader().readText()
        } catch (_:Exception){
            return
        }

        //Create the layer with the data we just loaded
        val geoJsonData = JSONObject(jsonData)
        val layer = GeoJsonLayer(googleMap, geoJsonData)
        layer.addLayerToMap()

        //Cache the layer
        layers.add(layer)
    }

    fun removeHighlighting(){
        //Remove all layers from map
        layers.forEach {
            if(it.isLayerOnMap) {
                it.removeLayerFromMap()
            }
        }

        //Empty list
        layers.clear()
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