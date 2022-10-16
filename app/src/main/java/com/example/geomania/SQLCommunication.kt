package com.example.geomania

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.InputStreamReader

object SQLCommunication {
    fun getQuestions(fName: String) : List<Question>{
        val list = mutableListOf<Question>()

        val reader = BufferedReader(InputStreamReader(GeoMania.appContext!!.assets.open(fName)))

        var line = reader.readLine()
        var contents: List<String>
        while(line != null){
            contents = line.split(",")

            try {
                list.add(
                    when(contents[0].toInt()) {
                        1 -> MChoiceQuestion(
                            contents[1],
                            listOf(contents[2], contents[3], contents[4], contents[5]),
                            contents[6].toInt(),
                            LatLng(contents[7].toDouble(), contents[8].toDouble()),
                            contents[9].toFloat(),
                            when (contents[10]) {
                                "null" -> null
                                else -> LatLng(contents[10].toDouble(), contents[11].toDouble())
                            }
                        )
                        2 -> SpellingQuestion(
                            contents[1],
                            contents[2],
                            LatLng(contents[3].toDouble(), contents[4].toDouble()),
                            contents[5].toFloat(),
                            when (contents[6]) {
                                "null" -> null
                                else -> LatLng(contents[6].toDouble(), contents[7].toDouble())
                            }
                        )
                        else -> TODO()
                    }
                )
            }
            catch (_: Exception){

            }

            line = reader.readLine()
        }

        list.shuffle()

        return list
    }
}