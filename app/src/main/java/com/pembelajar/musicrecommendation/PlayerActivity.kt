package com.pembelajar.musicrecommendation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.*
import com.pembelajar.musicrecommendation.commons.Utilities
import com.pembelajar.musicrecommendation.databinding.ActivityPlayerBinding
import com.pembelajar.musicrecommendation.viewmodel.PlayerViewModel
import kotlinx.coroutines.*
import java.util.*


class PlayerActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel
    private lateinit var player: SimpleExoPlayer
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler

    companion object {
        const val SONG_ID = "song_id"
        const val SONG_NAMES = "song_names"
    }

    var songId: String? = null
    var songNames: String? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_player)

        if (intent.extras != null) {
            songId = intent.getStringExtra(SONG_ID)
            songNames = intent.getStringExtra(SONG_NAMES)
        }

        viewModel = ViewModelProvider(this).get(PlayerViewModel::class.java)
        binding.playbackIvPlay.setOnClickListener(this)
        binding.playbackIvPause.setOnClickListener(this)
        binding.playbackSbTimer.max = 100

        getMetaData()
        handler = Handler(Looper.getMainLooper())


    }

    override fun onStart() {
        super.onStart()
        player = SimpleExoPlayer.Builder(this).build()
        runOnUiThread {
            runnables.run()
        }
        if (Build.VERSION.SDK_INT >= 24) {
            getStreamingInitPlayer()
        }
    }


    override fun onResume() {
        super.onResume()
        if ((Build.VERSION.SDK_INT < 24 || player == null)) {
            getStreamingInitPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < 24) {
            releasePlayer();
        }
    }

    override fun onStop() {
        super.onStop()
        if (Build.VERSION.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null);
    }

    fun getMetaData() {
        Glide.with(this)
            .load("https://www.langitmusik.co.id/imageSong.do?songId=$songId")
            .error(R.drawable.placeholder_song)
            .into(binding.imgSong)
        viewModel.sengReqMetaData(songId!!, this)
        viewModel.getMetaData().observe(this, Observer {
            if (it.isNotEmpty()) {
                binding.txtPlayerSongName.text = it[0].songName
                binding.txtPlayerArtist.text = it[0].artistName
                binding.txtPlayerAlbum.text = it[0].albumName
            } else {
                binding.txtPlayerSongName.text = songNames
            }
        })
    }

    fun getStreamingInitPlayer() {
        viewModel.sendingRequestGetStream(songId!!, this)
        viewModel.getStreamData().observe(this, Observer {
            if (it != null) {
                val media: MediaItem = MediaItem.fromUri(it.streamUrl!!)
                player.setMediaItem(media)
                player.playWhenReady = playWhenReady
                player.seekTo(currentWindow, playbackPosition)
                player.prepare()

                player.addListener(object : Player.EventListener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playbackState === SimpleExoPlayer.STATE_READY) {
                            var milis = player.duration
                            binding.txtTotalTime.text = Utilities.milliSecondsToTimer(milis)
                            Log.i("DURATTTTION", Utilities.milliSecondsToTimer(milis))
                        }else if (playbackState === SimpleExoPlayer.STATE_ENDED){
                            binding.playbackIvPlay.visibility = View.VISIBLE
                            binding.playbackIvPause.visibility = View.GONE
                        }
                    }
                    override fun onPlayerError(error: ExoPlaybackException) {
                        when (error.type) {
                            ExoPlaybackException.TYPE_SOURCE -> Utilities.showErrorDialog(
                                this@PlayerActivity,
                                error
                            )
                            ExoPlaybackException.TYPE_UNEXPECTED -> Utilities.showErrorDialog(
                                this@PlayerActivity,
                                error
                            )
                        }
                    }
                })
                if (isPlaying()) {
                    binding.playbackIvPause.visibility = View.VISIBLE
                    binding.playbackIvPlay.visibility = View.GONE
                }
            }
        })
    }

    fun releasePlayer() {
        if (player != null) {
            playWhenReady = player.playWhenReady
            playbackPosition = player.currentPosition
            currentWindow = player.currentWindowIndex
            player.release()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playback_iv_play -> {
                startPlayer()
            }
            R.id.playback_iv_pause -> {
                pausePlayer()
            }
        }
    }

    private fun pausePlayer() {
        player.playWhenReady = false
        player.playbackState
        binding.playbackIvPlay.visibility = View.VISIBLE
        binding.playbackIvPause.visibility = View.GONE
    }

    private fun startPlayer() {
        player.playWhenReady = true
        player.playbackState
        binding.playbackIvPlay.visibility = View.GONE
        binding.playbackIvPause.visibility = View.VISIBLE
    }

    private fun isPlaying(): Boolean {
        return player != null && player.playbackState != Player.STATE_ENDED
                && player.playbackState != Player.STATE_IDLE
                && player.playWhenReady
    }

    private fun updateTime(){
        if (player != null) {
            val mCurrentPosition: Long = (player.currentPosition * 100)/player.duration
            Log.i("CUURRRENT", mCurrentPosition.toString())
            binding.playbackSbTimer.progress = mCurrentPosition.toInt()
            binding.txtCurrentTime.text = Utilities.milliSecondsToTimer(player.currentPosition)
        }
    }

    private val runnables = object : Runnable {
        override fun run() {
            updateTime()
            handler.postDelayed(this, 1000)
        }
    }
}