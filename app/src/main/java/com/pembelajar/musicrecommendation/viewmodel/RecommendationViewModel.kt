package com.pembelajar.musicrecommendation.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.EditText
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
import com.pembelajar.musicrecommendation.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationViewModel: ViewModel() {
    private val TAG = RecommendationViewModel::class.java.simpleName
    private var recomm: MutableLiveData<Song> = MutableLiveData()

    fun sendingRequest(context: Context, userId: String, loading: FragmentManager){
        val params: HashMap<String, String>? = hashMapOf()
        val form: HashMap<String, String?> = hashMapOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val url = Config.BASE_URL+"train/pred/"+userId
                    Log.i(TAG, form.toString())
                    val response = OkHttpHelper.executeGet(url, params, form, null)
                    Log.i(TAG, response!!)
                    withContext(Dispatchers.Main){
                        loading.findFragmentByTag("Loading")?.let {
                            (it as DialogFragment).dismiss()
                        }
                    }
                    val gson = Gson()
                    val songMapper = gson.fromJson(response, Song::class.java)
                    recomm.postValue(songMapper)
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
    fun getSongRecommend(): LiveData<Song> = recomm
}