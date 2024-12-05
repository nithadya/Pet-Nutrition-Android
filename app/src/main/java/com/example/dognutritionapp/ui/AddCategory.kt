package com.example.dognutritionapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.data.Category
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.PetFoods
import com.example.dognutritionapp.helper.BaseActivity

class AddCategory : BaseActivity() {

    private lateinit var imageViewCategory: ImageView
    private lateinit var viewModel: PetFoodViewModel
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        // Initialize UI components
        imageViewCategory = findViewById(R.id.imageViewCategory)
        val categoryName: EditText = findViewById(R.id.categoryName)
        val addImageButton: Button = findViewById(R.id.button_add_category_image)
        val addCategoryButton: Button = findViewById(R.id.button_add_category)

        addImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        addCategoryButton.setOnClickListener {
            val name = categoryName.text.toString()


            if (name.isNotEmpty() && imageUri != null) {
                val categoryItem = Category(
                    name = name,
                    imageUri = imageUri.toString()
                )
                viewModel.insertCategory(categoryItem)
                finish() // Close activity after adding
            } else {


                Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data // Get the image URI
            imageViewCategory.setImageURI(imageUri) // Display the selected image in the ImageView
        }
    }

    companion object {
        const val REQUEST_IMAGE_PICK = 1001
    }
}