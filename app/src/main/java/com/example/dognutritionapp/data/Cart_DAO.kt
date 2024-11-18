package com.example.dognutritionapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface Cart_DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: Cart)

    @Update
    suspend fun update(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)

    @Query("SELECT * FROM cart_table WHERE userId = :userId")
    fun getCartItemsByUser(userId: Int): LiveData<List<Cart>>

    @Query("SELECT * FROM cart_table WHERE userId = :userId AND foodId = :foodId")
    suspend fun getCartItem(userId: Int, foodId: Int): Cart?
}
