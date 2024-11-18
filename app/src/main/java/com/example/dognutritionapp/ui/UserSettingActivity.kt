package com.example.dognutritionapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
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
import com.example.dognutritionapp.data.PetFoodDB
import com.example.dognutritionapp.data.User
import com.example.dognutritionapp.helper.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserSettingActivity : BaseActivity() {

    private lateinit var viewModel: PetFoodViewModel
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var buttonEdit: Button
    private lateinit var buttonConfirm: Button
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_setting)


        val repository = AppRepository(PetFoodDB.getDatabase(applicationContext))
        val factory = PetFoodViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(PetFoodViewModel::class.java)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextAddress = findViewById(R.id.editTextAddress)
        buttonEdit = findViewById(R.id.buttonEdit)
        buttonConfirm = findViewById(R.id.buttonConfirm)

        // Fetch user data and display it
        CoroutineScope(Dispatchers.Main).launch {
            val userId = 1
            currentUser = viewModel.getUserById(userId)
            currentUser?.let { user ->
                editTextName.setText(user.name)
                editTextEmail.setText(user.email)
                editTextAddress.setText(user.address)

                // Set the fields as non-editable initially
                setEditableFields(false)
            }
        }

        buttonEdit.setOnClickListener {
            setEditableFields(true)
            buttonEdit.visibility = View.GONE
            buttonConfirm.visibility = View.VISIBLE
        }

        buttonConfirm.setOnClickListener {
            currentUser?.let { user ->
                user.name = editTextName.text.toString().trim()
                user.email = editTextEmail.text.toString().trim()
                user.address = editTextAddress.text.toString().trim()

                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.updateUser(user)
                    Toast.makeText(this@UserSettingActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                    setEditableFields(false)
                    buttonEdit.visibility = View.VISIBLE
                    buttonConfirm.visibility = View.GONE
                }
            }
        }
    }

    private fun setEditableFields(editable: Boolean) {
        editTextName.isEnabled = editable
        editTextEmail.isEnabled = editable
        editTextAddress.isEnabled = editable
    }
}