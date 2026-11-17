package com.example.tripmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.tripmanager.data.database.TripDatabase
import com.example.tripmanager.data.model.FlightStatus
import com.example.tripmanager.data.repository.FlightRepository
import com.example.tripmanager.ui.viewmodel.FlightManualAddViewModel
import java.text.SimpleDateFormat
import java.util.*

fun formatDateTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightManualAddScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { TripDatabase.getDatabase(context) }
    val flightRepository = remember { FlightRepository(database.flightDao()) }
    val viewModel = remember { FlightManualAddViewModel(flightRepository) }
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.isSaveSuccessful) {
        if (uiState.isSaveSuccessful) {
            onNavigateBack()
        }
    }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isDepartureDateTime by remember { mutableStateOf(true) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Agregar Vuelo Manual",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Información del Vuelo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    OutlinedTextField(
                        value = uiState.flightNumber,
                        onValueChange = viewModel::setFlightNumber,
                        label = { Text("Número de Vuelo *") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej: AA123") },
                        singleLine = true
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.origin,
                            onValueChange = viewModel::setOrigin,
                            label = { Text("Origen *") },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("MEX") },
                            singleLine = true
                        )
                        
                        OutlinedTextField(
                            value = uiState.destination,
                            onValueChange = viewModel::setDestination,
                            label = { Text("Destino *") },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("JFK") },
                            singleLine = true
                        )
                    }
                    
                    OutlinedTextField(
                        value = if (uiState.departureTime > 0) {
                            formatDateTime(uiState.departureTime)
                        } else "",
                        onValueChange = {},
                        label = { Text("Fecha y Hora de Salida *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isDepartureDateTime = true
                                showDatePicker = true
                            },
                        enabled = false,
                        trailingIcon = {
                            IconButton(onClick = {
                                isDepartureDateTime = true
                                showDatePicker = true
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                            }
                        }
                    )
                    
                    OutlinedTextField(
                        value = if (uiState.arrivalTime > 0) {
                            formatDateTime(uiState.arrivalTime)
                        } else "",
                        onValueChange = {},
                        label = { Text("Fecha y Hora de Llegada *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isDepartureDateTime = false
                                showDatePicker = true
                            },
                        enabled = false,
                        trailingIcon = {
                            IconButton(onClick = {
                                isDepartureDateTime = false
                                showDatePicker = true
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Fecha")
                            }
                        }
                    )
                    
                    OutlinedTextField(
                        value = uiState.airline,
                        onValueChange = viewModel::setAirline,
                        label = { Text("Aerolínea (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej: American Airlines") },
                        singleLine = true
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.gate,
                            onValueChange = viewModel::setGate,
                            label = { Text("Puerta/Gate") },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("A12") },
                            singleLine = true
                        )
                        
                        var expanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = when (uiState.status) {
                                    FlightStatus.EN_TIEMPO -> "En Tiempo"
                                    FlightStatus.RETRASADO -> "Retrasado"
                                    FlightStatus.CANCELADO -> "Cancelado"
                                    FlightStatus.EN_VUELO -> "En Vuelo"
                                    FlightStatus.PROGRAMADO -> "Programado"
                                    FlightStatus.DESCONOCIDO -> "Desconocido"
                                },
                                onValueChange = {},
                                label = { Text("Estado") },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = false,
                                trailingIcon = {
                                    IconButton(onClick = { expanded = true }) {
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                FlightStatus.values().forEach { status ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                when (status) {
                                                    FlightStatus.EN_TIEMPO -> "En Tiempo"
                                                    FlightStatus.RETRASADO -> "Retrasado"
                                                    FlightStatus.CANCELADO -> "Cancelado"
                                                    FlightStatus.EN_VUELO -> "En Vuelo"
                                                    FlightStatus.PROGRAMADO -> "Programado"
                                                    FlightStatus.DESCONOCIDO -> "Desconocido"
                                                }
                                            )
                                        },
                                        onClick = {
                                            viewModel.setStatus(status)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    if (uiState.status == FlightStatus.RETRASADO) {
                        OutlinedTextField(
                            value = if (uiState.delayMinutes > 0) uiState.delayMinutes.toString() else "",
                            onValueChange = { 
                                viewModel.setDelayMinutes(it.toIntOrNull() ?: 0)
                            },
                            label = { Text("Minutos de Retraso") },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("30") },
                            singleLine = true
                        )
                    }
                }
            }
            
            Button(
                onClick = viewModel::saveFlight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar Vuelo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Error message
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = error,
                            color = Color(0xFFC62828),
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { viewModel.clearError() }) {
                            Text("OK", color = Color(0xFFC62828))
                        }
                    }
                }
            }
        }
    }
}

