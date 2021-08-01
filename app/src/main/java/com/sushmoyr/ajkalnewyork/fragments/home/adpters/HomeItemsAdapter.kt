package com.sushmoyr.ajkalnewyork.fragments.home.adpters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.GalleryPlaceholderLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.HighlightNewsLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.NewsItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel

class HomeItemsAdapter : RecyclerView.Adapter<HomeItemsViewHolder>(){

    var items = listOf<DataModel>()

    var categories = listOf<Category>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var itemClickListener: ((view: View, item: DataModel) -> Unit)? = null


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
            R.layout.gallery_placeholder_layout -> HomeItemsViewHolder.GalleryItemViewHolder(
                GalleryPlaceholderLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: HomeItemsViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        when (holder) {
            is HomeItemsViewHolder.AdvertisementViewHolder -> holder.bind(
                items[position] as DataModel.Advertisement
            )
            is HomeItemsViewHolder.HighlightedNewsViewHolder -> holder.bind(
                items[position] as DataModel.News,
                categories
            )
            is HomeItemsViewHolder.NormalNewsViewHolder -> holder.bind(
                items[position] as DataModel.News,
                categories
            )
            is HomeItemsViewHolder.GalleryItemViewHolder -> holder.bind(
                items[position] as DataModel.GalleryItem
            )
        }
    }

    override fun getItemCount(): Int {
        Log.d("adpterSize", "Adapter Size = ${items.size}")
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DataModel.Advertisement -> R.layout.advertisement_layout
            is DataModel.News -> when (position) {
                0 -> R.layout.highlight_news_layout
                else -> when (items[position - 1]) {
                    is DataModel.Advertisement -> R.layout.highlight_news_layout
                    else -> R.layout.news_item_layout
                }
            }
            is DataModel.GalleryItem -> R.layout.gallery_placeholder_layout
        }
    }

    fun setData(list: List<DataModel>){
        items = list
        notifyDataSetChanged()
    }

    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                var selectedCategoryId = -1
                categories.forEach { category ->
                    if(category.categoryName == charString)
                        selectedCategoryId = category.id
                }
                Log.d("viewmodel", "selected = $selectedCategoryId")


                filteredItems = if (charString.isEmpty()) items else {
                    val filteredList : MutableList<DataModel> = mutableListOf()
                    items
                        .filter {
                            (it is DataModel.News
                                    &&
                                    selectedCategoryId != -1
                                    && it.categoryId==selectedCategoryId) or (it is DataModel.Advertisement)
                        }
                        .forEach { filteredList.add(it) }
                    filteredList

                }
                Log.d("viewmodel", "filtered items size = ${filteredItems.size}")
                return FilterResults().apply { values = filteredItems }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filteredItems = if (results?.values == null)
                    ArrayList()
                else
                    results.values as List<DataModel>
                notifyDataSetChanged()
            }
        }
    }*/
}