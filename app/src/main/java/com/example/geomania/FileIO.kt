package com.example.geomania

import java.io.File

object FileIO {
    //A couple of functions to make the reading/writing of strings to files a bit easier
    fun storeString(file: File, content: String){
        if(!file.exists()) {
            file.createNewFile()
        }

        file.writeText(content)
    }

    fun loadString(file: File): String?{
        if(!file.exists()) return null

        return try {
            file.readText()
        } catch (_: Exception){
            null
        }
    }
}