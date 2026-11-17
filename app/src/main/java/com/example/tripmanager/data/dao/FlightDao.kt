package com.example.tripmanager.data.dao

import androidx.room.*
import com.example.tripmanager.data.model.SavedFlight
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Query("SELECT * FROM saved_flights ORDER BY departureTime ASC")
    fun getAllFlights(): Flow<List<SavedFlight>>
    
    @Query("SELECT * FROM saved_flights WHERE id = :flightId")
    suspend fun getFlightById(flightId: Long): SavedFlight?
    
    @Query("SELECT * FROM saved_flights WHERE departureTime >= :currentTime ORDER BY departureTime ASC")
    fun getUpcomingFlights(currentTime: Long = System.currentTimeMillis()): Flow<List<SavedFlight>>
    
    @Query("SELECT * FROM saved_flights WHERE departureTime < :currentTime ORDER BY departureTime DESC")
    fun getPastFlights(currentTime: Long = System.currentTimeMillis()): Flow<List<SavedFlight>>
    
    @Query("SELECT * FROM saved_flights WHERE origin LIKE '%' || :airportCode || '%' OR destination LIKE '%' || :airportCode || '%' ORDER BY departureTime ASC")
    fun getFlightsByAirport(airportCode: String): Flow<List<SavedFlight>>
    
    @Query("SELECT * FROM saved_flights WHERE flightNumber LIKE '%' || :query || '%' OR origin LIKE '%' || :query || '%' OR destination LIKE '%' || :query || '%' ORDER BY departureTime ASC")
    fun searchFlights(query: String): Flow<List<SavedFlight>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlight(flight: SavedFlight): Long
    
    @Update
    suspend fun updateFlight(flight: SavedFlight)
    
    @Delete
    suspend fun deleteFlight(flight: SavedFlight)
    
    @Query("DELETE FROM saved_flights WHERE id = :flightId")
    suspend fun deleteFlightById(flightId: Long)
}













