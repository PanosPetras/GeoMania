package com.example.geomania

import java.io.File

object FileIO {
    fun storeString(file: File, content: String){
        if(!file.exists()) return

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