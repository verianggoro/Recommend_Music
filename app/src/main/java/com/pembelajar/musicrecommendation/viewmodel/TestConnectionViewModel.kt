package com.pembelajar.musicrecommendation.viewmodel

import android.content.Context
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pembelajar.musicrecommendation.commons.Config
import com.pembelajar.musicrecommendation.commons.OkHttpHelper
import com.pembelajar.musicrecommendation.commons.Utilities
import com.pembelajar.musicrecommendation.model.Detail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestConnectionViewModel: ViewModel() {
    private val TAG = TestConnectionViewModel::class.java.simpleName
    private var testData : MutableLiveData<Detail> = MutableLiveData()

    fun sending(context: Context, loading: FragmentManager){
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
                    withContext(Dispatchers.Main){
                        loading.findFragmentByTag("Loading")?.let {
                            (it as DialogFragment).dismiss()
                        }
                    }
                }catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        Utilities.showErrorDialog(context, e)
                        loading.findFragmentByTag("Loading")?.let {
                            (it as DialogFragment).dismiss()
                        }
                    }
                }
            }
        }
    }

    fun getDetailTest():LiveData<Detail> = testData
}