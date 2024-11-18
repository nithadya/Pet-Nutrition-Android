package com.example.dognutritionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_table",
    foreignKeys = [ForeignKey(
        entity = PetFoods::class,
        parentColumns = ["foodId"],
        childColumns = ["foodId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Cart(
    @PrimaryKey(autoGenerate = true) val cartId: Int = 0,
    @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "foodId") val foodId: Int,
    @ColumnInfo(name = "quantity") var quantity: Int
)
