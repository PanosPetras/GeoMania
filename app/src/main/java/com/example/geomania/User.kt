package com.example.geomania

import java.io.File

object User {
    private var fileDir: File? = null

    private var pUsername = "Guest"
    var username: String
        get() {
            return pUsername
        }
        set(value) {
            pUsername = value
            saveUserInfo()
        }

    private var pLevel: Int = 1
    val level: Int
        get() {
            return pLevel
        }

    private var pExperience: Int = 0
    var experience: Int
        get() {
            return pExperience
        }
        set(value) {
            if(level == 10) return
            if(value < pExperience) return

            pExperience = value

            if(pExperience > 100) {
                pExperience -= 100
                pLevel += 1

                if(pLevel == 10){
                    pExperience = 10
                }
            }

            saveUserInfo()
        }

    private var pHints: Int = 10
    var hints: Int
        get() {
            return pHints
        }
        set(value) {
            pHints = value
            saveUserInfo()
        }

    private var pIcon: Int = R.drawable.uic_earth
    var icon: Int
        get() {
            return pIcon
        }
        set(value) {
            pIcon = value
            saveUserInfo()
        }

    init{
        fileDir = GeoMania.appContext?.filesDir
        loadUserInfo()
    }

    fun rewardPlayer(score: Int, totalQuestions: Int){
        experience += score + (score * 2 * (11 - level) / 10.0).toInt()
        if(score > totalQuestions * 0.7) {
            hints++
            if(score == totalQuestions){
                hints++
            }
        }
    }

    //Save/Load user data from memory
    private fun saveUserInfo(){
        if(fileDir == null) return

        val file = File(fileDir, "User Info.txt")

        if(!file.exists()){
            file.createNewFile()
        }

        file.writeText("$username,$level,$experience,$hints,$icon")
    }

    private fun loadUserInfo(){
        if(fileDir == null) return

        val file = File(fileDir, "User Info.txt")

        if(file.exists()){
            val content = file.readText().split(",")

            if(content.size == 5){
                pUsername = content[0]
                pLevel = content[1].toInt()
                pExperience = content[2].toInt()
                pHints = content[3].toInt()
                pIcon = content[4].toInt()
            }
        }
    }
}