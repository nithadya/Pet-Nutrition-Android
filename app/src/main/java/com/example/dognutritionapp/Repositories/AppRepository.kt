package com.example.dognutritionapp.Repositories

import androidx.lifecycle.LiveData
import com.example.dognutritionapp.data.Cart
import com.example.dognutritionapp.data.Category
import com.example.dognutritionapp.data.EducationalContent
import com.example.dognutritionapp.data.Order
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.PetFoods
import com.example.dognutritionapp.data.User

class AppRepository(private val database: PetFoodDB) {

    // Category Operations
    val allCategories: LiveData<List<Category>> = database.categoryDao().getAllCategories()

    suspend fun insertCategory(category: Category) {
        database.categoryDao().insert(category)
    }

    // Food Operations

    fun getAllFood(): LiveData<List<PetFoods>> {
        return database.foodDao().getAllFood()
    }

    fun getFoodByCategory(categoryId: Int): LiveData<List<PetFoods>> {
        return database.foodDao().getFoodByCategory(categoryId)
    }

    suspend fun insertFood(food: PetFoods) {
        database.foodDao().insert(food)
    }

    suspend fun updateFood(food: PetFoods) {
        database.foodDao().update(food)
    }

    suspend fun deleteFood(food: PetFoods) {
        database.foodDao().delete(food)
    }

    fun searchFoodByName(name: String): LiveData<List<PetFoods>> {
        return database.foodDao().searchFoodByName("%$name%")
    }

    fun searchFoodByCategoryAndName(categoryId: Int, name: String): LiveData<List<PetFoods>> {
        return database.foodDao().searchFoodByCategoryAndName(categoryId, "%$name%")
    }

    suspend fun getFoodById(foodId: Int): PetFoods? {
        return database.foodDao().getFoodById(foodId)
    }

    // Educational Content Operations
    val allContent: LiveData<List<EducationalContent>> = database.educationalContentDao().getAllContent()

    suspend fun insertContent(content: EducationalContent) {
        database.educationalContentDao().insert(content)
    }

    // Order Operations
    fun getOrdersByUser(userId: Int): LiveData<List<Order>> {
        return database.orderDao().getOrdersByUser(userId)
    }

    suspend fun insertOrder(order: Order) {
        database.orderDao().insert(order)
    }

    // User Operations
    val allUsers: LiveData<List<User>> = database.userDao().getAllUsers()

    suspend fun insertUser(user: User) {
        database.userDao().insert(user)
    }

    suspend fun updateUser(user: User) {
        database.userDao().update(user)
    }

    suspend fun deleteUser(user: User) {
        database.userDao().delete(user)
    }

    suspend fun getUserById(userId: Int): User? {
        return database.userDao().getUserById(userId)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return database.userDao().getUserByEmailAndPassword(email, password)
    }


    // Cart Operations
    fun getCartItemsByUser(userId: Int) = database.cartDao().getCartItemsByUser(userId)

    suspend fun insertCartItem(cart: Cart) {
        database.cartDao().insert(cart)
    }

    suspend fun updateCartItem(cart: Cart) {
        database.cartDao().update(cart)
    }

    suspend fun deleteCartItem(cart: Cart) {
        database.cartDao().delete(cart)
    }

     suspend fun getCartItem(userId: Int, foodId: Int): Cart? {
        return database.cartDao().getCartItem(userId, foodId)
    }


}