package com.example.dognutritionapp.ui


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.adapters.BlogAdapter
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.helper.BaseActivity


class EduContentActivity : BaseActivity() {
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var blogProgressBar: ProgressBar
    private lateinit var viewModel: PetFoodViewModel
    private lateinit var blogAdapter:BlogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edu_content)

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        initializeBlog()


        val imageView = findViewById<ImageView>(R.id.addBlogBtnImg)
        imageView.setOnClickListener {
            val intent = Intent(this, AddContentActivity::class.java)
            startActivity(intent)
        }


        // Handle "Back" button click
        findViewById<ImageView>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }

    fun initializeBlog(){
        blogProgressBar=findViewById(R.id.progressBarBlog)
        blogRecyclerView = findViewById(R.id.viewBlog)
        blogProgressBar.visibility = View.VISIBLE

        viewModel.allContent.observe(this) { blog ->
            blogRecyclerView.layoutManager = LinearLayoutManager(this)
            blogAdapter = BlogAdapter(blog)
            blogRecyclerView.adapter = blogAdapter
            blogProgressBar.visibility = View.GONE
        }
    }
}