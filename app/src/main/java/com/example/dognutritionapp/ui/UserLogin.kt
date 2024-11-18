package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dognutritionapp.R
import com.example.dognutritionapp.Repositories.AppRepository
import com.example.dognutritionapp.ViewModel.PetFoodViewModel
import com.example.dognutritionapp.ViewModel.PetFoodViewModelFactory
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.helper.BaseActivity
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class UserLogin : BaseActivity() {

    private lateinit var viewModel: PetFoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        val repository = AppRepository(PetFoodDB.getDatabase(application))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)

        textViewRegister.setOnClickListener {
            startActivity(Intent(this, UserRegister::class.java))
        }

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                MotionToast.createColorToast(
                    this,
                    "Please fill all fields!",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))


                return@setOnClickListener
            }

            if (validateInputs(email, password)) {
                // Launch a coroutine to validate user credentials
                viewModel.viewModelScope.launch {
                    val user = viewModel.getUserByEmailAndPassword(email, password)

                    user?.let {
                        // Check user type and navigate to the appropriate page
                        val intent = if (user.userType == "Admin") {
                            MotionToast.createColorToast(
                                this@UserLogin,
                                "Admin Logged in successfully!",
                                "Success",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@UserLogin, R.font.font1))
                            Intent(this@UserLogin, AdminActivity::class.java)
                        } else {
                            MotionToast.createColorToast(
                                this@UserLogin,
                                "Customer Logged in successfully!",
                                "Success",
                                MotionToastStyle.SUCCESS,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@UserLogin, R.font.font1))
                            Intent(this@UserLogin, HomeActivity::class.java)
                        }

                        // Pass user ID to the next activity
                        intent.putExtra("USER_ID", user.userId)
                        startActivity(intent)
                        finish()
                    } ?: run {
                        MotionToast.createColorToast(
                            this@UserLogin,
                            "Invalid email or password!",
                            "Error",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@UserLogin, R.font.font1))
                    }
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                MotionToast.createColorToast(
                    this,
                    "Email is required!",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))

                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                MotionToast.createColorToast(
                    this,
                    "Enter a valid email!",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))
                false
            }
            password.isEmpty() -> {
                MotionToast.createColorToast(
                    this,
                    "Password is required",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))
                false
            }
            password.length < 6 -> {
                MotionToast.createColorToast(
                    this,
                    "Password should be at least 6 characters!",
                    "Warning",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, R.font.font1))
                false
            }
            else -> true
        }
    }
}