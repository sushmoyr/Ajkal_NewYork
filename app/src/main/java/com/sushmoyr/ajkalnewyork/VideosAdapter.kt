package com.sushmoyr.ajkalnewyork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.databinding.VideoCardLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Video

class VideosAdapter: RecyclerView.Adapter<VideosAdapter.MyViewHolder>() {

    private var items = emptyList<Video>()

    class MyViewHolder(private val binding: VideoCardLayoutBinding) : RecyclerView.ViewHolder
        (binding.root) {
        fun bind(video: Video) {
            binding.videoTitle.text = video.title
            binding.videoCategoryLayout.categoryNameText.text = video.category.categoryTitleBn

            Glide.with(binding.root.context)
                .load(video.thumbnail)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(binding.videoThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(VideoCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
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