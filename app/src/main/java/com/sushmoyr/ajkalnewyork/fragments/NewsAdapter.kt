package com.sushmoyr.ajkalnewyork.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.sushmoyr.ajkalnewyork.databinding.NewsItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.utils.Constants.MAXIMUM_MORE_NEWS_COUNT

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private var data = emptyList<News>()
    private var categoryList = emptyList<Category>()
    var itemClickListener: ((item: News) -> Unit)? = null

    class MyViewHolder(private val binding: NewsItemLayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        var itemClickListener: ((item: News) -> Unit)? = null

        fun bind(news: News, categoryList: List<Category>) {
            binding.root.setOnClickListener {
                itemClickListener?.invoke(news)
            }
            binding.itemNewsHeadline.text = news.newsTitle
            val category = categoryList.find {
                it.id == news.categoryId
            }
            if(category!=null){
                binding.newsCategory.text = category.categoryName
            }
            else {
                binding.newsCategory.visibility = View.GONE
            }

            Glide.with(binding.root.context)
                .asBitmap()
                .load(news.defaultImage)
                .transition(BitmapTransitionOptions.withCrossFade())
                .into(binding.itemNewsCover)

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
        holder.bind(data[position], categoryList)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(data: List<News>) {
        this.data = when {
            data.size > MAXIMUM_MORE_NEWS_COUNT -> data.take(MAXIMUM_MORE_NEWS_COUNT)
            else -> data
        }
        notifyDataSetChanged()
    }

    fun shuffle(){
        data = data.shuffled()
        notifyDataSetChanged()
    }

    fun setCategoryList(it: List<Category>) {
        this.categoryList = it
    }
}