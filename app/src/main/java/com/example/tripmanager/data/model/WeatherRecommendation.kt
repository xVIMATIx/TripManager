package com.example.tripmanager.data.model

data class WeatherRecommendation(
    val title: String,
    val description: String,
    val icon: String,
    val type: RecommendationType
)

enum class RecommendationType {
    CLOTHING,
    ACCESSORIES,
    EQUIPMENT,
    HEALTH,
    GENERAL
}

