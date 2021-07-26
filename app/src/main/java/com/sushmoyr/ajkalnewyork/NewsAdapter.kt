package com.sushmoyr.ajkalnewyork

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.databinding.NewsLayoutBinding
import com.sushmoyr.ajkalnewyork.models.News

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private var data = emptyList<News>()
    class MyViewHolder(private val binding: NewsLayoutBinding) : RecyclerView.ViewHolder(binding
        .root){
        fun bind(news: News){
            binding.newsCover.minimumHeight = 560
            binding.newsCover.maxHeight = 560
            Log.d("resizing", "width: ${binding.newsCover.width} && height: ${binding.newsCover
                .height} of cat = ${news.category.categoryTitleBn}")
            binding.newsHeadline.text = news.title
            binding.newsCategory.text = news.category.categoryTitleBn
            Glide.with(binding.root.context)
                .load(news.image)
                .override(binding.newsCover.width, binding.newsCover.height)
                .transform(RoundedCorners(48))
                .into(binding.newsCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(NewsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<News>){
        this.data = data
        notifyDataSetChanged()
    }
}