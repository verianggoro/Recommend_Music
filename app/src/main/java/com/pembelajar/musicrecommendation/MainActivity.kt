package com.pembelajar.musicrecommendation

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.pembelajar.musicrecommendation.adapter.SongAdapter
import com.pembelajar.musicrecommendation.commons.Utilities
import com.pembelajar.musicrecommendation.databinding.ActivityMainBinding
import com.pembelajar.musicrecommendation.fragment.LoadingFragment
import com.pembelajar.musicrecommendation.model.DataList
import com.pembelajar.musicrecommendation.viewmodel.RecommendationViewModel
import com.pembelajar.musicrecommendation.viewmodel.TestConnectionViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RecommendationViewModel
    private lateinit var viewModelTest: TestConnectionViewModel
    private lateinit var songAdapter: SongAdapter
    private lateinit var loadingFragment: LoadingFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(RecommendationViewModel::class.java)
        viewModelTest = ViewModelProvider(this).get(TestConnectionViewModel::class.java)
        binding.bottomSheetLayout.rvResultRekom.layoutManager = LinearLayoutManager(this)
        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheetLayout.bottomSheetLayout)
        bottomSheet.peekHeight = 150
        bottomSheet.isHideable = false
        loadingFragment = LoadingFragment.newInstance()
        binding.inputUserId.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                bottomSheet.peekHeight = 150
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    getProcessRecom()
                    return true
                }
                return false
            }
        })

        binding.btnTest.setOnClickListener { viewClick ->
            loadingFragment.show(supportFragmentManager, "Loading")
            viewModelTest.sending(this, supportFragmentManager)
            viewModelTest.getDetailTest().observe(this, Observer {
                val snackBar = Snackbar.make(
                    viewClick, it.detail!! + "|" + it.status,
                    Snackbar.LENGTH_LONG
                ).setAction("Action", null)
                snackBar.show()
            })
        }
    }

    private fun getProcessRecom() {
        Utilities.hideKeyBoard(binding.inputUserId)
        loadingFragment.show(supportFragmentManager, "Loading")
        viewModel.sendingRequest(this, binding.inputUserId.text.toString(), supportFragmentManager)
        viewModel.getSongRecommend().observe(this, Observer {
            if (it.dataList!!.isNotEmpty()){
                binding.bottomSheetLayout.containerEmptyRecom.visibility = View.GONE
                binding.bottomSheetLayout.containerResultMusic.visibility = View.VISIBLE
            }else{
                binding.bottomSheetLayout.containerEmptyRecom.visibility = View.VISIBLE
                binding.bottomSheetLayout.containerResultMusic.visibility = View.GONE
            }
            binding.bottomSheetLayout.txtScoreMae.text = String.format(resources.getString(R.string.place_score_mae), it.mae)
//            binding.bottomSheetLayout.txtScoreAvgPrecision.text = String.format(resources.getString(R.string.place_score_average_precision), it.avgPrecision)
            binding.bottomSheetLayout.txtScoreRmse.text = String.format(resources.getString(R.string.place_score_rmse), it.rmse)
            songAdapter = SongAdapter(this, it.dataList)
            binding.bottomSheetLayout.rvResultRekom.adapter = songAdapter
            songAdapter.setOnClicked(object : SongAdapter.onItemCallback {
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
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
            bottomSheet.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        })
    }

}