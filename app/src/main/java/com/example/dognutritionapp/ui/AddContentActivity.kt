package com.example.dognutritionapp.ui

import com.example.dognutritionapp.R


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.data.EducationalContent
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.helper.BaseActivity
import com.google.android.material.textfield.TextInputEditText

class AddContentActivity : BaseActivity() {

    private lateinit var etTitle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var imagePreview: ImageView
    private lateinit var btnSelectImage: Button
    private lateinit var btnSubmit: Button
    private lateinit var viewModel: PetFoodViewModel
    private var selectedImageUri: Uri? = null

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_content)

        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        etTitle = findViewById(R.id.contentTitle)
        etDescription = findViewById(R.id.contentDescription)
        imagePreview = findViewById(R.id.imagePreview)
        btnSelectImage = findViewById(R.id.selectImageButton)
        btnSubmit = findViewById(R.id.addContentButton)

        btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        btnSubmit.setOnClickListener {
            saveContent()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            Glide.with(this)
                .load(selectedImageUri)
                .centerCrop()
                .into(imagePreview)
        }
    }

    private fun saveContent() {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val imageUrl = selectedImageUri.toString()

        if (title.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
            return
        }

        val content = EducationalContent(
            title = title,
            description = description,
            contentUrl = imageUrl
        )
        // Insert content into the database
        viewModel.insertContent(content)
        // Display a success toast message
        Toast.makeText(this, "Content Added Successfully", Toast.LENGTH_SHORT).show()
        finish() // Close the activity after successful submission
    }
}
