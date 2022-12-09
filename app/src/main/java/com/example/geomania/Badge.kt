package com.example.geomania

open class Badge(val name: String, val id: Int, val requirements: List<String>) {
    companion object {
        var badges = listOf(GreeceBadge, WorldBadge)
    }
}

object GreeceBadge : Badge("Greece Badge", R.id.badge_GR_IV, listOf("GRAchievement1", "GRAchievement2", "GRAchievement3", "GRAchievement4", "GRAchievement5", "GRAchievement6", "GRAchievement7"))

object WorldBadge : Badge("World Badge", R.id.badge_World_IV, listOf("WorldAchievement"))