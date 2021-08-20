package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.*
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.utility.DataModel

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
                .asBitmap()
                .load(news.defaultImage)
                .transition(BitmapTransitionOptions.withCrossFade())
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
                .asBitmap()
                .load(news.defaultImage)
                .transition(BitmapTransitionOptions.withCrossFade())
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
                .asBitmap()
                .load(advertisement.image)
                .transition(BitmapTransitionOptions.withCrossFade())
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
                for (i in 0 until 3){
                    if(i < imageData.size){
                        when(i){
                            0 -> {
                                binding.mainImageCaption.text = imageData[0].photoTitle
                                loadImageIntoView(binding.root.context, imageData[0].imagePath, binding.mainImage)
                            }
                            1 -> {
                                binding.leftImageCaption.text = imageData[1].photoTitle
                                loadImageIntoView(binding.root.context, imageData[1].imagePath, binding.leftImage)
                            }
                            2 -> {
                                binding.rightImageCaption.text = imageData[2].photoTitle
                                loadImageIntoView(binding.root.context, imageData[2].imagePath, binding.rightImage)
                            }
                        }
                    }
                }

                binding.root.setOnClickListener{
                    itemClickListener?.invoke(it, photo)
                }
            }

            private fun loadImageIntoView(context: Context, image:String, target: ImageView) {
                Glide.with(context)
                    .asBitmap()
                    .load(image)
                    .transition(BitmapTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(target)
            }
        }

}