package com.example.tripmanager.data.repository

import com.example.tripmanager.data.dao.FlightDao
import com.example.tripmanager.data.model.SavedFlight
import kotlinx.coroutines.flow.Flow

/**
 * Repository simplificado para vuelos - Solo operaciones locales sin APIs
 */
class FlightRepository(
    private val flightDao: FlightDao
) {
    
    fun getAllFlights(): Flow<List<SavedFlight>> = flightDao.getAllFlights()
    
    suspend fun getFlightById(id: Long): SavedFlight? = flightDao.getFlightById(id)
    
    fun getUpcomingFlights(): Flow<List<SavedFlight>> = flightDao.getUpcomingFlights(System.currentTimeMillis())
    
    fun getPastFlights(): Flow<List<SavedFlight>> = flightDao.getPastFlights(System.currentTimeMillis())
    
    fun searchFlights(query: String): Flow<List<SavedFlight>> = flightDao.searchFlights("%$query%")
    
    suspend fun insertFlight(flight: SavedFlight): Long = flightDao.insertFlight(flight)
    
    suspend fun updateFlight(flight: SavedFlight) = flightDao.updateFlight(flight)
    
    suspend fun deleteFlight(flight: SavedFlight) = flightDao.deleteFlight(flight)
}

