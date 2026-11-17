package com.example.tripmanager.data.database

import androidx.room.TypeConverter
import com.example.tripmanager.data.model.FlightStatus

class FlightStatusConverter {
    @TypeConverter
    fun fromStatus(status: FlightStatus): String {
        return status.name
    }
    
    @TypeConverter
    fun toStatus(value: String): FlightStatus {
        return try {
            FlightStatus.valueOf(value)
        } catch (e: Exception) {
            FlightStatus.DESCONOCIDO
        }
    }
}










