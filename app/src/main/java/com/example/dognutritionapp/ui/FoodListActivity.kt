package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.adapters.FoodSearchFilterListAdapter
import com.example.dognutritionapp.adapters.PetFoodListAdapter
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.helper.BaseActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class FoodListActivity : BaseActivity() {
    private lateinit var viewModel: PetFoodViewModel
    private lateinit var petFoodAdapter: FoodSearchFilterListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText
    private var userId: Int = -1 // Default value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        // Initialize views
        progressBar = findViewById(R.id.progressBarFoodList)
        searchField = findViewById(R.id.foodSearchBar)
        recyclerView = findViewById(R.id.viewFoodList)

        // Extract USER_ID from Intent
        userId = intent.getIntExtra("USER_ID", -1)
        if (userId == -1) {
            MotionToast.createColorToast(
                this,
                "User Id not found",
                "Error",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.font1))
            finish() // Exit the activity if user ID is not valid
            return
        }

        // Initialize RecyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        petFoodAdapter = FoodSearchFilterListAdapter(emptyList()) { selectedFood ->
            openFoodDetailActivity(selectedFood.foodId, selectedFood.name, selectedFood.imageUri,
                selectedFood.price.toString(), selectedFood.description)
        }
        recyclerView.adapter = petFoodAdapter

        // Initialize ViewModel
        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        // Fetch food items by category or all food items
        val categoryId = intent.getIntExtra("categoryId", -1)
        if (categoryId != -1) {
            Log.d("FoodListActivity", "Received Category ID: $categoryId")
            observeFoodByCategory(categoryId)
        } else {
            observeAllFood()
        }

        // Hide progress bar once data is ready
        progressBar.visibility = View.GONE

        // Setup search functionality
        setupSearchListener(categoryId)


        // Handle "Back" button click
        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
            finish()
        }

    }

    private fun observeFoodByCategory(categoryId: Int) {
        viewModel.getFoodByCategory(categoryId).observe(this) { foodList ->
            petFoodAdapter.updateData(foodList)
        }
    }

    private fun observeAllFood() {
        viewModel.allFood.observe(this) { foodList ->
            petFoodAdapter.updateData(foodList)
        }
    }

    private fun setupSearchListener(categoryId: Int) {
        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    if (categoryId != -1) {
                        viewModel.searchFoodByCategoryAndName(categoryId, query).observe(this@FoodListActivity) { filteredList ->
                            petFoodAdapter.updateData(filteredList)
                        }
                    } else {
                        viewModel.searchFoodByName(query).observe(this@FoodListActivity) { filteredList ->
                            petFoodAdapter.updateData(filteredList)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun openFoodDetailActivity(foodId: Int, name: String, imageUri: String, price: String, description: String) {
        val intent = Intent(this, FoodDetailActivity::class.java).apply {
            putExtra("foodName", name)
            putExtra("foodImage", imageUri)
            putExtra("foodPrice", price)
            putExtra("foodDescription", description)
            putExtra("foodId", foodId)
            putExtra("USER_ID", userId) // Pass user ID to FoodDetailActivity
        }
        startActivity(intent)
    }
}