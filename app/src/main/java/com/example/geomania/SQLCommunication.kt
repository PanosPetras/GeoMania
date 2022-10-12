package com.example.geomania

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.InputStreamReader

object SQLCommunication {
    fun getQuestions(number: Int = 10) : List<Question>{
        val list = mutableListOf<Question>()

        val res = GeoMania.appContext!!.resources
        val inS = res.openRawResource(R.raw.questions)
        val reader = BufferedReader(InputStreamReader(inS))

        var line = reader.readLine()
        var contents: List<String>
        while(line != null){
            contents = line.split(",")

            try {
                list.add(
                    Question(
                        QuestionType.values()[contents[0].toInt() - 1],
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
                )
            }
            catch (exception: Exception){

            }

            line = reader.readLine()
        }

        list.shuffle()

        return list
    }
}