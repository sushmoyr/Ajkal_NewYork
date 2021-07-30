package com.sushmoyr.ajkalnewyork.activities

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.ActivityVideosBinding

class VideosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVideosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        configureVideoView()
    }

    private fun configureVideoView() {
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
    }
}