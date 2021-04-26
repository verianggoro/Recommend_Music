package com.pembelajar.musicrecommendation

import android.content.Context
import androidx.multidex.MultiDexApplication

class RekomendasiApp: MultiDexApplication() {
    companion object{
        lateinit var instance: RekomendasiApp private set
        fun getInstance(): Context?{
            return instance
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}