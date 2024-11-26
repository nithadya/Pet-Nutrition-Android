package com.example.dognutritionapp.ui



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.dognutritionapp.R
import com.example.dognutritionapp.databinding.ActivityLoadingBinding
import com.example.dognutritionapp.helper.BaseActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


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


//        val animationView = findViewById<LottieAnimationView>(R.id.animation_view)
//        try {
//            animationView.setAnimation(R.raw.log1)
//            animationView.playAnimation()
//        } catch (e: Exception) {
//            Log.e("LottieError", "Failed to load animation", e)
//            MotionToast.createColorToast(
//                this,
//                "Failed to load animation",
//                "Error",
//                MotionToastStyle.ERROR,
//                MotionToast.GRAVITY_BOTTOM,
//                MotionToast.LONG_DURATION,
//                ResourcesCompat.getFont(this, R.font.font1))
//        }
    }
}