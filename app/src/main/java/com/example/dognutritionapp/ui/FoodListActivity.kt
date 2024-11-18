package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        //get logged user
        val userId = intent.getIntExtra("userId", -1)
        // Get the selected category ID from intent
        val categoryId = intent.getIntExtra("categoryId", -1)
        if (categoryId != -1) {
            observeFoodByCategory(categoryId)
        }else{
            MotionToast.createColorToast(
                this,
                "Category Id not found",
                "Error",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.font1))
        }

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        recyclerView = findViewById(R.id.viewFoodList)
        searchField = findViewById(R.id.foodSearchBar)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        petFoodAdapter = FoodSearchFilterListAdapter(emptyList()) { selectedFood ->
            val intent = Intent(this, FoodDetailActivity::class.java).apply {
                putExtra("foodName", selectedFood.name)
                putExtra("foodImage", selectedFood.imageUri)
                putExtra("foodPrice", selectedFood.price)
                putExtra("foodDescription", selectedFood.description)
                putExtra("foodId", selectedFood.foodId)
                putExtra("userId", userId)
            }
            startActivity(intent)
        }
        recyclerView.adapter = petFoodAdapter

        setupSearchListener(categoryId)
    }

    private fun observeFoodByCategory(categoryId: Int) {
        viewModel.getFoodByCategory(categoryId).observe(this) { foodList ->
            petFoodAdapter.updateData(foodList)
        }
    }

    private fun setupSearchListener(categoryId: Int) {
        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
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

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }
        })
    }

}