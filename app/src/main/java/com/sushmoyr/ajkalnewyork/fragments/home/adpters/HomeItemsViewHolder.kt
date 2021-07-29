package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.HighlightNewsLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel

sealed class HomeItemsViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class HighlightedNewsViewHolder(private val binding: HighlightNewsLayoutBinding): HomeItemsViewHolder
        (binding){
        fun bind(news: DataModel.News, categoryList : List<Category>){
            binding.newsHeadline.text = news.newsTitle
            binding.newsCategory.text = categoryList[(news.categoryId%4)].category_name
            Glide.with(binding.root.context)
                .load(news.defaultImage)
                .override(binding.newsCover.width, binding.newsCover.height)
                .transform(RoundedCorners(48))
                .into(binding.newsCover)

            categoryList.forEach {
                Log.d("recycler", it.toString())
            }
        }
    }

    class AdvertisementViewHolder(private val binding: AdvertisementLayoutBinding):
        HomeItemsViewHolder(binding){
            fun bind(advertisement: DataModel.Advertisement){
                Glide.with(binding.root.context)
                    .load(advertisement.imagePath)
                    .into(binding.addImage)
                binding.sponsorHeader.text = advertisement.id
            }
        }

}