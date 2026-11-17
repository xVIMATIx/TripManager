package com.example.tripmanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_flights")
data class SavedFlight(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val flightNumber: String,        // "AA123"
    val origin: String,              // "MEX" (código aeropuerto)
    val originName: String?,         // "Aeropuerto Internacional de la Ciudad de México"
    val destination: String,         // "JFK"
    val destinationName: String?,    // "John F. Kennedy International Airport"
    val departureTime: Long,         // Timestamp
    val arrivalTime: Long,           // Timestamp
    val airline: String?,            // "American Airlines"
    val gate: String?,              // "A12"
    val status: FlightStatus,        // EN_TIEMPO, RETRASADO, etc.
    val delayMinutes: Int = 0,      // Minutos de retraso
    val dateAdded: Long = System.currentTimeMillis() // Cuando lo agregó el usuario
)

enum class FlightStatus {
    EN_TIEMPO,      // On Time
    RETRASADO,      // Delayed
    CANCELADO,      // Cancelled
    EN_VUELO,       // In Flight
    PROGRAMADO,     // Scheduled
    DESCONOCIDO     // Unknown
}




