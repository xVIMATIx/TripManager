package com.example.tripmanager.data.model

data class ClimateInfo(
    val country: String,
    val city: String? = null,
    val region: String? = null,
    val climateType: ClimateType,
    val seasons: SeasonInfo
)

data class SeasonInfo(
    val winter: SeasonClimate, // Dic-Feb
    val spring: SeasonClimate, // Mar-May
    val summer: SeasonClimate, // Jun-Ago
    val autumn: SeasonClimate  // Sep-Nov
)

data class SeasonClimate(
    val avgTemp: Int, // temperatura promedio en °C
    val humidity: HumidityLevel,
    val precipitation: PrecipitationLevel,
    val description: String
)

enum class ClimateType {
    TROPICAL,      // Clima tropical (playas, selvas)
    TEMPERATE,     // Clima templado (mediterráneo, continental)
    COLD,          // Clima frío (polar, subpolar)
    DESERT,        // Clima desértico
    MOUNTAIN,      // Clima de montaña
    CONTINENTAL    // Clima continental
}

enum class HumidityLevel {
    LOW,      // <40%
    MEDIUM,   // 40-70%
    HIGH      // >70%
}

enum class PrecipitationLevel {
    LOW,      // Poca lluvia
    MEDIUM,   // Lluvia moderada
    HIGH      // Mucha lluvia
}

