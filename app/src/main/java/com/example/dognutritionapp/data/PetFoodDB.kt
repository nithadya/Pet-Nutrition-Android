package com.example.dognutritionapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Category::class, PetFoods::class, EducationalContent::class, Order::class,
        User::class, Cart::class],
    version = 1,
    exportSchema = false
)
abstract class PetFoodDB: RoomDatabase()  {
    abstract fun categoryDao(): Category_DAO
    abstract fun foodDao(): PetFood_DAO
    abstract fun educationalContentDao(): Educational_Content_DAO
    abstract fun orderDao(): Order_DAO
    abstract fun userDao(): User_DAO
    abstract fun cartDao(): Cart_DAO

    companion object {
        @Volatile
        private var INSTANCE: PetFoodDB? = null

        fun getDatabase(context: Context): PetFoodDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PetFoodDB::class.java,
                        "DogFood_Database"
                    ).build()
                }
                return instance
            }
        }
    }
}




