package com.example.dognutritionapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface PetFood_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: PetFoods)

    @Update
    suspend fun update(food: PetFoods)

    @Delete
    suspend fun delete(food: PetFoods)

    @Query("SELECT * FROM PetFood_table WHERE categoryId = :categoryId")
    fun getFoodByCategory(categoryId: Int): LiveData<List<PetFoods>>

    @Query("SELECT * FROM PetFood_table")
    fun getAllFood(): LiveData<List<PetFoods>>

    @Query("SELECT * FROM PetFood_table WHERE food_name LIKE :name")
    fun searchFoodByName(name: String): LiveData<List<PetFoods>>

    @Query("SELECT * FROM PetFood_table WHERE categoryId = :categoryId AND food_name LIKE :name")
    fun searchFoodByCategoryAndName(categoryId: Int, name: String): LiveData<List<PetFoods>>

    @Query("SELECT * FROM PetFood_table WHERE foodId = :foodId")
    suspend fun getFoodById(foodId: Int): PetFoods?


}
