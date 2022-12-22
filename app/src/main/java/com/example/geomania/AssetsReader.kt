package com.example.geomania

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object AssetsReader {
    fun getQuestions(fName: String): List<Question>{
        //Create an empty list
        val list = mutableListOf<Question>()

        //Fetch the contents of the given file
        val reader = BufferedReader(InputStreamReader(GeoMania.appContext!!.assets.open("$fName/questions.csv")))

        var line = reader.readLine()
        var contents: List<String>

        //While the file has lines to read
        while(line != null){
            contents = line.split(",")

            try {
                //Try to parse the current line as a Question object
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
                        else -> break
                    }
                )
            }
            catch (_: Exception){
                //The line couldn't be parsed, just skip it
            }

            //Get the next line of the file
            line = reader.readLine()
        }

        //Shuffle the list so that the questions are on a random order
        list.shuffle()

        return list
    }

    fun getMilestone(fName: String): String?{
        return try {
            //Fetch the contents of the given file
            val reader =
                BufferedReader(InputStreamReader(GeoMania.appContext!!.assets.open("$fName/reward.txt")))

            val line = reader.readText()
            if (line != "") {
                line
            } else {
                //If the file was empty, return null
                null
            }
        } catch (e: Exception){
            //If the file couldn't read, return null
            null
        }
    }

    fun getDirectoryContents(dir: String?): MutableList<String>{
        var reader: BufferedReader? = null
        val contents = mutableListOf<String>()

        //Create the to the content file of the directory
        val fName = if(dir == null) "Content/content.csv" else "Content/$dir/content.csv"

        try {
            //Fetch the contents of the given directory from the content.csv file
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
        } catch (_: IOException) {
            //Couldn't read the file, will return an empty list
        } finally {
            //Close the reader
            if (reader != null) {
                try {
                    reader.close()
                } catch (_: IOException) {
                }
            }
        }

        return contents
    }
}