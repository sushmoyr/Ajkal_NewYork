package com.sushmoyr.ajkalnewyork.activities

import android.os.Bundle
import android.util.Log
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.sushmoyr.ajkalnewyork.databinding.ActivityVideosBinding

class VideosActivity : YouTubeBaseActivity() {

    private val apiKey="AIzaSyC_tmJXWNzL7-ALsk5vb3MQjsc-HsYI4GI"
    private lateinit var binding: ActivityVideosBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get reference to the view of Video player
        val ytPlayer = binding.ytPlayer

        ytPlayer.initialize(apiKey, object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo("wdHeiVf0mds")
                player?.play()
                player?.setOnFullscreenListener {
                    Log.d("youtube", "fullscreen = $it")
                }

            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                if (p1 != null) {
                    Log.d("youtube", p1.name)
                    p1.getErrorDialog(this@VideosActivity, 1)
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("youtube", "activity destroyed")
    }

    /*private fun configureVideoView() {
        val uri = "http://server1.dhakamovie.com/TV_Series/Animation%20TV%20Shows/Erased/s1/erased.2017.s01e01.720p.web-dl.dd5.1.x265.hevc.mp4"
        binding.videoView.setVideoPath(uri)


        binding.videoView.setOnPreparedListener {
            it.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            it.setOnVideoSizeChangedListener { mp, width, height ->
                val mediaController = MediaController(this)
                //mediaController.setMediaPlayer(binding.videoView)
                binding.videoView.setMediaController(mediaController)
                mediaController.setAnchorView(binding.videoView)
            }
        }

        binding.videoView.start()
    }*/
}