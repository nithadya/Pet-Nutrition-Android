package com.example.dognutritionapp.helper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.dognutritionapp.R
import com.example.dognutritionapp.ui.LoadingActivity


// This class managed the device status bar
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set flags for edge-to-edge display
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        // Enable edge-to-edge content
        enableEdgeToEdge()

        // Set the status bar color to purple
        window.statusBarColor = Color.parseColor("white") // Purple color

        // Set the system UI visibility to show the navigation bar
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

                )

        // Adjust the window insets to avoid overlapping with the notch and navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply padding to avoid overlap with system bars
            view.setPadding(0, systemInsets.top, 0, systemInsets.bottom)
            WindowInsetsCompat.CONSUMED



        }

    }
}
