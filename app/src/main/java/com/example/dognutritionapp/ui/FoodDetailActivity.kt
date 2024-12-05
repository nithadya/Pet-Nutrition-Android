package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.data.Cart
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.helper.BaseActivity
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class FoodDetailActivity : BaseActivity() {

    private lateinit var viewModel: PetFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)


        // Retrieve data from intent
        val foodName = intent.getStringExtra("foodName")
        val foodImageUri = intent.getStringExtra("foodImage")
        val foodPrice = intent.getDoubleExtra("foodPrice", 0.0)
        val foodDescription = intent.getStringExtra("foodDescription")
        val foodId = intent.getIntExtra("foodId",-1)
        val userId = intent.getIntExtra("USER_ID", -1)


        // Set data to UI elements
        findViewById<TextView>(R.id.titleTxt).text = foodName
        findViewById<TextView>(R.id.priceTxt).text = "LKR $foodPrice"
        findViewById<TextView>(R.id.descriptionText).text = foodDescription
        Glide.with(this)
            .load(foodImageUri)
            .into(findViewById(R.id.foodDetailImage))


        // Set up Add to Cart button
        val addToCartBtn = findViewById<Button>(R.id.addToCartBtn)
        addToCartBtn.setOnClickListener {
            addToCart()
        }


        // Handle "Back" button click
        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
            finish()
        }


        findViewById<ImageButton>(R.id.cartBtn).setOnClickListener {
            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("USER_ID", userId)
                putExtra("foodId", foodId)
            }
            startActivity(intent)
        }
    }

    fun addToCart(){
        val foodId = intent.getIntExtra("foodId", 0)
        val userId = intent.getIntExtra("USER_ID", -1)

        if (userId == -1) {
            MotionToast.createColorToast(
                this,
                "User ID not found",
                "Warning",
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.font1))
            finish()
            return
        }

        // Check if item already exists in the cart for the user
        viewModel.viewModelScope.launch {
            try {
                val existingCartItem = viewModel.getCartItem(foodId, foodId)

                if (existingCartItem == null) {
                    val newCartItem = Cart(userId = userId, foodId = foodId, quantity = 1)
                    viewModel.insertCartItem(newCartItem)
                    runOnUiThread {
                        MotionToast.createColorToast(
                            this@FoodDetailActivity,
                            "Added to cart!!",
                            "Success",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@FoodDetailActivity, R.font.font1))

                    }
                } else {
                    existingCartItem.quantity += 1
                    viewModel.updateCartItem(existingCartItem)
                    runOnUiThread {
                        MotionToast.createColorToast(
                            this@FoodDetailActivity,
                            "Cart updated!",
                            "Update",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@FoodDetailActivity, R.font.font1))
                    }
                }



            } catch (e: Exception) {
                runOnUiThread {
                    MotionToast.createColorToast(
                        this@FoodDetailActivity,
                        "Failed to add to cart: ${e.message}\"",
                        "Error",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@FoodDetailActivity, R.font.font1))
                }
            }
        }
    }
}