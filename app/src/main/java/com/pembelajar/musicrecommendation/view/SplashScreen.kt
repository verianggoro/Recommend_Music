package com.pembelajar.musicrecommendation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pembelajar.musicrecommendation.R
import com.pembelajar.musicrecommendation.databinding.ActivitySplashScreenBinding
import com.pembelajar.musicrecommendation.viewmodel.CheckingNetwork

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var viewModelTest: CheckingNetwork


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        viewModelTest = ViewModelProvider(this).get(CheckingNetwork::class.java)

        viewModelTest.sending(this, this)
        viewModelTest.getDetailTest().observe(this, Observer {
            if (it.status != null){
                goHome()
            }else{
                Toast.makeText(this, binding.root.resources.getString(R.string.msg_status), Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private fun goHome(){
        startActivity(Intent(this, MainActivity::class.java))
    }
}