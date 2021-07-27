package com.pembelajar.musicrecommendation.view

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pembelajar.musicrecommendation.BuildConfig
import com.pembelajar.musicrecommendation.R
import com.pembelajar.musicrecommendation.databinding.ActivitySplashScreenBinding
import com.pembelajar.musicrecommendation.viewmodel.CheckingNetwork

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModelTest: CheckingNetwork


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        setupVersionTxt()
        viewModelTest = ViewModelProvider(this).get(CheckingNetwork::class.java)


        viewModelTest.sending(this, this)
        viewModelTest.getDetailTest().observe(this, Observer {
            if (it.status != null) {
                goHome()
            } else {
                Toast.makeText(
                    this,
                    binding.root.resources.getString(R.string.msg_status),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
    }

    private fun goHome(){
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun setupVersionTxt(){
        val configuration = Configuration()
        val currentNightMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.versionTxt.setTextColor(ContextCompat.getColor(this,R.color.black))
                Log.i("SINI TERANG", "TERANG")
            } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.versionTxt.setTextColor(ContextCompat.getColor(this,R.color.white))
            } // Night mode is active, we're using dark theme
        }
        binding.versionTxt.text = String.format(
            binding.root.resources.getString(R.string.txt_version),
            BuildConfig.VERSION_NAME
        )
    }
}