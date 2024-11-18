package com.example.dognutritionapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dognutritionapp.R
import com.example.dognutritionapp.databinding.ActivityLoadingBinding
import com.example.dognutritionapp.helper.BaseActivity

class LoadingActivity : BaseActivity() {

    private lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Access the start button using view binding directly
        binding.startBtn.setOnClickListener {
            startActivity(Intent(this@LoadingActivity, UserLogin::class.java))
        }
    }
}