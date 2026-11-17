package com.example.tripmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packing_items")
data class PackingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tripId: Long? = null, // null = plantilla global
    val category: PackingCategory,
    val item: String,
    val isChecked: Boolean = false,
    val isCustom: Boolean = true // true = agregado por usuario, false = de plantilla
)

enum class PackingCategory {
    ROPA,
    CALZADO,
    ARTICULOS_HIGIENE,
    ELECTRONICOS,
    DOCUMENTOS,
    MEDICINA,
    COMIDA,
    OTROS
}

enum class TripPackingType {
    PLAYA,
    MONTAÑA,
    TRABAJO,
    CIUDAD,
    AVENTURA,
    PERSONALIZADO
}




