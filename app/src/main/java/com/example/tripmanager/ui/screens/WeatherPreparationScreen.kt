package com.example.tripmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripmanager.data.model.ClimateDatabase
import com.example.tripmanager.data.model.ClimateType
import com.example.tripmanager.data.model.WeatherRecommendation
import com.example.tripmanager.data.model.RecommendationType
import java.text.SimpleDateFormat
import java.util.*
import com.example.tripmanager.util.datePickerMillisToLocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPreparationScreen(
    destination: String = "",
    tripDate: Long? = null,
    onNavigateBack: () -> Unit
) {
    var destinationText by remember { mutableStateOf(destination) }
    var selectedDate by remember { mutableStateOf(tripDate ?: System.currentTimeMillis()) }
    
    // Asegurar que la fecha inicial esté en la zona horaria local
    val initialDateMillis = remember(tripDate) {
        if (tripDate != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = tripDate
            // Establecer a mediodía para evitar problemas de zona horaria
            calendar.set(Calendar.HOUR_OF_DAY, 12)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.timeInMillis
        } else {
            System.currentTimeMillis()
        }
    }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )
    
    // Calcular recomendaciones basadas en destino y fecha (mejorado)
    val recommendations = remember(destinationText, selectedDate) {
        getWeatherRecommendations(destinationText, selectedDate)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Preparación Climática",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input de destino
            OutlinedTextField(
                value = destinationText,
                onValueChange = { destinationText = it },
                label = { Text("Destino") },
                placeholder = { Text("Ej: Cancún, México") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            // Selector de fecha
            OutlinedTextField(
                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(selectedDate)),
                onValueChange = {},
                readOnly = true,
                label = { Text("Fecha del viaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                    }
                }
            )
            
            // Recomendaciones
            if (destinationText.isNotBlank()) {
                recommendations.forEach { recommendation ->
                    WeatherRecommendationCard(recommendation)
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Text(
                        text = "Ingresa un destino para ver recomendaciones climáticas",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
    
    // Date Picker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            selectedDate = datePickerMillisToLocalDate(it).time
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// Moved to ClimateData.kt

@Composable
fun WeatherRecommendationCard(recommendation: WeatherRecommendation) {
    val cardColor = when (recommendation.type) {
        RecommendationType.CLOTHING -> Color(0xFFE3F2FD)
        RecommendationType.ACCESSORIES -> Color(0xFFF3E5F5)
        RecommendationType.EQUIPMENT -> Color(0xFFE8F5E9)
        RecommendationType.HEALTH -> Color(0xFFFFEBEE)
        RecommendationType.GENERAL -> Color(0xFFFFF3E0)
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = recommendation.icon,
                fontSize = 32.sp
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = recommendation.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = recommendation.description,
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

fun getWeatherRecommendations(destination: String, date: Long): List<WeatherRecommendation> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = date
    val month = calendar.get(Calendar.MONTH) + 1 // 1-12
    val destinationLower = destination.lowercase()
    
    val recommendations = mutableListOf<WeatherRecommendation>()
    
    // Obtener información climática de la base de datos
    val climateInfo = ClimateDatabase.getClimateInfo(destination)
    val climateType = ClimateDatabase.detectClimateType(destination)
    
    // Determinar estación
    val season = when (month) {
        in 12..2 -> "winter"
        in 3..5 -> "spring"
        in 6..8 -> "summer"
        in 9..11 -> "autumn"
        else -> "spring"
    }
    
    // Obtener datos climáticos de la temporada actual
    val seasonClimate = climateInfo?.let {
        when (season) {
            "winter" -> it.seasons.winter
            "spring" -> it.seasons.spring
            "summer" -> it.seasons.summer
            "autumn" -> it.seasons.autumn
            else -> it.seasons.spring
        }
    }
    
    // Detección mejorada de tipo de destino
    val isBeach = destinationLower.contains("playa") || destinationLower.contains("cancun") || 
                   destinationLower.contains("puerto") || destinationLower.contains("caribe") ||
                   destinationLower.contains("tulum") || destinationLower.contains("riviera maya") ||
                   climateType == ClimateType.TROPICAL
    val isMountain = destinationLower.contains("montaña") || destinationLower.contains("montana") || 
                     destinationLower.contains("sierra") || destinationLower.contains("alp") ||
                     climateType == ClimateType.MOUNTAIN
    val isTropical = destinationLower.contains("tropical") || destinationLower.contains("mexico") || 
                     destinationLower.contains("cancun") || destinationLower.contains("cuba") ||
                     destinationLower.contains("republica dominicana") || 
                     climateType == ClimateType.TROPICAL
    val isCold = destinationLower.contains("norte") || destinationLower.contains("canada") || 
                 destinationLower.contains("alaska") || destinationLower.contains("rusia") ||
                 climateType == ClimateType.COLD
    val isArid = destinationLower.contains("desierto") || destinationLower.contains("las vegas") ||
                 destinationLower.contains("phoenix") || destinationLower.contains("dubai") ||
                 climateType == ClimateType.DESERT
    
    // Agregar recomendaciones basadas en datos climáticos si están disponibles
    if (seasonClimate != null && climateInfo != null) {
        recommendations.add(
            WeatherRecommendation(
                "Clima ${getSeasonName(season)}",
                "${seasonClimate.description}. Temperatura promedio: ${seasonClimate.avgTemp}°C. ${getHumidityText(seasonClimate.humidity)}. ${getPrecipitationText(seasonClimate.precipitation)}.",
                getSeasonEmoji(season),
                RecommendationType.GENERAL
            )
        )
    }
    
    // Recomendaciones por tipo y temporada
    when {
        isBeach || isTropical -> {
            recommendations.addAll(listOf(
                WeatherRecommendation(
                    "Ropa Ligera",
                    "Trajes de baño, shorts, camisetas ligeras y vestidos. Ropa de algodón que seque rápido. Colores claros reflejan el sol.",
                    "👕",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Protección Solar",
                    "Bloqueador solar SPF 50+, gafas de sol con protección UV, gorra o sombrero de ala ancha. Protección UV es esencial.",
                    "☀️",
                    RecommendationType.ACCESSORIES
                ),
                WeatherRecommendation(
                    "Calzado Cómodo",
                    "Sandalias resistentes al agua, zapatos cerrados para caminar y posiblemente zapatos de playa. Evita zapatos que puedan dañarse con arena/sal.",
                    "👟",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Hidratación",
                    "Mantente hidratado. Lleva una botella de agua reutilizable y bebe agua regularmente. El calor y la humedad aumentan la necesidad de hidratación.",
                    "💧",
                    RecommendationType.HEALTH
                )
            ))
            
            // Temporada de lluvia para playas tropicales
            if (month in 6..9 || seasonClimate?.precipitation == com.example.tripmanager.data.model.PrecipitationLevel.HIGH) {
                recommendations.add(
                    WeatherRecommendation(
                        "Temporada de Lluvia/Huracanes",
                        "Puede haber lluvias tropicales intensas. Considera un impermeable ligero o paraguas compacto. En septiembre-octubre, monitorea alertas de huracanes.",
                        "🌧️",
                        RecommendationType.EQUIPMENT
                    )
                )
            }
            
            // Si es muy caliente (>28°C promedio)
            if (seasonClimate?.avgTemp ?: 0 > 28) {
                recommendations.add(
                    WeatherRecommendation(
                        "Protección contra Calor Extremo",
                        "Evita exposición directa al sol entre 11am-3pm. Busca sombra, usa ropa holgada y protección completa. Considera actividades temprano o tarde.",
                        "🔥",
                        RecommendationType.HEALTH
                    )
                )
            }
        }
        
        isMountain -> {
            recommendations.addAll(listOf(
                WeatherRecommendation(
                    "Ropa en Capas (Sistema de 3 Capas)",
                    "1) Capa base: camiseta térmica (merino o sintético). 2) Capa intermedia: polar o pluma ligera. 3) Capa exterior: chaqueta impermeable y cortavientos. Permite ajustar según temperatura y esfuerzo.",
                    "🧥",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Calzado Especializado",
                    "Botas de montaña con buen agarre (suela Vibram o similar), impermeables (Gore-Tex), con soporte de tobillo. Prueba las botas antes del viaje para evitar ampollas.",
                    "🥾",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Equipamiento Esencial",
                    "Mochila de trekking (20-30L para día, 40-60L para varios días), bastones de trekking, linterna frontal, mapa/brújula o GPS, silbato de emergencia.",
                    "🎒",
                    RecommendationType.EQUIPMENT
                ),
                WeatherRecommendation(
                    "Protección UV",
                    "En montaña el sol es más intenso (+10% cada 1000m altitud). Gorra, guantes, bufanda, gafas de sol categoría 3-4 con protección UV, bloqueador labial SPF.",
                    "🧤",
                    RecommendationType.ACCESSORIES
                )
            ))
            
            // Invierno en montaña
            if (month in 12..2 || season == "winter" || seasonClimate?.avgTemp ?: 0 < 5) {
                recommendations.addAll(listOf(
                    WeatherRecommendation(
                        "Equipo para Frío y Nieve",
                        "Ropa térmica adicional, chaqueta de plumas o sintética, pantalones de nieve/impermeables, guantes gruesos, calcetines térmicos.",
                        "🧊",
                        RecommendationType.CLOTHING
                    ),
                    WeatherRecommendation(
                        "Calzado para Nieve/Hielo",
                        "Botas con aislamiento térmico, posiblemente crampones si hay hielo, cubre-botas impermeables.",
                        "⛸️",
                        RecommendationType.EQUIPMENT
                    )
                ))
            }
            
            // Precauciones de montaña
            recommendations.add(
                WeatherRecommendation(
                    "Seguridad en Montaña",
                    "Informa a alguien de tu ruta y horarios. Lleva botiquín de primeros auxilios, agua extra y comida de emergencia. Acércate gradualmente a la altitud para evitar mal de montaña.",
                    "⚠️",
                    RecommendationType.HEALTH
                )
            )
        }
        
        isCold || season == "winter" || seasonClimate?.avgTemp ?: 20 < 10 -> {
            recommendations.addAll(listOf(
                WeatherRecommendation(
                    "Abrigo Cálido en Capas",
                    "Sistema de capas: ropa térmica base, suéter/polar intermedio, abrigo exterior (plumón o sintético) resistente al viento. Gorros, guantes, bufanda y calcetines térmicos.",
                    "🧥",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Calzado para Frío",
                    "Botas impermeables con buen aislamiento térmico (Thinsulate o similar), agarre en hielo (suela con buen diseño), que no aprieten demasiado (necesitas circulación).",
                    "👢",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Protección de Piel",
                    "Crema hidratante rica para cara y manos, bálsamo labial con SPF, protector facial si hace mucho frío. El viento y frío resecan y agrietan la piel.",
                    "🧴",
                    RecommendationType.HEALTH
                ),
                WeatherRecommendation(
                    "Protección de Extremidades",
                    "Guantes, calcetines térmicos (evita algodón, usa lana o sintéticos), calentadores de manos/pies si es extremo. Las extremidades se enfrían primero.",
                    "🧤",
                    RecommendationType.ACCESSORIES
                )
            ))
            
            // Si es muy frío (<0°C)
            if (seasonClimate?.avgTemp ?: 0 < 0) {
                recommendations.add(
                    WeatherRecommendation(
                        "Prevención de Hipotermia",
                        "Evita sudar en exceso (ajusta capas), mantén seco (ropa impermeable), come regularmente para generar calor. Reconoce signos de hipotermia.",
                        "❄️",
                        RecommendationType.HEALTH
                    )
                )
            }
        }
        
        isArid -> {
            recommendations.addAll(listOf(
                WeatherRecommendation(
                    "Ropa Ligera y Protección Solar",
                    "Ropa de colores claros, manga larga para protegerse del sol. Sombrero de ala ancha y gafas de sol.",
                    "☀️",
                    RecommendationType.CLOTHING
                ),
                WeatherRecommendation(
                    "Hidratación Extrema",
                    "Bebe mucha agua constantemente. Evita actividad física intensa en las horas más calientes del día (10am-4pm).",
                    "💧",
                    RecommendationType.HEALTH
                ),
                WeatherRecommendation(
                    "Protección para la Piel",
                    "Bloqueador solar SPF 50+, labial con protección solar, crema hidratante. La piel se seca mucho en clima árido.",
                    "🧴",
                    RecommendationType.HEALTH
                )
            ))
        }
        
        else -> {
            // Recomendaciones generales por temporada y datos climáticos
            if (seasonClimate != null) {
                val temp = seasonClimate.avgTemp
                recommendations.add(
                    WeatherRecommendation(
                        "Ropa Versátil",
                        "Temperatura promedio: ${temp}°C. Ropa en capas que puedas adaptar según el clima del día. Lleva opciones para calor y frío.",
                        "👔",
                        RecommendationType.CLOTHING
                    )
                )
                
                when {
                    temp > 25 -> {
                        recommendations.add(
                            WeatherRecommendation(
                                "Ropa Fresca",
                                "Ropa ligera de colores claros, tejidos transpirables (algodón, lino), sombrero y protección solar. Evita ropa sintética que no respire.",
                                "🌞",
                                RecommendationType.CLOTHING
                            )
                        )
                    }
                    temp < 15 -> {
                        recommendations.add(
                            WeatherRecommendation(
                                "Abrigo Moderado",
                                "Sweater o chaqueta ligera, ropa de manga larga, posiblemente guantes ligeros. Capas que puedas agregar según el día.",
                                "🧣",
                                RecommendationType.CLOTHING
                            )
                        )
                    }
                }
            } else {
                // Fallback sin datos climáticos
                recommendations.add(
                    WeatherRecommendation(
                        "Ropa Versátil",
                        "Ropa en capas que puedas adaptar según el clima del día. Lleva opciones para calor y frío.",
                        "👔",
                        RecommendationType.CLOTHING
                    )
                )
                
                when (season) {
                    "summer" -> {
                        recommendations.add(
                            WeatherRecommendation(
                                "Ropa Fresca",
                                "Ropa ligera de colores claros, sombrero y protección solar.",
                                "🌞",
                                RecommendationType.CLOTHING
                            )
                        )
                    }
                    "winter" -> {
                        recommendations.add(
                            WeatherRecommendation(
                                "Ropa de Abrigo",
                                "Abrigo cálido, gorros, guantes y bufanda. Capas térmicas si hace mucho frío.",
                                "🧥",
                                RecommendationType.CLOTHING
                            )
                        )
                    }
                    "spring", "autumn" -> {
                        recommendations.add(
                            WeatherRecommendation(
                                "Ropa de Temporada",
                                "Ropa intermedia con capas. Puede haber cambios bruscos de temperatura.",
                                "👔",
                                RecommendationType.CLOTHING
                            )
                        )
                    }
                }
            }
        }
    }
    
    // Recomendaciones universales
    recommendations.add(
        WeatherRecommendation(
            "Documentos",
            "Lleva copias de documentos importantes y seguro de viaje que cubra condiciones climáticas extremas.",
            "📄",
            RecommendationType.GENERAL
        )
    )
    
    return recommendations
}

fun getSeasonName(season: String): String {
    return when (season) {
        "winter" -> "Invierno"
        "spring" -> "Primavera"
        "summer" -> "Verano"
        "autumn" -> "Otoño"
        else -> "General"
    }
}

fun getSeasonEmoji(season: String): String {
    return when (season) {
        "winter" -> "❄️"
        "spring" -> "🌸"
        "summer" -> "☀️"
        "autumn" -> "🍂"
        else -> "🌤️"
    }
}

fun getHumidityText(humidity: com.example.tripmanager.data.model.HumidityLevel): String {
    return when (humidity) {
        com.example.tripmanager.data.model.HumidityLevel.LOW -> "Humedad baja (<40%)"
        com.example.tripmanager.data.model.HumidityLevel.MEDIUM -> "Humedad moderada (40-70%)"
        com.example.tripmanager.data.model.HumidityLevel.HIGH -> "Humedad alta (>70%)"
    }
}

fun getPrecipitationText(precipitation: com.example.tripmanager.data.model.PrecipitationLevel): String {
    return when (precipitation) {
        com.example.tripmanager.data.model.PrecipitationLevel.LOW -> "Poca probabilidad de lluvia"
        com.example.tripmanager.data.model.PrecipitationLevel.MEDIUM -> "Lluvia moderada posible"
        com.example.tripmanager.data.model.PrecipitationLevel.HIGH -> "Alta probabilidad de lluvia"
    }
}

