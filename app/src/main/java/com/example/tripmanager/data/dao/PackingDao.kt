package com.example.tripmanager.data.dao

import androidx.room.*
import com.example.tripmanager.data.model.PackingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PackingDao {
    @Query("SELECT * FROM packing_items WHERE tripId = :tripId OR tripId IS NULL ORDER BY category ASC, item ASC")
    fun getPackingItems(tripId: Long?): Flow<List<PackingItem>>
    
    @Query("SELECT * FROM packing_items WHERE id = :itemId")
    suspend fun getPackingItemById(itemId: Long): PackingItem?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackingItem(item: PackingItem): Long
    
    @Update
    suspend fun updatePackingItem(item: PackingItem)
    
    @Delete
    suspend fun deletePackingItem(item: PackingItem)
    
    @Query("DELETE FROM packing_items WHERE id = :itemId")
    suspend fun deletePackingItemById(itemId: Long)
    
    @Query("DELETE FROM packing_items WHERE tripId = :tripId")
    suspend fun deletePackingItemsByTripId(tripId: Long)
}




