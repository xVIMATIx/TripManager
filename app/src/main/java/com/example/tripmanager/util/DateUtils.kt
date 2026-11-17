package com.example.tripmanager.util

import java.util.*

/**
 * Convierte un timestamp de DatePicker (UTC medianoche) a una fecha local correcta.
 * Esto evita problemas de zona horaria donde se muestra un día anterior.
 */
fun datePickerMillisToLocalDate(datePickerMillis: Long): Date {
    // El selectedDateMillis del DatePicker es un timestamp UTC que representa medianoche UTC del día seleccionado
    // Para evitar problemas de zona horaria, extraemos año/mes/día desde UTC y luego creamos una fecha local
    
    // Crear Calendar en UTC para extraer la fecha correcta
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.timeInMillis = datePickerMillis
    
    // Extraer año, mes y día desde UTC
    val year = utcCalendar.get(Calendar.YEAR)
    val month = utcCalendar.get(Calendar.MONTH)
    val day = utcCalendar.get(Calendar.DAY_OF_MONTH)
    
    // Crear una nueva fecha a mediodía en la zona horaria local para evitar problemas de zona horaria
    val localCalendar = Calendar.getInstance() // Esto usa la zona horaria local del dispositivo
    localCalendar.set(Calendar.YEAR, year)
    localCalendar.set(Calendar.MONTH, month)
    localCalendar.set(Calendar.DAY_OF_MONTH, day)
    localCalendar.set(Calendar.HOUR_OF_DAY, 12) // Mediodía para evitar problemas de zona horaria
    localCalendar.set(Calendar.MINUTE, 0)
    localCalendar.set(Calendar.SECOND, 0)
    localCalendar.set(Calendar.MILLISECOND, 0)
    
    return localCalendar.time
}

/**
 * Convierte un timestamp de DatePicker a un formato de fecha string local.
 */
fun formatDatePickerMillis(datePickerMillis: Long, pattern: String = "dd/MM/yyyy"): String {
    val date = datePickerMillisToLocalDate(datePickerMillis)
    val dateFormat = java.text.SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(date)
}

