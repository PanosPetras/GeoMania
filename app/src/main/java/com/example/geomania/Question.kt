package com.example.geomania

import com.google.android.gms.maps.model.LatLng

open class Question(
    val body: String,
    val location: LatLng,
    val zoom: Float,
    val marker: LatLng?)

class MChoiceQuestion(
    body: String,
    val answers: List<String>,
    val correctAnswer: Int,
    location: LatLng,
    zoom: Float,
    marker: LatLng?)
    :Question(body, location, zoom, marker)

class SpellingQuestion(
    body: String,
    val correctAnswer: String,
    location: LatLng,
    zoom: Float,
    marker: LatLng?)
    :Question(body, location, zoom, marker)