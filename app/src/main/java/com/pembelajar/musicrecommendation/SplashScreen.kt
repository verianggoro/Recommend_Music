package com.pembelajar.musicrecommendation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import com.pembelajar.musicrecommendation.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            goHome()
        }, 750)
    }

    private fun goHome(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}