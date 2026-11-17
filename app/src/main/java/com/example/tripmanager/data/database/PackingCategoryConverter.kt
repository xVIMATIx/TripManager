package com.example.tripmanager.data.database

import androidx.room.TypeConverter
import com.example.tripmanager.data.model.PackingCategory

class PackingCategoryConverter {
    @TypeConverter
    fun fromPackingCategory(category: PackingCategory): String {
        return category.name
    }
    
    @TypeConverter
    fun toPackingCategory(category: String): PackingCategory {
        return PackingCategory.valueOf(category)
    }
}




