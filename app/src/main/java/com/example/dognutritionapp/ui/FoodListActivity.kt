package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
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
    private lateinit var progressBarFoodList: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        progressBarFoodList= findViewById(R.id.progressBarFoodList)
        progressBarFoodList.visibility = View.VISIBLE
        searchField = findViewById(R.id.foodSearchBar)
        recyclerView = findViewById(R.id.viewFoodList)
        //get logged user
        val userId = intent.getIntExtra("USER_ID", -1)

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        petFoodAdapter = FoodSearchFilterListAdapter(emptyList()) { selectedFood ->
            val intent = Intent(this, FoodDetailActivity::class.java).apply {
                putExtra("foodName", selectedFood.name)
                putExtra("foodImage", selectedFood.imageUri)
                putExtra("foodPrice", selectedFood.price)
                putExtra("foodDescription", selectedFood.description)
                putExtra("foodId", selectedFood.foodId)
                putExtra("USER_ID", userId)
            }

            startActivity(intent)
        }
        recyclerView.adapter = petFoodAdapter


        val categoryId = intent.getIntExtra("categoryId", -1)
        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        if (categoryId != -1) {
            Log.d("FoodListActivity", "Received Category ID: $categoryId")
            observeFoodByCategory(categoryId)
        }
//        else{
//            MotionToast.createColorToast(
//                this,
//                "Category Id not found",
//                "Error",
//                MotionToastStyle.ERROR,
//                MotionToast.GRAVITY_BOTTOM,
//                MotionToast.LONG_DURATION,
//                ResourcesCompat.getFont(this, R.font.font1))
//        }


        progressBarFoodList.visibility = View.GONE
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

                    viewModel.searchFoodByName(query).observe(this@FoodListActivity) { filteredList ->
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