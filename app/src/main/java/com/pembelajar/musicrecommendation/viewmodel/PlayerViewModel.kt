package com.pembelajar.musicrecommendation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pembelajar.musicrecommendation.commons.Config
import com.pembelajar.musicrecommendation.commons.OkHttpHelper
import com.pembelajar.musicrecommendation.commons.Utilities
import com.pembelajar.musicrecommendation.model.MetaDataSong
import com.pembelajar.musicrecommendation.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerViewModel: ViewModel() {
    private val TAG = PlayerViewModel::class.java.simpleName
    private var playerData: MutableLiveData<Player> = MutableLiveData()
    private var metaData : MutableLiveData<List<MetaDataSong>> = MutableLiveData()

    fun sendingRequestGetStream(songId : String, context: Context){
        val params: HashMap<String, String>? = hashMapOf(
            "bitRate" to "BR0096",
            "songId" to songId,
            "L_UID" to "28160464"
        )
        val form: HashMap<String, String?> = hashMapOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val url = Config.BASE_URL_NEW_STREAM
                    Log.i(TAG, form.toString())
                    val response = OkHttpHelper.executeGet(url, params, form, null)
                    val gson = Gson()
                    val songMapper = gson.fromJson(response, Player::class.java)
                    playerData.postValue(songMapper)
                }catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        Utilities.showErrorDialog(context, e)
                    }
                }
            }
        }
    }

    fun sengReqMetaData(songId: String, context: Context){
        val params: HashMap<String, String>? = hashMapOf(
            "songId" to songId,
        )
        val form: HashMap<String, String?> = hashMapOf()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try {
                    val url = Config.BASE_URL_META_DATA
                    Log.i(TAG, form.toString())
                    val response = OkHttpHelper.executeGet(url, params, form, null)
                    val gson = Gson()
                    val songMapper:List<MetaDataSong> = gson.fromJson(response, Array<MetaDataSong>::class.java).toList()
                    metaData.postValue(songMapper)
                    Log.i(TAG, songMapper.toString())
                }catch (e: Exception){
                    withContext(Dispatchers.Main) {
                        Utilities.showErrorDialog(context, e)
                    }
                }
            }
        }
    }

    fun getStreamData(): LiveData<Player> = playerData
    fun getMetaData(): LiveData<List<MetaDataSong>> = metaData
}