package com.example.geomania

import com.google.android.gms.maps.model.LatLng

data class Question(val type: QuestionType,
                    val body: String,
                    val answers: List<String>,
                    val correctAnswer: Int,
                    val location: LatLng,
                    val zoom: Float,
                    val marker: LatLng?)

enum class QuestionType {
    MapShowsWithQuestion, MapShowsAfterQuestion
}