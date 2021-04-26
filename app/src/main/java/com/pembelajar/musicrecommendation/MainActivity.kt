package com.pembelajar.musicrecommendation

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.pembelajar.musicrecommendation.adapter.SongAdapter
import com.pembelajar.musicrecommendation.commons.Utilities
import com.pembelajar.musicrecommendation.databinding.ActivityMainBinding
import com.pembelajar.musicrecommendation.model.DataList
import com.pembelajar.musicrecommendation.viewmodel.RecommendationViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var songAdapter: SongAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(RecommendationViewModel::class.java)
        binding.bottomSheetLayout.rvResultRekom.layoutManager = LinearLayoutManager(this)
        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheetLayout)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        binding.inputUserId.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_GO){
                    getProcessRecom()
                    return true
                }
                return false
            }
        })
    }

    private fun getProcessRecom(){
        Utilities.hideKeyBoard(binding.inputUserId)
        binding.loadingAnimate.visibility = View.VISIBLE
        viewModel.sendingRequest(this, binding.inputUserId.text.toString(), binding.loadingAnimate, binding.inputUserId)
        viewModel.getSongRecommend().observe(this, Observer {
            songAdapter = SongAdapter(this, it.dataList)
            binding.bottomSheetLayout.rvResultRekom.adapter = songAdapter
            songAdapter.setOnClicked(object : SongAdapter.onItemCallback{
                override fun itemClickPlay(songId: DataList) {
                    val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                    intent.putExtra(PlayerActivity.SONG_ID, songId.songId)
                    intent.putExtra(PlayerActivity.SONG_NAMES, songId.songName)
                    startActivity(intent)
                }
            })
            binding.inputUserId.isEnabled = true
            binding.inputUserId.isClickable = true
            val bottomSheet = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheetLayout)
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
            bottomSheet.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        })
    }

}