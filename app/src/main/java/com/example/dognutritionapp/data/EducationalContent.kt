package com.example.dognutritionapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "educational_content_table")
data class EducationalContent(

    @PrimaryKey(autoGenerate = true) val contentId: Int = 0,
    @ColumnInfo(name = "content_title") val title: String,
    @ColumnInfo(name = "content_description") val description: String,
    @ColumnInfo(name = "content_url") val contentUrl: String
)
