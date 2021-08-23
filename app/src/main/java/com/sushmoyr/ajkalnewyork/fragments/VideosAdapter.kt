package com.sushmoyr.ajkalnewyork.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.databinding.VideoCardLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.Video

import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.google.android.youtube.player.*
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.utils.Constants.YT_API_KEY


class VideosAdapter: RecyclerView.Adapter<VideosAdapter.MyViewHolder>() {

    private var items = emptyList<Video>()
    var itemClickListener : ((video: Video)->Unit)? = null

    class MyViewHolder(private val binding: VideoCardLayoutBinding) : RecyclerView.ViewHolder
        (binding.root) {
        var itemClickListener : ((video: Video)->Unit)? = null
        fun bind(video: Video) {
            binding.videoTitle.text = video.videoTitle

            Glide.with(binding.root.context)
                .asBitmap()
                .load(video.image)
                .transition(BitmapTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.videoThumbnail)

            binding.root.setOnClickListener{
                itemClickListener?.invoke(video)
            }

            /*binding.videoThumbnail.initialize(
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
                                binding.root.setOnClickListener {
                                    //binding.root.findNavController().navigate(R.id .action_videosFragment_to_videosActivity)
                                    itemClickListener?.invoke(video)

                                }
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
                        Log.d("youtube", "Thumbnail Init failed ${p1?.name} - ${p1?.ordinal}")
                        if(p1?.ordinal == YouTubeInitializationResult.SERVICE_MISSING.ordinal){
                            Toast.makeText(binding.root.context, "Missing Youtube App",
                                Toast.LENGTH_SHORT).show()

                        }
                    }

                }
            )*/


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