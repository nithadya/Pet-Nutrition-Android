package com.example.dognutritionapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.data.Cart
import com.example.dognutritionapp.data.Category
import com.example.dognutritionapp.data.EducationalContent
import com.example.dognutritionapp.data.Order
import com.example.dognutritionapp.data.PetFoods
import com.example.dognutritionapp.data.User
import kotlinx.coroutines.launch

class PetFoodViewModel (private val repository: AppRepository) : ViewModel() {

    val allCategories: LiveData<List<Category>> = repository.allCategories
    val allContent: LiveData<List<EducationalContent>> = repository.allContent
    val allUsers: LiveData<List<User>> = repository.allUsers
    val allFood: LiveData<List<PetFoods>> = repository.getAllFood()


    fun getOrdersByUser(userId: Int): LiveData<List<Order>> {
        return repository.getOrdersByUser(userId)
    }

    fun insertCategory(category: Category) = viewModelScope.launch {
        repository.insertCategory(category)
    }

    fun getFoodByCategory(categoryId: Int): LiveData<List<PetFoods>> {
        return repository.getFoodByCategory(categoryId)
    }

    fun insertFood(food: PetFoods) = viewModelScope.launch {
        repository.insertFood(food)
    }

    fun updateFood(food: PetFoods) = viewModelScope.launch {
        repository.updateFood(food)
    }

    fun deleteFood(food: PetFoods) = viewModelScope.launch {
        repository.deleteFood(food)
    }

    fun searchFoodByName(name: String): LiveData<List<PetFoods>> {
        return repository.searchFoodByName(name)
    }

    fun searchFoodByCategoryAndName(categoryId: Int, name: String): LiveData<List<PetFoods>> {
        return repository.searchFoodByCategoryAndName(categoryId, name)
    }

     suspend fun getFoodById(foodId: Int): PetFoods? {
        return repository.getFoodById(foodId)
    }

    fun insertContent(content: EducationalContent) = viewModelScope.launch {
        repository.insertContent(content)
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun updateUser(user: User) = viewModelScope.launch {
        repository.updateUser(user)
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    suspend fun getUserById(userId: Int): User? {
        return repository.getUserById(userId)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return repository.getUserByEmailAndPassword(email, password)
    }

    fun getCartItemsByUser(userId: Int) = repository.getCartItemsByUser(userId)

    fun insertCartItem(cart: Cart) = viewModelScope.launch {
        repository.insertCartItem(cart)
    }

    fun updateCartItem(cart: Cart) = viewModelScope.launch {
        repository.updateCartItem(cart)
    }

    fun deleteCartItem(cart: Cart) = viewModelScope.launch {
        repository.deleteCartItem(cart)
    }

    suspend fun getCartItem(userId: Int, foodId: Int): Cart? {
        return repository.getCartItem(userId, foodId)
    }
}