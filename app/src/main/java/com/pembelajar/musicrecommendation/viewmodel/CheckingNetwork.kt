package com.pembelajar.musicrecommendation.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pembelajar.musicrecommendation.commons.Config
import com.pembelajar.musicrecommendation.commons.OkHttpHelper
import com.pembelajar.musicrecommendation.model.Detail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckingNetwork: ViewModel() {
    private val TAG = CheckingNetwork::class.java.simpleName
    private var testData : MutableLiveData<Detail> = MutableLiveData()

    fun sending(context: Context, activity: Activity){
        val params: HashMap<String, String>? = hashMapOf()
        val form: HashMap<String, String?> = hashMapOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val url = Config.BASE_URL+"test/connect"
                    Log.i(TAG, form.toString())
                    val response = OkHttpHelper.executeGet(url, params, form, null)
                    Log.i(TAG, response!!)
                    val gson = Gson()
                    val detail = gson.fromJson(response, Detail::class.java)
                    testData.postValue(detail)
                }catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Tidak dapat Masuk", Toast.LENGTH_SHORT).show()
                        activity.finish()
                    }
                }
            }
        }
    }

    fun getDetailTest():LiveData<Detail> = testData
}