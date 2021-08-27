package com.sushmoyr.ajkalnewyork.fragments

import android.util.Log
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
    private var filteredData = emptyList<News>()
    private var categoryList = emptyList<Category>()
    var date: String?= null
    var catId: String?= null
    var itemClickListener: ((item: News) -> Unit)? = null
    private var isFilterSet = false

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
        if(isFilterSet){
            holder.bind(filteredData[position], categoryList)
        } else
            holder.bind(data[position], categoryList)
    }

    override fun getItemCount(): Int {
        return when(isFilterSet){
            true -> filteredData.size
            else -> data.size
        }
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

    fun setFilter(filterDate: String? = null, filterCatId: String ?= null){
        isFilterSet = true
        if(filterCatId!=null)
            catId = filterCatId
        if(filterDate!= null)
            date = filterDate

        Log.d("filter", "Filter date: ${date ?: "null"} and Filter cat: ${catId?:"null"}")

        filteredData = data.filter {
            when{
                date!=null && catId==null -> {
                    it.createdAt.contains(date!!)
                }
                date==null && catId!=null -> {
                    it.categoryId == catId
                }
                date != null && catId != null -> {
                    it.categoryId == catId && it.createdAt.contains(date!!)
                }
                else -> {
                    true
                }
            }
        }
        notifyDataSetChanged()
    }

    fun clearFilter(){
        if(isFilterSet){
            date = null
            catId = null
            isFilterSet = false
            filteredData = emptyList()
            notifyDataSetChanged()
        }
    }
}