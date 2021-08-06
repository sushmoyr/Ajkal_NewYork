package com.sushmoyr.ajkalnewyork.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.NewsItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.News

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private var data = emptyList<News>()
    var itemClickListener: ((item: News) -> Unit)? = null

    class MyViewHolder(private val binding: NewsItemLayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        var itemClickListener: ((item: News) -> Unit)? = null
        fun bind(news: News) {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(news)
            }
            binding.itemNewsHeadline.text = news.description
            Glide.with(binding.root.context)
                .load(news.defaultImage)
                .into(binding.itemNewsCover)
            binding.newsCategory.text = binding.root.context.resources.getString(R.string.dummy_cat)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            NewsItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<News>) {
        this.data = data
        notifyDataSetChanged()
    }
}