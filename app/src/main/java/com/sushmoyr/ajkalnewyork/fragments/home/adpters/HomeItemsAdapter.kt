package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.HighlightNewsLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.NewsItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel

class HomeItemsAdapter : RecyclerView.Adapter<HomeItemsViewHolder>() {

    var items = listOf<DataModel>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var categories = listOf<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemsViewHolder {
        return when (viewType) {
            R.layout.advertisement_layout -> HomeItemsViewHolder.AdvertisementViewHolder(
                AdvertisementLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.highlight_news_layout -> HomeItemsViewHolder.HighlightedNewsViewHolder(
                HighlightNewsLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            R.layout.news_item_layout -> HomeItemsViewHolder.NormalNewsViewHolder(
                NewsItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: HomeItemsViewHolder, position: Int) {
        when (holder) {
            is HomeItemsViewHolder.AdvertisementViewHolder -> holder.bind(items[position] as DataModel.Advertisement)
            is HomeItemsViewHolder.HighlightedNewsViewHolder -> holder.bind(
                items[position] as DataModel.News,
                categories
            )
            is HomeItemsViewHolder.NormalNewsViewHolder -> holder.bind(
                items[position] as DataModel.News,
                categories
            )
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DataModel.Advertisement -> R.layout.advertisement_layout
            is DataModel.News -> when(position){
                0 -> R.layout.highlight_news_layout
                else -> when(items[position-1]){
                    is DataModel.Advertisement -> R.layout.highlight_news_layout
                    else -> R.layout.news_item_layout
                }
            }
        }
    }
}