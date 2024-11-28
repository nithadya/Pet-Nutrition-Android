package com.example.dognutritionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    @ColumnInfo(name = "user_name") var name: String,
    @ColumnInfo(name = "user_email") var email: String,
    @ColumnInfo(name = "user_password") val password: String,
    @ColumnInfo(name = "user_address") var address: String,
    @ColumnInfo(name = "user_type") var userType: String,// Values: "admin" or "customer"
    @ColumnInfo(name = "user_profile_image") val profileImageUri: String? = null // Profile image URI
)
