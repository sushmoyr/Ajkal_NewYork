package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.*
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
                val category = categoryList.find { category -> category.id == news.categoryId }

                if (category != null) {
                    binding.newsCategory.text = category.categoryName
                }
            }
            Glide.with(binding.root.context)
                .load(news.defaultImage)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_placeholder)
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
                val category = categoryList.find { category -> category.id == news.categoryId }

                if (category != null) {
                    binding.newsCategory.text = category.categoryName
                }
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
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.addImage)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, advertisement)
            }
        }
    }

    class GalleryItemViewHolder(private val binding: GalleryPlaceholderLayoutBinding) :
        HomeItemsViewHolder(binding) {
            fun bind(photo: DataModel.GalleryItem){
                val imageData = photo.images.shuffled()
                binding.mainImageCaption.text = imageData[0].caption
                binding.leftImageCaption.text = imageData[1].caption
                binding.rightImageCaption.text = imageData[2].caption

                loadImageIntoView(binding.root.context, imageData[0].imagePath, binding.mainImage)
                loadImageIntoView(binding.root.context, imageData[1].imagePath, binding.leftImage)
                loadImageIntoView(binding.root.context, imageData[2].imagePath, binding.rightImage)

                binding.root.setOnClickListener{
                    itemClickListener?.invoke(it, photo)
                }
            }

            private fun loadImageIntoView(context: Context, image:String, target: ImageView) {
                Glide.with(context)
                    .load(image)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(target)
            }
        }

}