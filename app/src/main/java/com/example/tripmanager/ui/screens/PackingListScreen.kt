package com.example.tripmanager.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.platform.LocalContext
import com.example.tripmanager.data.database.TripDatabase
import com.example.tripmanager.data.model.PackingCategory
import com.example.tripmanager.data.model.PackingItem
import com.example.tripmanager.data.model.TripPackingType
import com.example.tripmanager.data.repository.PackingRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackingListScreen(
    tripId: Long? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val database = remember { TripDatabase.getDatabase(context) }
    val packingRepository = remember { PackingRepository(database.packingDao()) }
    
    var selectedType by remember { mutableStateOf<TripPackingType?>(null) }
    var showTypeSelector by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(PackingCategory.ROPA) }
    
    val packingItems = remember { mutableStateListOf<PackingItem>() }
    
    LaunchedEffect(selectedType) {
        if (selectedType != null) {
            // Cargar items de plantilla según el tipo seleccionado
            packingItems.clear()
            packingItems.addAll(getTemplateItems(selectedType!!))
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lista de Empaque",
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
                    IconButton(onClick = { showTypeSelector = true }) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Tipo de viaje",
                            tint = Color(0xFF2196F3)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar item",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Selector de tipo de viaje
            if (selectedType == null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTypeSelector = true },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Selecciona el tipo de viaje para generar una plantilla",
                            fontSize = 14.sp,
                            color = Color(0xFF2196F3)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF2196F3)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                // Mostrar tipo seleccionado
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tipo: ${getTypeName(selectedType!!)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4CAF50)
                        )
                        IconButton(onClick = { showTypeSelector = true }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Cambiar tipo",
                                tint = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Lista de items agrupados por categoría
            if (packingItems.isEmpty() && selectedType != null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay items en la lista",
                        fontSize = 16.sp,
                        color = Color(0xFF666666)
                    )
                }
            } else {
                val itemsByCategory = packingItems.groupBy { it.category }
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsByCategory.forEach { (category, items) ->
                        item {
                            Text(
                                text = getCategoryName(category),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(items) { item ->
                            PackingItemCard(
                                item = item,
                                onToggle = {
                                    val index = packingItems.indexOf(item)
                                    if (index != -1) {
                                        packingItems[index] = item.copy(isChecked = !item.isChecked)
                                    }
                                },
                                onDelete = {
                                    packingItems.remove(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Dialog para seleccionar tipo de viaje
    if (showTypeSelector) {
        AlertDialog(
            onDismissRequest = { showTypeSelector = false },
            title = { Text("Seleccionar Tipo de Viaje") },
            text = {
                Column {
                    TripPackingType.values().forEach { type ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedType = type
                                    showTypeSelector = false
                                },
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Text(
                                text = getTypeName(type),
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTypeSelector = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Dialog para agregar item
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Agregar Item") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = newItemText,
                        onValueChange = { newItemText = it },
                        label = { Text("Nombre del item") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    var categoryExpanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = !categoryExpanded }) {
                        OutlinedTextField(
                            value = getCategoryName(selectedCategory),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            PackingCategory.values().forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(getCategoryName(category)) },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newItemText.isNotBlank()) {
                            packingItems.add(
                                PackingItem(
                                    tripId = tripId,
                                    category = selectedCategory,
                                    item = newItemText,
                                    isChecked = false,
                                    isCustom = true
                                )
                            )
                            newItemText = ""
                            showAddDialog = false
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PackingItemCard(
    item: PackingItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { onToggle() }
                )
                Text(
                    text = item.item,
                    fontSize = 16.sp,
                    color = if (item.isChecked) Color(0xFF999999) else Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFE57373)
                )
            }
        }
    }
}

fun getTypeName(type: TripPackingType): String {
    return when (type) {
        TripPackingType.PLAYA -> "Playa"
        TripPackingType.MONTAÑA -> "Montaña"
        TripPackingType.TRABAJO -> "Trabajo"
        TripPackingType.CIUDAD -> "Ciudad"
        TripPackingType.AVENTURA -> "Aventura"
        TripPackingType.PERSONALIZADO -> "Personalizado"
    }
}

fun getCategoryName(category: PackingCategory): String {
    return when (category) {
        PackingCategory.ROPA -> "Ropa"
        PackingCategory.CALZADO -> "Calzado"
        PackingCategory.ARTICULOS_HIGIENE -> "Artículos de Higiene"
        PackingCategory.ELECTRONICOS -> "Electrónicos"
        PackingCategory.DOCUMENTOS -> "Documentos"
        PackingCategory.MEDICINA -> "Medicina"
        PackingCategory.COMIDA -> "Comida"
        PackingCategory.OTROS -> "Otros"
    }
}

fun getTemplateItems(type: TripPackingType): List<PackingItem> {
    return when (type) {
        TripPackingType.PLAYA -> listOf(
            PackingItem(category = PackingCategory.ROPA, item = "Traje de baño", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Shorts", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Camisetas ligeras", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Sandalias", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Gorra/Sombrero", isCustom = false),
            PackingItem(category = PackingCategory.ARTICULOS_HIGIENE, item = "Bloqueador solar", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Gafas de sol", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Toalla", isCustom = false)
        )
        TripPackingType.MONTAÑA -> listOf(
            PackingItem(category = PackingCategory.ROPA, item = "Abrigo impermeable", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Botas de montaña", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Guantes", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Gorro", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Mochila", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Pantalones térmicos", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Bastones de trekking", isCustom = false),
            PackingItem(category = PackingCategory.MEDICINA, item = "Botiquín de primeros auxilios", isCustom = false)
        )
        TripPackingType.TRABAJO -> listOf(
            PackingItem(category = PackingCategory.ROPA, item = "Traje formal", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Camisas formales", isCustom = false),
            PackingItem(category = PackingCategory.CALZADO, item = "Zapatos formales", isCustom = false),
            PackingItem(category = PackingCategory.ELECTRONICOS, item = "Laptop", isCustom = false),
            PackingItem(category = PackingCategory.ELECTRONICOS, item = "Cargador laptop", isCustom = false),
            PackingItem(category = PackingCategory.DOCUMENTOS, item = "Documentos de trabajo", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Maletín", isCustom = false)
        )
        TripPackingType.CIUDAD -> listOf(
            PackingItem(category = PackingCategory.ROPA, item = "Ropa casual", isCustom = false),
            PackingItem(category = PackingCategory.CALZADO, item = "Zapatos cómodos", isCustom = false),
            PackingItem(category = PackingCategory.ELECTRONICOS, item = "Cámara", isCustom = false),
            PackingItem(category = PackingCategory.DOCUMENTOS, item = "Mapa/Guía", isCustom = false),
            PackingItem(category = PackingCategory.ELECTRONICOS, item = "Power bank", isCustom = false)
        )
        TripPackingType.AVENTURA -> listOf(
            PackingItem(category = PackingCategory.ROPA, item = "Ropa deportiva", isCustom = false),
            PackingItem(category = PackingCategory.CALZADO, item = "Zapatos deportivos", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Mochila", isCustom = false),
            PackingItem(category = PackingCategory.MEDICINA, item = "Botiquín", isCustom = false),
            PackingItem(category = PackingCategory.ROPA, item = "Linterna", isCustom = false)
        )
        TripPackingType.PERSONALIZADO -> emptyList()
    }
}

