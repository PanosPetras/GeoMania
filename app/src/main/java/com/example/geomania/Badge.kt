package com.example.geomania

import java.io.Serializable

open class Badge(val name: String, val id: Int, val requirements: List<String>) {
    companion object {
        @Transient
        var badges = mapOf<Int, Badge>(Pair(GreeceBadge.id, GreeceBadge))
    }
}

object GreeceBadge : Badge("Greece Badge", R.id.badge_GR_IV, listOf("SpellingGR", "MChoiceGR"))