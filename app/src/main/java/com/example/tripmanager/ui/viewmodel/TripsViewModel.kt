package com.example.tripmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmanager.data.model.Trip
import com.example.tripmanager.data.repository.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TripsViewModel(
    private val tripRepository: TripRepository,
    private val currentUser: String
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()

    init {
        loadTrips()
    }

    private fun loadTrips() {
        viewModelScope.launch {
            tripRepository.getTripsByUser(currentUser).collect { trips ->
                val currentState = _uiState.value
                val filtered = if (currentState.isFiltering && currentState.dateRangeStart != null && currentState.dateRangeEnd != null) {
                    filterTripsByDateRange(trips, currentState.dateRangeStart!!, currentState.dateRangeEnd!!)
                } else {
                    trips
                }
                _uiState.value = _uiState.value.copy(
                    trips = trips,
                    filteredTrips = filtered
                )
            }
        }
    }
    
    fun setDateRange(startDate: Long?, endDate: Long?) {
        val isFiltering = startDate != null && endDate != null
        val filtered = if (isFiltering) {
            filterTripsByDateRange(_uiState.value.trips, startDate!!, endDate!!)
        } else {
            _uiState.value.trips
        }
        _uiState.value = _uiState.value.copy(
            dateRangeStart = startDate,
            dateRangeEnd = endDate,
            isFiltering = isFiltering,
            filteredTrips = filtered
        )
    }
    
    fun clearDateRange() {
        _uiState.value = _uiState.value.copy(
            dateRangeStart = null,
            dateRangeEnd = null,
            isFiltering = false,
            filteredTrips = _uiState.value.trips
        )
    }
    
    /**
     * Filtra viajes que se solapan con el rango de fechas seleccionado
     * Un viaje se incluye si:
     * - Su inicio está dentro del rango, o
     * - Su fin está dentro del rango, o
     * - El viaje contiene completamente el rango, o
     * - El rango contiene completamente el viaje
     */
    private fun filterTripsByDateRange(trips: List<Trip>, rangeStart: Long, rangeEnd: Long): List<Trip> {
        return trips.filter { trip ->
            // Verificar si hay solapamiento entre el viaje y el rango
            (trip.startDate <= rangeEnd && trip.endDate >= rangeStart)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            try {
                tripRepository.deleteTrip(trip)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error al eliminar el viaje: ${e.message}"
                )
            }
        }
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class TripsUiState(
    val trips: List<Trip> = emptyList(),
    val filteredTrips: List<Trip> = emptyList(),
    val dateRangeStart: Long? = null,
    val dateRangeEnd: Long? = null,
    val isFiltering: Boolean = false,
    val errorMessage: String? = null
)
