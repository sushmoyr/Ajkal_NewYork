package com.sushmoyr.ajkalnewyork.diffutils

import androidx.recyclerview.widget.DiffUtil
import com.sushmoyr.ajkalnewyork.models.utility.DataModel

class HomeItemsDiffUtil(
    private val oldItems: List<DataModel>,
    private val newItems: List<DataModel>
    ) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return when {
            oldItem is DataModel.News && newItem is DataModel.News -> {
                oldItem.id == newItem.id
            }

            oldItem is DataModel.GalleryItem && newItem is DataModel.GalleryItem -> {
                oldItem.images == newItem.images
            }

            oldItem is DataModel.Advertisement && newItem is DataModel.Advertisement -> {
                oldItem.id == newItem.id
            }
            else -> false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return when {
            oldItem is DataModel.News && newItem is DataModel.News -> {
                oldItem == newItem
            }

            oldItem is DataModel.GalleryItem && newItem is DataModel.GalleryItem -> {
                oldItem == newItem
            }

            oldItem is DataModel.Advertisement && newItem is DataModel.Advertisement -> {
                oldItem == newItem
            }
            else -> false
        }
    }
}