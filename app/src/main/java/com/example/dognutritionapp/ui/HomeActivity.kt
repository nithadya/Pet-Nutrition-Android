package com.example.dognutritionapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.dognutritionapp.R
import com.example.dognutritionapp.adapters.SliderAdapter
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import androidx.viewpager2.widget.CompositePageTransformer
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.adapters.CategoryAdapter
import com.example.dognutritionapp.adapters.PetFoodListAdapter
import com.example.dognutritionapp.data.Category
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.PetFoods
import com.example.dognutritionapp.data.User
import com.example.dognutritionapp.helper.BaseActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class HomeActivity : BaseActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var dotsIndicator: DotsIndicator// Declare DotsIndicator
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var viewModel: PetFoodViewModel
    private lateinit var progressBarCategory: ProgressBar
    private lateinit var progressBarPetFood: ProgressBar
    private lateinit var petFoodAdapter: PetFoodListAdapter
    private lateinit var PetFoodRecyclerView: RecyclerView
    private val REQUEST_READ_STORAGE = 101
    private var userId: Int = -1

    private val bannerList = listOf(
        R.drawable.banner3,
        R.drawable.banner2,

    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progressBarCategory = findViewById(R.id.progressBarCategory)


        // Get user ID from intent extras
        userId = intent.getIntExtra("USER_ID", -1)

        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        // Check for permission at runtime
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if it has not been granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_STORAGE
            )
        }

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)



//Initialize  Banners
        initializeBanners()
//Initialize Food Category
        initializeCategory()

        initializePetFood()

        //Navigation for bottom bar navigation
        navigationPage()

    }


    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_STORAGE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, proceed with loading images
            } else {
                // Permission denied; notify the user
                MotionToast.createColorToast(
                    this,
                    "Storage permission is required to display images",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))
            }
        }
    }



    private fun initializePetFood(){
        progressBarPetFood=findViewById(R.id.progressBarFood)
        PetFoodRecyclerView = findViewById(R.id.viewFoods)
        progressBarPetFood.visibility = View.VISIBLE


        viewModel.allFood.observe(this) { petfood ->
            PetFoodRecyclerView.layoutManager = GridLayoutManager(this,2)
            petFoodAdapter = PetFoodListAdapter(petfood) { selectedItem: PetFoods ->
                // Create an Intent to start FoodDetailActivity
                val intent = Intent(this, FoodDetailActivity::class.java).apply {
                    putExtra("foodName", selectedItem.name)
                    putExtra("foodImage", selectedItem.imageUri)
                    putExtra("foodPrice", selectedItem.price)
                    putExtra("foodDescription", selectedItem.description)
                    putExtra("foodId", selectedItem.foodId)
                    putExtra("userId", userId)

                }
                startActivity(intent)

            }
            PetFoodRecyclerView.adapter = petFoodAdapter
            progressBarPetFood.visibility = View.GONE
        }

    }

    private fun initializeCategory(){
        progressBarCategory=findViewById(R.id.progressBarCategory)
        recyclerView = findViewById(R.id.viewCategories)
        progressBarCategory.visibility = View.VISIBLE


        viewModel.allCategories.observe(this) { category ->
            recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL,false)
            // Set up the adapter with the fetched Category data
            categoryAdapter = CategoryAdapter(category) { selectedItem: Category ->
                intent.putExtra("categoryId", selectedItem.categoryId)
                val intent = Intent(this, FoodListActivity::class.java)
                startActivity(intent)
            }
            recyclerView.adapter = categoryAdapter
            progressBarCategory.visibility = View.GONE
        }

    }

    private fun initializeBanners(){
        // Initialize ViewPager
        viewPager = findViewById(R.id.viewPagerBanner)
        viewPager.adapter = SliderAdapter(bannerList)
        viewPager.clipToPadding=false
        viewPager.clipChildren=false
        viewPager.offscreenPageLimit=3
        viewPager.getChildAt(0).overScrollMode=RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer=CompositePageTransformer().apply{
            addTransformer(MarginPageTransformer(40))
        }
        viewPager.setPageTransformer(compositePageTransformer)

      // Initialize Dots Indicator
        dotsIndicator = findViewById(R.id.dotIndicator)
        dotsIndicator.setViewPager2(viewPager)

        // Only modify progressBarCategory if initialized
        if (::progressBarCategory.isInitialized) {
            progressBarCategory.visibility = View.GONE
        } else {
            Toast.makeText(this, "progressBarCategory is not initialized!", Toast.LENGTH_SHORT).show()
        }

        // Optional: Implement auto-scrolling and dot indicator functionality here
        setupAutoScroll()
        // setupDotsIndicator()
    }

    private fun setupAutoScroll() {

        viewPager.postDelayed(object : Runnable {
            override fun run() {
                val nextItem = (viewPager.currentItem + 1) % viewPager.adapter!!.itemCount
                viewPager.setCurrentItem(nextItem, true)
                viewPager.postDelayed(this, 5000) // Scroll every 3 seconds
            }
        }, 5000)
    }

    private fun navigationPage(){
        val listNavBtn = findViewById<LinearLayout>(R.id.nav1)
        val cartNavBtn = findViewById<LinearLayout>(R.id.nav2)
        val blogNavBtn = findViewById<LinearLayout>(R.id.nav3)
        val orderNavBtn = findViewById<LinearLayout>(R.id.nav4)
        val profileNavBtn = findViewById<LinearLayout>(R.id.nav5)


        blogNavBtn.setOnClickListener {
            val intent = Intent(this, EduContentActivity::class.java)
            startActivity(intent)
        }

        cartNavBtn.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java).apply {
                putExtra("userId", userId)
            }
            startActivity(intent)
        }


        listNavBtn.setOnClickListener {
            val intent = Intent(this, FoodListActivity::class.java)
            startActivity(intent)
        }


        orderNavBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        profileNavBtn.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
        }
    }

}