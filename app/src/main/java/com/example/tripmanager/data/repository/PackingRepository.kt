package com.example.tripmanager.data.repository

import com.example.tripmanager.data.dao.PackingDao
import com.example.tripmanager.data.model.PackingItem
import kotlinx.coroutines.flow.Flow

class PackingRepository(
    private val packingDao: PackingDao
) {
    fun getPackingItems(tripId: Long?): Flow<List<PackingItem>> = packingDao.getPackingItems(tripId)
    
    suspend fun getPackingItemById(itemId: Long): PackingItem? = packingDao.getPackingItemById(itemId)
    
    suspend fun insertPackingItem(item: PackingItem): Long = packingDao.insertPackingItem(item)
    
    suspend fun updatePackingItem(item: PackingItem) = packingDao.updatePackingItem(item)
    
    suspend fun deletePackingItem(item: PackingItem) = packingDao.deletePackingItem(item)
    
    suspend fun deletePackingItemById(itemId: Long) = packingDao.deletePackingItemById(itemId)
    
    suspend fun deletePackingItemsByTripId(tripId: Long) = packingDao.deletePackingItemsByTripId(tripId)
}




