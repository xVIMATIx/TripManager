package com.example.tripmanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tripmanager.data.database.TripDatabase
import com.example.tripmanager.data.model.Trip
import com.example.tripmanager.data.model.TripType
import com.example.tripmanager.data.repository.TripRepository
import com.example.tripmanager.data.session.UserSession
import com.example.tripmanager.ui.viewmodel.TripsViewModel
import java.text.SimpleDateFormat
import java.util.*
import com.example.tripmanager.util.formatDatePickerMillis
import com.example.tripmanager.util.datePickerMillisToLocalDate
import com.example.tripmanager.R
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(
    username: String,
    onNavigateBack: () -> Unit,
    onNavigateToAddTrip: () -> Unit,
    onNavigateToEditTrip: (Long) -> Unit,
    onNavigateToTripDetail: (Long) -> Unit,
    onNavigateToTools: () -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember { TripDatabase.getDatabase(context) }
    val tripRepository = remember { TripRepository(database.tripDao()) }
    val tripsViewModel = remember { TripsViewModel(tripRepository, UserSession.getCurrentUser() ?: username) }
    val uiState by tripsViewModel.uiState.collectAsState()
    
    // Estados para el selector de rango de fechas
    var showDateRangePicker by remember { mutableStateOf(false) }
    var startDatePickerVisible by remember { mutableStateOf(false) }
    var endDatePickerVisible by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Viajes",
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
                actions = {
                    // Botón de Herramientas
                    IconButton(onClick = onNavigateToTools) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Herramientas",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                    // Botón de Filtro por Rango de Fechas
                    IconButton(onClick = { showDateRangePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Filtrar por rango de fechas",
                            tint = if (uiState.isFiltering) Color(0xFF2196F3) else Color(0xFF666666)
                        )
                    }
                    // Small logo in top right
                    Image(
                        painter = painterResource(id = R.drawable.log),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp)), // opcional, esquinas redondeadas
                        contentScale = ContentScale.Crop
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTrip,
                containerColor = Color(0xFF1DE9B6)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Trip",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Date Range Picker Dialog (usando dos DatePickers separados)
            if (showDateRangePicker) {
                DateRangeSelectionDialog(
                    onDismissRequest = { showDateRangePicker = false },
                    onConfirmClick = {
                        val startDate = startDatePickerState.selectedDateMillis?.let { 
                            datePickerMillisToLocalDate(it).time 
                        }
                        val endDate = endDatePickerState.selectedDateMillis?.let { 
                            datePickerMillisToLocalDate(it).time 
                        }
                        if (startDate != null && endDate != null) {
                            // Validar que la fecha inicio sea menor o igual a la fecha fin
                            if (startDate <= endDate) {
                                tripsViewModel.setDateRange(startDate, endDate)
                                showDateRangePicker = false
                            }
                        }
                    },
                    onClearClick = {
                        tripsViewModel.clearDateRange()
                        showDateRangePicker = false
                    },
                    startDatePickerState = startDatePickerState,
                    endDatePickerState = endDatePickerState,
                    showStartPicker = { startDatePickerVisible = true },
                    showEndPicker = { endDatePickerVisible = true },
                    startDatePickerVisible = startDatePickerVisible,
                    endDatePickerVisible = endDatePickerVisible,
                    onStartPickerDismiss = { startDatePickerVisible = false },
                    onEndPickerDismiss = { endDatePickerVisible = false }
                )
            }
            
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Indicador de rango de fechas activo
                if (uiState.isFiltering && uiState.dateRangeStart != null && uiState.dateRangeEnd != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Filtro activo",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(uiState.dateRangeStart!!))} - ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(uiState.dateRangeEnd!!))}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF2196F3)
                                )
                                Text(
                                    text = "${uiState.filteredTrips.size} viaje(s) encontrado(s)",
                                    fontSize = 12.sp,
                                    color = Color(0xFF666666)
                                )
                            }
                            Row {
                                IconButton(onClick = { showDateRangePicker = true }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Cambiar rango",
                                        tint = Color(0xFF2196F3)
                                    )
                                }
                                IconButton(onClick = { tripsViewModel.clearDateRange() }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar filtro",
                                        tint = Color(0xFF666666)
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Lista de viajes
                if (uiState.trips.isEmpty()) {
                    // Empty state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No tienes viajes aún",
                            fontSize = 18.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Toca el botón + para agregar tu primer viaje",
                            fontSize = 14.sp,
                            color = Color(0xFF999999),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    val tripsToShow = if (uiState.isFiltering) uiState.filteredTrips else uiState.trips
                    
                    if (uiState.isFiltering && tripsToShow.isEmpty()) {
                        // Empty state cuando hay filtro pero no hay resultados
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No hay viajes en este rango de fechas",
                                fontSize = 18.sp,
                                color = Color(0xFF666666),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Intenta seleccionar otro rango de fechas",
                                fontSize = 14.sp,
                                color = Color(0xFF999999),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(tripsToShow) { trip ->
                                TripCard(
                                    trip = trip,
                                    onTripClick = { onNavigateToTripDetail(trip.id) },
                                    onEditClick = { onNavigateToEditTrip(trip.id) },
                                    onDeleteClick = { tripsViewModel.deleteTrip(trip) }
                                )
                            }
                        }
                    }
                }
            }

            // Error message
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.9f))
                ) {
                    Text(
                        text = error,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onTripClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val startDate = dateFormat.format(Date(trip.startDate))
    val endDate = dateFormat.format(Date(trip.endDate))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTripClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = trip.destination,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "$startDate - $endDate",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = if (trip.tripType == TripType.VACATION) "Vacaciones" else "Trabajo",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
            
            Box {
                IconButton(
                    onClick = { showMenu = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Trip Options",
                        tint = Color(0xFF666666)
                    )
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color(0xFF666666),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        },
                        onClick = {
                            showMenu = false
                            onEditClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { 
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color(0xFFE57373),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar", color = Color(0xFFE57373))
                            }
                        },
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        }
                    )
                }
            }
            
            // Diálogo de confirmación para eliminar
            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    title = {
                        Text(
                            text = "Eliminar viaje",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Text("¿Estás seguro de que quieres eliminar este viaje? Esta acción no se puede deshacer.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                onDeleteClick()
                            }
                        ) {
                            Text(
                                text = "Eliminar",
                                color = Color(0xFFE57373)
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelectionDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    onClearClick: () -> Unit,
    startDatePickerState: DatePickerState,
    endDatePickerState: DatePickerState,
    showStartPicker: () -> Unit,
    showEndPicker: () -> Unit,
    startDatePickerVisible: Boolean,
    endDatePickerVisible: Boolean,
    onStartPickerDismiss: () -> Unit,
    onEndPickerDismiss: () -> Unit
) {
    // Date Picker para fecha inicio
    if (startDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = onStartPickerDismiss,
            confirmButton = {
                TextButton(onClick = onStartPickerDismiss) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = onStartPickerDismiss) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }
    
    // Date Picker para fecha fin
    if (endDatePickerVisible) {
        DatePickerDialog(
            onDismissRequest = onEndPickerDismiss,
            confirmButton = {
                TextButton(onClick = onEndPickerDismiss) {
                    Text("Seleccionar")
                }
            },
            dismissButton = {
                TextButton(onClick = onEndPickerDismiss) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }
    
    // Dialog principal para seleccionar rango
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Seleccionar Rango de Fechas",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Fecha Inicio
                OutlinedTextField(
                    value = startDatePickerState.selectedDateMillis?.let {
                        formatDatePickerMillis(it)
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha Inicio") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartPicker() },
                    trailingIcon = {
                        IconButton(onClick = { showStartPicker() }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha inicio"
                            )
                        }
                    }
                )
                
                // Fecha Fin
                OutlinedTextField(
                    value = endDatePickerState.selectedDateMillis?.let {
                        formatDatePickerMillis(it)
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha Fin") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showEndPicker() },
                    trailingIcon = {
                        IconButton(onClick = { showEndPicker() }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar fecha fin"
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onClearClick) {
                    Text("Limpiar", color = Color(0xFF666666))
                }
                TextButton(
                    onClick = onConfirmClick,
                    enabled = startDatePickerState.selectedDateMillis != null && 
                              endDatePickerState.selectedDateMillis != null &&
                              (startDatePickerState.selectedDateMillis!! <= endDatePickerState.selectedDateMillis!!)
                ) {
                    Text("Filtrar", color = Color(0xFF2196F3))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}