object ClimateDatabase {
    private val climateData = mapOf(
        // México
        "mexico" to ClimateInfo(
            country = "México",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(22, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Templado y seco"),
                spring = SeasonClimate(26, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Cálido y agradable"),
                summer = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Caliente y lluvioso"),
                autumn = SeasonClimate(25, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado con lluvias")
            )
        ),
        "cancun" to ClimateInfo(
            country = "México",
            city = "Cancún",
            region = "Quintana Roo",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(26, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Perfecto para playa, cálido y seco"),
                spring = SeasonClimate(28, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Muy cálido, ideal para playa"),
                summer = SeasonClimate(30, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente y húmedo, temporada de lluvias"),
                autumn = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido con posibilidad de huracanes")
            )
        ),
        "ciudad de mexico" to ClimateInfo(
            country = "México",
            city = "Ciudad de México",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(18, HumidityLevel.LOW, PrecipitationLevel.LOW, "Frío y seco, necesita abrigo"),
                spring = SeasonClimate(22, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Templado y agradable"),
                summer = SeasonClimate(24, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Lluvioso y fresco"),
                autumn = SeasonClimate(20, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado con lluvias")
            )
        ),
        // Estados Unidos
        "estados unidos" to ClimateInfo(
            country = "Estados Unidos",
            climateType = ClimateType.CONTINENTAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(5, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Frío, necesita abrigo pesado"),
                spring = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y variable"),
                summer = SeasonClimate(28, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Cálido a muy caliente"),
                autumn = SeasonClimate(15, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Fresco y colorido")
            )
        ),
        "nueva york" to ClimateInfo(
            country = "Estados Unidos",
            city = "Nueva York",
            climateType = ClimateType.CONTINENTAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(2, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Muy frío, nieve frecuente"),
                spring = SeasonClimate(15, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Fresco y variable"),
                summer = SeasonClimate(27, HumidityLevel.HIGH, PrecipitationLevel.MEDIUM, "Caliente y húmedo"),
                autumn = SeasonClimate(14, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Fresco y agradable")
            )
        ),
        "los angeles" to ClimateInfo(
            country = "Estados Unidos",
            city = "Los Ángeles",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(18, HumidityLevel.LOW, PrecipitationLevel.LOW, "Suave y seco, clima mediterráneo"),
                spring = SeasonClimate(21, HumidityLevel.LOW, PrecipitationLevel.LOW, "Perfecto, soleado y templado"),
                summer = SeasonClimate(25, HumidityLevel.LOW, PrecipitationLevel.LOW, "Caliente y seco, perfecto para playa"),
                autumn = SeasonClimate(22, HumidityLevel.LOW, PrecipitationLevel.LOW, "Cálido y agradable")
            )
        ),
        "miami" to ClimateInfo(
            country = "Estados Unidos",
            city = "Miami",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(24, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Cálido y seco, perfecto"),
                spring = SeasonClimate(27, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Muy cálido y húmedo"),
                summer = SeasonClimate(30, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente, húmedo y lluvioso"),
                autumn = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido con alta humedad")
            )
        ),
        // España
        "espana" to ClimateInfo(
            country = "España",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(12, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Frío y variable"),
                spring = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable"),
                summer = SeasonClimate(26, HumidityLevel.LOW, PrecipitationLevel.LOW, "Caliente y seco"),
                autumn = SeasonClimate(16, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado con lluvias")
            )
        ),
        "madrid" to ClimateInfo(
            country = "España",
            city = "Madrid",
            climateType = ClimateType.CONTINENTAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(8, HumidityLevel.LOW, PrecipitationLevel.LOW, "Frío y seco"),
                spring = SeasonClimate(16, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y soleado"),
                summer = SeasonClimate(28, HumidityLevel.LOW, PrecipitationLevel.LOW, "Muy caliente y seco"),
                autumn = SeasonClimate(14, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Fresco y variable")
            )
        ),
        "barcelona" to ClimateInfo(
            country = "España",
            city = "Barcelona",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(14, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Suave, clima mediterráneo"),
                spring = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable"),
                summer = SeasonClimate(26, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Caliente y húmedo"),
                autumn = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado con lluvias")
            )
        ),
        // Países fríos
        "canada" to ClimateInfo(
            country = "Canadá",
            climateType = ClimateType.COLD,
            seasons = SeasonInfo(
                winter = SeasonClimate(-10, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Muy frío, nieve abundante"),
                spring = SeasonClimate(8, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Fresco y variable"),
                summer = SeasonClimate(22, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable"),
                autumn = SeasonClimate(8, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Frío y colorido")
            )
        ),
        "alaska" to ClimateInfo(
            country = "Estados Unidos",
            city = "Alaska",
            climateType = ClimateType.COLD,
            seasons = SeasonInfo(
                winter = SeasonClimate(-15, HumidityLevel.LOW, PrecipitationLevel.LOW, "Extremadamente frío"),
                spring = SeasonClimate(5, HumidityLevel.LOW, PrecipitationLevel.LOW, "Muy frío"),
                summer = SeasonClimate(15, HumidityLevel.LOW, PrecipitationLevel.LOW, "Fresco, días muy largos"),
                autumn = SeasonClimate(2, HumidityLevel.LOW, PrecipitationLevel.LOW, "Muy frío")
            )
        ),
        // Países de montaña
        "suiza" to ClimateInfo(
            country = "Suiza",
            climateType = ClimateType.MOUNTAIN,
            seasons = SeasonInfo(
                winter = SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.HIGH, "Muy frío, nieve abundante"),
                spring = SeasonClimate(10, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Fresco y variable"),
                summer = SeasonClimate(20, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable"),
                autumn = SeasonClimate(8, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Frío con nieve en altura")
            )
        ),
        "nepal" to ClimateInfo(
            country = "Nepal",
            climateType = ClimateType.MOUNTAIN,
            seasons = SeasonInfo(
                winter = SeasonClimate(10, HumidityLevel.LOW, PrecipitationLevel.LOW, "Frío y seco, ideal para trekking"),
                spring = SeasonClimate(18, HumidityLevel.LOW, PrecipitationLevel.LOW, "Templado, perfecto para montaña"),
                summer = SeasonClimate(25, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Lluvioso, temporada de monzón"),
                autumn = SeasonClimate(16, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Templado y seco, mejor temporada")
            )
        ),
        // América del Sur
        "argentina" to ClimateInfo(
            country = "Argentina",
            climateType = ClimateType.CONTINENTAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(12, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Fresco en el norte, frío en el sur"),
                spring = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable"),
                summer = SeasonClimate(26, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Cálido"),
                autumn = SeasonClimate(15, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Fresco y colorido")
            )
        ),
        "buenos aires" to ClimateInfo(
            country = "Argentina",
            city = "Buenos Aires",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(13, HumidityLevel.HIGH, PrecipitationLevel.MEDIUM, "Frío y húmedo"),
                spring = SeasonClimate(19, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Templado y lluvioso"),
                summer = SeasonClimate(26, HumidityLevel.HIGH, PrecipitationLevel.MEDIUM, "Caliente y húmedo"),
                autumn = SeasonClimate(18, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Templado con lluvias")
            )
        ),
        "brasil" to ClimateInfo(
            country = "Brasil",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(22, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Templado y seco"),
                spring = SeasonClimate(25, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Cálido"),
                summer = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente y lluvioso"),
                autumn = SeasonClimate(24, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido y húmedo")
            )
        ),
        "rio de janeiro" to ClimateInfo(
            country = "Brasil",
            city = "Río de Janeiro",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(23, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Cálido y seco, perfecto"),
                spring = SeasonClimate(25, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Muy cálido"),
                summer = SeasonClimate(29, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente, húmedo y lluvioso"),
                autumn = SeasonClimate(26, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido con alta humedad")
            )
        ),
        // Asia
        "japon" to ClimateInfo(
            country = "Japón",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(8, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Frío, nieve en el norte"),
                spring = SeasonClimate(16, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado, época de cerezos"),
                summer = SeasonClimate(27, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Caliente, húmedo y lluvioso"),
                autumn = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y colorido")
            )
        ),
        "tokio" to ClimateInfo(
            country = "Japón",
            city = "Tokio",
            climateType = ClimateType.TEMPERATE,
            seasons = SeasonInfo(
                winter = SeasonClimate(10, HumidityLevel.LOW, PrecipitationLevel.LOW, "Frío y seco"),
                spring = SeasonClimate(18, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado, época de cerezos"),
                summer = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente, húmedo y lluvioso"),
                autumn = SeasonClimate(20, HumidityLevel.MEDIUM, PrecipitationLevel.MEDIUM, "Templado y agradable")
            )
        ),
        // Países con playa tropical
        "republica dominicana" to ClimateInfo(
            country = "República Dominicana",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(27, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Perfecto para playa"),
                spring = SeasonClimate(28, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Muy cálido"),
                summer = SeasonClimate(30, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente y lluvioso"),
                autumn = SeasonClimate(29, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido, temporada de huracanes")
            )
        ),
        "cuba" to ClimateInfo(
            country = "Cuba",
            climateType = ClimateType.TROPICAL,
            seasons = SeasonInfo(
                winter = SeasonClimate(26, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Cálido y seco"),
                spring = SeasonClimate(28, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "Muy cálido"),
                summer = SeasonClimate(30, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Muy caliente y lluvioso"),
                autumn = SeasonClimate(28, HumidityLevel.HIGH, PrecipitationLevel.HIGH, "Cálido, temporada de huracanes")
            )
        )
    )
    
    fun getClimateInfo(destination: String): ClimateInfo? {
        val destinationLower = destination.lowercase().trim()
        
        // Buscar por ciudad específica primero
        climateData.forEach { (key, info) ->
            if (destinationLower.contains(key, ignoreCase = true)) {
                return info
            }
        }
        
        // Si no se encuentra, buscar por país genérico
        return climateData["mexico"] // fallback
    }
    
    fun detectClimateType(destination: String): ClimateType {
        val destinationLower = destination.lowercase()
        val climateInfo = getClimateInfo(destination)
        
        if (climateInfo != null) {
            return climateInfo.climateType
        }
        
        // Detección por palabras clave
        return when {
            destinationLower.contains("playa") || destinationLower.contains("cancun") || 
            destinationLower.contains("caribe") || destinationLower.contains("tropical") -> ClimateType.TROPICAL
            destinationLower.contains("montaña") || destinationLower.contains("montana") || 
            destinationLower.contains("sierra") || destinationLower.contains("snow") -> ClimateType.MOUNTAIN
            destinationLower.contains("canada") || destinationLower.contains("alaska") || 
            destinationLower.contains("norte") && destinationLower.contains("europa") -> ClimateType.COLD
            destinationLower.contains("desierto") || destinationLower.contains("dubai") -> ClimateType.DESERT
            else -> ClimateType.TEMPERATE
        }
    }
    
    fun getSeason(month: Int): SeasonClimate {
        val season = when (month) {
            in 12..2 -> "winter"
            in 3..5 -> "spring"
            in 6..8 -> "summer"
            in 9..11 -> "autumn"
            else -> "spring"
        }
        return when (season) {
            "winter" -> SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "")
            "spring" -> SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "")
            "summer" -> SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "")
            "autumn" -> SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "")
            else -> SeasonClimate(0, HumidityLevel.MEDIUM, PrecipitationLevel.LOW, "")
        }
    }
}
