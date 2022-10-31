package com.example.geomania

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object AssetsReader {
    fun getQuestions(fName: String): List<Question>{
        val list = mutableListOf<Question>()

        val reader = BufferedReader(InputStreamReader(GeoMania.appContext!!.assets.open("$fName/questions.csv")))

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

    fun getMilestone(fName: String): String?{
        return try {
            val reader =
                BufferedReader(InputStreamReader(GeoMania.appContext!!.assets.open("$fName/reward.txt")))

            val line = reader.readText()
            if (line != "") {
                line
            } else {
                null
            }
        } catch (e: Exception){
            null
        }
    }

    fun getDirectoryContents(dir: String?): MutableList<String>{
        var reader: BufferedReader? = null
        val contents = mutableListOf<String>()

        val fName = if(dir == null) "Content/content.csv" else "Content/$dir/content.csv"

        try {
            reader = BufferedReader(
                InputStreamReader(GeoMania.appContext!!.assets.open(fName))
            )

            val mLine: String = reader.readText()

            mLine.let {
                val lineContents = mLine.split(",")

                lineContents.forEach{
                    contents.add(it)
                }
            }
        } catch (e: IOException) {
            //Handle it
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    //log the exception
                }
            }
        }

        return contents
    }
}