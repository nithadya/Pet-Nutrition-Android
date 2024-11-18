package com.example.dognutritionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "PetFood_table",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["categoryId"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class PetFoods(

    @PrimaryKey(autoGenerate = true) val foodId: Int = 0,
    @ColumnInfo(name = "food_name") val name: String,
    @ColumnInfo(name = "food_description") val description: String,
    @ColumnInfo(name = "food_image") val imageUri: String,
    @ColumnInfo(name = "food_price") val price: Double,
    @ColumnInfo(name = "categoryId", index = true) val categoryId: Int

    )