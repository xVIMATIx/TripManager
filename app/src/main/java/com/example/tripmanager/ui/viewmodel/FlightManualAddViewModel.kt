package com.example.tripmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripmanager.data.model.FlightStatus
import com.example.tripmanager.data.model.SavedFlight
import com.example.tripmanager.data.repository.FlightRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlightManualAddViewModel(
    private val flightRepository: FlightRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FlightManualAddUiState())
    val uiState: StateFlow<FlightManualAddUiState> = _uiState.asStateFlow()
    
    fun setFlightNumber(flightNumber: String) {
        _uiState.value = _uiState.value.copy(flightNumber = flightNumber.uppercase().trim())
    }
    
    fun setOrigin(origin: String) {
        _uiState.value = _uiState.value.copy(origin = origin.uppercase().trim())
    }
    
    fun setDestination(destination: String) {
        _uiState.value = _uiState.value.copy(destination = destination.uppercase().trim())
    }
    
    fun setDepartureTime(departureTime: Long) {
        _uiState.value = _uiState.value.copy(departureTime = departureTime)
    }
    
    fun setArrivalTime(arrivalTime: Long) {
        _uiState.value = _uiState.value.copy(arrivalTime = arrivalTime)
    }
    
    fun setAirline(airline: String) {
        _uiState.value = _uiState.value.copy(airline = airline)
    }
    
    fun setGate(gate: String) {
        _uiState.value = _uiState.value.copy(gate = gate.uppercase().trim())
    }
    
    fun setStatus(status: FlightStatus) {
        _uiState.value = _uiState.value.copy(status = status)
    }
    
    fun setDelayMinutes(delayMinutes: Int) {
        _uiState.value = _uiState.value.copy(delayMinutes = delayMinutes)
    }
    
    fun saveFlight() {
        val currentState = _uiState.value
        
        if (!isValid(currentState)) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true, errorMessage = null)
            
            try {
                val flight = SavedFlight(
                    id = 0,
                    flightNumber = currentState.flightNumber,
                    origin = currentState.origin,
                    originName = null,
                    destination = currentState.destination,
                    destinationName = null,
                    departureTime = currentState.departureTime,
                    arrivalTime = currentState.arrivalTime,
                    airline = currentState.airline.ifEmpty { null },
                    gate = currentState.gate.ifEmpty { null },
                    status = currentState.status,
                    delayMinutes = currentState.delayMinutes,
                    dateAdded = System.currentTimeMillis()
                )
                
                flightRepository.insertFlight(flight)
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isSaveSuccessful = true
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Error al guardar el vuelo: ${e.message}"
                )
            }
        }
    }
    
    private fun isValid(state: FlightManualAddUiState): Boolean {
        if (state.flightNumber.isEmpty()) {
            _uiState.value = state.copy(errorMessage = "El número de vuelo es obligatorio")
            return false
        }
        
        if (state.origin.isEmpty() || state.origin.length < 3) {
            _uiState.value = state.copy(errorMessage = "El código de origen debe tener 3 caracteres")
            return false
        }
        
        if (state.destination.isEmpty() || state.destination.length < 3) {
            _uiState.value = state.copy(errorMessage = "El código de destino debe tener 3 caracteres")
            return false
        }
        
        if (state.origin == state.destination) {
            _uiState.value = state.copy(errorMessage = "El origen y destino no pueden ser iguales")
            return false
        }
        
        if (state.departureTime == 0L) {
            _uiState.value = state.copy(errorMessage = "Por favor selecciona la fecha y hora de salida")
            return false
        }
        
        if (state.arrivalTime == 0L) {
            _uiState.value = state.copy(errorMessage = "Por favor selecciona la fecha y hora de llegada")
            return false
        }
        
        if (state.arrivalTime <= state.departureTime) {
            _uiState.value = state.copy(errorMessage = "La hora de llegada debe ser posterior a la de salida")
            return false
        }
        
        return true
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class FlightManualAddUiState(
    val flightNumber: String = "",
    val origin: String = "",
    val destination: String = "",
    val departureTime: Long = 0L,
    val arrivalTime: Long = 0L,
    val airline: String = "",
    val gate: String = "",
    val status: FlightStatus = FlightStatus.PROGRAMADO,
    val delayMinutes: Int = 0,
    val isLoading: Boolean = false,
    val isSaveSuccessful: Boolean = false,
    val errorMessage: String? = null
)









