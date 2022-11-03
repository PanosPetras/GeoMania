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

    private var pLevel = 1
    val level: Int
        get() {
            return pLevel
        }

    private var pExperience = 0
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

    private var pCoins = 10
    var onCoinsChanged: (() -> Unit)? = null
    var coins: Int
        get() {
            return pCoins
        }
        set(value) {
            pCoins = value
            onCoinsChanged?.invoke()
            saveUserInfo()
        }

    private var pIcon = R.drawable.uic_earth
    const val iconPrice = 5
    var icon: Int
        get() {
            return pIcon
        }
        set(value) {
            pIcon = value
            saveUserInfo()
        }

    private var pAvailableIcons = Array(15) {i -> i == R.drawable.uic_earth - R.drawable.uic_big_ben}
    var availableIcons: Array<Boolean>
        get() {
            return pAvailableIcons
        }
        set(value) {
            pAvailableIcons = value
            saveUserInfo()
        }

    fun isIconAvailable(icon: Int) : Boolean{
        return pAvailableIcons[icon - R.drawable.uic_big_ben]
    }

    private val pMilestones: MutableList<String> = mutableListOf()
    private fun checkIfBadgeIsCompleted(){
        var flag: Boolean
        Badge.badges.forEach{ badge ->
            flag = true

            badge.requirements.forEach { req ->
                if(!pMilestones.contains(req)){
                    flag = false
                }
            }

            if(flag){
                pBadges.add(badge)
            }
        }
    }

    private var pBadges: MutableList<Badge> = mutableListOf()
    val badges: MutableList<Badge>
        get() {
            return pBadges
        }

    init{
        fileDir = GeoMania.appContext?.filesDir
        loadUserInfo()
    }

    fun rewardPlayer(score: Int, totalQuestions: Int, milestone: String?){
        if(score > totalQuestions * 0.7) {
            pCoins++

            if(score == totalQuestions){
                pCoins++

                milestone?.let {
                    if(!pMilestones.contains(it)) {
                        pMilestones.add(it)
                        checkIfBadgeIsCompleted()
                    }
                }
            }
        }

        experience += score + (score * 2 * (11 - level) / 10.0).toInt()
    }

    //Save/Load user data from memory
    private fun saveUserInfo(){
        if(fileDir == null) return

        var file = File(fileDir, "User Info.inf")
        var content = "$username,$level,$experience,$coins,$icon,${pAvailableIcons.joinToString(",")}"

        FileIO.storeString(file, content)

        file = File(fileDir, "Badge Info.inf")
        content = pMilestones.joinToString(",")

        FileIO.storeString(file, content)
    }

    private fun loadUserInfo(){
        if(fileDir == null) return

        val file = File(fileDir, "User Info.inf")
        val fileData = FileIO.loadString(file) ?: return

        val content = fileData.split(",")

        if(content.size == 20){
            pUsername = content[0]
            pLevel = content[1].toInt()
            pExperience = content[2].toInt()
            pCoins = content[3].toInt()
            pIcon = content[4].toInt()
            for(i in 5 until 20){
                pAvailableIcons[i - 5] = content[i].toBoolean()
            }
        }
    }
}