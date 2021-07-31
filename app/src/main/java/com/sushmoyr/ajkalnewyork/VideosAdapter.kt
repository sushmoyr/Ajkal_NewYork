package com.sushmoyr.ajkalnewyork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.databinding.VideoCardLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Video
import androidx.core.content.ContextCompat.startActivity

import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID

import com.google.android.youtube.player.YouTubeStandalonePlayer

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeThumbnailLoader
import com.google.android.youtube.player.YouTubeThumbnailView
import com.sushmoyr.ajkalnewyork.utils.Constants.YT_API_KEY


class VideosAdapter: RecyclerView.Adapter<VideosAdapter.MyViewHolder>() {

    private var items = emptyList<Video>()
    var itemClickListener : ((video: Video)->Unit)? = null

    class MyViewHolder(private val binding: VideoCardLayoutBinding) : RecyclerView.ViewHolder
        (binding.root) {
        var itemClickListener : ((video: Video)->Unit)? = null
        fun bind(video: Video) {
            binding.videoTitle.text = video.title
            binding.videoCategoryLayout.categoryNameText.text = video.categoryItem.categoryTitleBn

            /*Glide.with(binding.root.context)
                .load(video.thumbnail)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.videoThumbnail)*/

            binding.videoThumbnail.initialize(
                YT_API_KEY,
                object : YouTubeThumbnailView.OnInitializedListener{
                    override fun onInitializationSuccess(
                        view: YouTubeThumbnailView?,
                        loader: YouTubeThumbnailLoader?
                    ) {
                        loader?.setVideo(video.thumbnail)
                        loader?.setOnThumbnailLoadedListener(object : YouTubeThumbnailLoader
                        .OnThumbnailLoadedListener{
                            override fun onThumbnailLoaded(p0: YouTubeThumbnailView?, p1: String?) {
                                Log.d("youtube", "Thumbnail loaded")
                                loader.release()
                            }

                            override fun onThumbnailError(
                                p0: YouTubeThumbnailView?,
                                p1: YouTubeThumbnailLoader.ErrorReason?
                            ) {
                                Log.d("youtube", "Thumbnail error: ${p1?.name}")
                            }

                        })
                    }

                    override fun onInitializationFailure(
                        p0: YouTubeThumbnailView?,
                        p1: YouTubeInitializationResult?
                    ) {
                        Log.d("youtube", "Thumbnail Init failed")
                    }

                }
            )

            binding.root.setOnClickListener {
                //binding.root.findNavController().navigate(R.id .action_videosFragment_to_videosActivity)
                itemClickListener?.invoke(video)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(VideoCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(items: List<Video>){
        this.items = items
        notifyDataSetChanged()
    }
}