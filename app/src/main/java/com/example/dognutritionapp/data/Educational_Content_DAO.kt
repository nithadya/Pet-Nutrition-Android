package com.example.dognutritionapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Educational_Content_DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(content: EducationalContent)

    @Query("SELECT * FROM educational_content_table")
    fun getAllContent(): LiveData<List<EducationalContent>>
}