package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.User
import com.example.dognutritionapp.helper.BaseActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class UserRegister : BaseActivity() {

    private lateinit var viewModel: PetFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_register)

        val repository = AppRepository(PetFoodDB.getDatabase(application))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val editTextUserRole =findViewById<EditText>(R.id.editTextUserRole)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val textViewLogin = findViewById<TextView>(R.id.textViewLogin)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val address = editTextAddress.text.toString().trim()
            val userType = editTextUserRole.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || address.isEmpty()) {
                MotionToast.createColorToast(
                    this,
                    "Please fill all fields",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))
                return@setOnClickListener
            }

            // TODO: Add user type logic if needed

            val user = User(name = name, email = email, password = password, address = address, userType = userType)
            viewModel.insertUser(user)
            MotionToast.createColorToast(
                this,
                "User registered successfully!",
                "Success",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, R.font.font1))
            finish()
        }

        textViewLogin.setOnClickListener {
            startActivity(Intent(this, UserLogin::class.java))

        }
    }
}