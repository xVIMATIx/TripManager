package com.example.tripmanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tripmanager.data.model.Countries
import com.example.tripmanager.data.model.Country
import com.example.tripmanager.data.model.City
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneCalculatorScreen(
    originCity: String = "",
    destinationCity: String = "",
    onNavigateBack: () -> Unit
) {
    // Estados para selección de país y ciudad
    var originCountrySelected by remember { mutableStateOf<Country?>(null) }
    var originCitySelected by remember { mutableStateOf<City?>(null) }
    var destinationCountrySelected by remember { mutableStateOf<Country?>(null) }
    var destinationCitySelected by remember { mutableStateOf<City?>(null) }
    
    var originCountryExpanded by remember { mutableStateOf(false) }
    var originCityExpanded by remember { mutableStateOf(false) }
    var destinationCountryExpanded by remember { mutableStateOf(false) }
    var destinationCityExpanded by remember { mutableStateOf(false) }
    
    var selectedTime by remember { mutableStateOf(System.currentTimeMillis()) }
    
    // Obtener ciudades basadas en país seleccionado
    val originCities = remember(originCountrySelected) {
        originCountrySelected?.cities ?: emptyList()
    }
    
    val destinationCities = remember(destinationCountrySelected) {
        destinationCountrySelected?.cities ?: emptyList()
    }
    
    // Obtener TimeZone basado en ciudad seleccionada
    val originTimeZone = remember(originCitySelected) {
        originCitySelected?.let { TimeZone.getTimeZone(it.timeZone) }
    }
    
    val destinationTimeZone = remember(destinationCitySelected) {
        destinationCitySelected?.let { TimeZone.getTimeZone(it.timeZone) }
    }
    
    // Calcular hora en destino basada en hora de origen
    val destinationTime = remember(selectedTime, originTimeZone, destinationTimeZone) {
        if (originTimeZone != null && destinationTimeZone != null) {
            convertTimeZone(selectedTime, originTimeZone, destinationTimeZone)
        } else null
    }
    
    // Calcular diferencia de horas
    val timeDifference = remember(originTimeZone, destinationTimeZone) {
        if (originTimeZone != null && destinationTimeZone != null) {
            calculateTimeDifference(originTimeZone, destinationTimeZone)
        } else null
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Calculadora de Zona Horaria",
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
            Text(
                text = "Calcula la diferencia horaria entre dos ciudades.",
                fontSize = 16.sp,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Origen
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Origen",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    // Selector de País Origen
                    Text(
                        text = "País",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    ExposedDropdownMenuBox(
                        expanded = originCountryExpanded,
                        onExpandedChange = { originCountryExpanded = !originCountryExpanded }
                    ) {
                        OutlinedTextField(
                            value = originCountrySelected?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar País") },
                            placeholder = { Text("Selecciona un país") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = originCountryExpanded)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = originCountryExpanded,
                            onDismissRequest = { originCountryExpanded = false }
                        ) {
                            Countries.COUNTRIES.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country.name) },
                                    onClick = {
                                        originCountrySelected = country
                                        originCitySelected = null // Resetear ciudad al cambiar país
                                        originCountryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Selector de Ciudad/Estado Origen
                    if (originCountrySelected != null && originCities.isNotEmpty()) {
                        Text(
                            text = "Ciudad/Estado",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                        ExposedDropdownMenuBox(
                            expanded = originCityExpanded,
                            onExpandedChange = { originCityExpanded = !originCityExpanded }
                        ) {
                            OutlinedTextField(
                                value = originCitySelected?.let { 
                                    if (it.state != null) "${it.name}, ${it.state}" else it.name 
                                } ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar Ciudad/Estado") },
                                placeholder = { Text("Selecciona una ciudad") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = originCityExpanded)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = originCityExpanded,
                                onDismissRequest = { originCityExpanded = false }
                            ) {
                                originCities.forEach { city ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    text = city.name,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                if (city.state != null) {
                                                    Text(
                                                        text = city.state,
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF666666)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            originCitySelected = city
                                            originCityExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Mostrar información de zona horaria y hora
                    if (originTimeZone != null) {
                        Text(
                            text = "Zona horaria: ${originTimeZone.id}",
                            fontSize = 12.sp,
                            color = Color(0xFF2196F3)
                        )
                        
                        val originTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                            timeZone = originTimeZone
                        }.format(Date(selectedTime))
                        
                        Text(
                            text = "Hora actual: $originTimeStr",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            
            // Botón de intercambio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        val tempCountry = originCountrySelected
                        val tempCity = originCitySelected
                        originCountrySelected = destinationCountrySelected
                        originCitySelected = destinationCitySelected
                        destinationCountrySelected = tempCountry
                        destinationCitySelected = tempCity
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Intercambiar",
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            
            // Destino
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Destino",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    // Selector de País Destino
                    Text(
                        text = "País",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    ExposedDropdownMenuBox(
                        expanded = destinationCountryExpanded,
                        onExpandedChange = { destinationCountryExpanded = !destinationCountryExpanded }
                    ) {
                        OutlinedTextField(
                            value = destinationCountrySelected?.name ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Seleccionar País") },
                            placeholder = { Text("Selecciona un país") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinationCountryExpanded)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = destinationCountryExpanded,
                            onDismissRequest = { destinationCountryExpanded = false }
                        ) {
                            Countries.COUNTRIES.forEach { country ->
                                DropdownMenuItem(
                                    text = { Text(country.name) },
                                    onClick = {
                                        destinationCountrySelected = country
                                        destinationCitySelected = null // Resetear ciudad al cambiar país
                                        destinationCountryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Selector de Ciudad/Estado Destino
                    if (destinationCountrySelected != null && destinationCities.isNotEmpty()) {
                        Text(
                            text = "Ciudad/Estado",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                        ExposedDropdownMenuBox(
                            expanded = destinationCityExpanded,
                            onExpandedChange = { destinationCityExpanded = !destinationCityExpanded }
                        ) {
                            OutlinedTextField(
                                value = destinationCitySelected?.let { 
                                    if (it.state != null) "${it.name}, ${it.state}" else it.name 
                                } ?: "",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Seleccionar Ciudad/Estado") },
                                placeholder = { Text("Selecciona una ciudad") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = destinationCityExpanded)
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = destinationCityExpanded,
                                onDismissRequest = { destinationCityExpanded = false }
                            ) {
                                destinationCities.forEach { city ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    text = city.name,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                if (city.state != null) {
                                                    Text(
                                                        text = city.state,
                                                        fontSize = 12.sp,
                                                        color = Color(0xFF666666)
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            destinationCitySelected = city
                                            destinationCityExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // Mostrar información de zona horaria y hora
                    if (destinationTimeZone != null && destinationTime != null) {
                        Text(
                            text = "Zona horaria: ${destinationTimeZone.id}",
                            fontSize = 12.sp,
                            color = Color(0xFF4CAF50)
                        )
                        
                        val destTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                            timeZone = destinationTimeZone
                        }.format(Date(destinationTime))
                        
                        Text(
                            text = "Hora en destino: $destTimeStr",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    } else if (destinationTimeZone != null) {
                        val destTimeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                            timeZone = destinationTimeZone
                        }.format(Date())
                        
                        Text(
                            text = "Hora actual: $destTimeStr",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            
            // Información de diferencia
            if (timeDifference != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Diferencia de Horario",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = if (timeDifference >= 0) {
                                "El destino está ${timeDifference} horas adelante del origen"
                            } else {
                                "El destino está ${abs(timeDifference)} horas atrás del origen"
                            },
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }
            
            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💡 Consejos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "• Ajusta tu reloj antes de viajar para evitar confusión",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "• Si la diferencia es >6 horas, considera jet lag",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = "• Duerme según el horario del destino unos días antes",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

fun convertTimeZone(
    timeMillis: Long,
    fromTimeZone: TimeZone,
    toTimeZone: TimeZone
): Long {
    val calendar = Calendar.getInstance()
    calendar.timeZone = fromTimeZone
    calendar.timeInMillis = timeMillis
    
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    
    val toCalendar = Calendar.getInstance()
    toCalendar.timeZone = toTimeZone
    toCalendar.set(Calendar.HOUR_OF_DAY, hour)
    toCalendar.set(Calendar.MINUTE, minute)
    toCalendar.set(Calendar.SECOND, 0)
    toCalendar.set(Calendar.MILLISECOND, 0)
    
    // Calcular la diferencia de offset
    val fromOffset = fromTimeZone.getOffset(timeMillis)
    val toOffset = toTimeZone.getOffset(timeMillis)
    val offsetDiff = toOffset - fromOffset
    
    return timeMillis + offsetDiff
}

fun calculateTimeDifference(timeZone1: TimeZone, timeZone2: TimeZone): Int {
    val now = System.currentTimeMillis()
    val offset1 = timeZone1.getOffset(now) / (1000 * 60 * 60) // Convertir a horas
    val offset2 = timeZone2.getOffset(now) / (1000 * 60 * 60)
    return (offset2 - offset1).toInt()
}
