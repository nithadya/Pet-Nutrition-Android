package com.example.dognutritionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val categoryId: Int = 0,
    @ColumnInfo(name = "category_name") val name: String,
    @ColumnInfo(name = "category_image") val imageUri: String
)
