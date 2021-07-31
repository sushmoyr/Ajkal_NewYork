package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.HighlightNewsLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.NewsItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel

sealed class HomeItemsViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    var itemClickListener: ((view: View, item: DataModel) -> Unit)? = null

    class HighlightedNewsViewHolder(private val binding: HighlightNewsLayoutBinding) :
        HomeItemsViewHolder
            (binding) {
        fun bind(news: DataModel.News, categoryList: List<Category>) {
            binding.newsHeadline.text = news.newsTitle
            if(categoryList.isNotEmpty()){
                binding.newsCategory.text = categoryList[(news.categoryId % 4)].categoryName
            }
            Glide.with(binding.root.context)
                .load(news.defaultImage)
                .override(binding.newsCover.width, binding.newsCover.height)
                .placeholder(R.drawable.ic_placeholder)
                .transform(RoundedCorners(48))
                .into(binding.newsCover)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, news)
            }
        }
    }

    class NormalNewsViewHolder(private val binding: NewsItemLayoutBinding) : HomeItemsViewHolder
        (binding) {
        fun bind(news: DataModel.News, categoryList: List<Category>) {
            binding.itemNewsHeadline.text = news.newsTitle
            if(categoryList.isNotEmpty()){
                binding.newsCategory.text = categoryList[(news.categoryId % 4)].categoryName
            }
            Glide.with(binding.root.context)
                .load(news.defaultImage)
                .placeholder(R.drawable.ic_placeholder)
                .transform(RoundedCorners(24))
                .into(binding.itemNewsCover)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, news)
            }

        }
    }

    class AdvertisementViewHolder(private val binding: AdvertisementLayoutBinding) :
        HomeItemsViewHolder(binding) {
        fun bind(advertisement: DataModel.Advertisement) {
            Glide.with(binding.root.context)
                .load(advertisement.imagePath)
                .into(binding.addImage)
            binding.sponsorHeader.text = advertisement.id

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, advertisement)
            }
        }
    }

}