package com.sushmoyr.ajkalnewyork.fragments.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.GalleryItemLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.Photo

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.MyViewHolder>() {

    private var imageData = emptyList<Photo>()

    class MyViewHolder(val binding: GalleryItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: Photo) {
            Glide.with(binding.root)
                .load(photo.imagePath)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.galleryPhoto)
            binding.galleryPhotoCaption.text = photo.photoTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            GalleryItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(imageData[position])
    }

    override fun getItemCount(): Int {
        return imageData.size
    }

    fun setData(data: List<Photo>) {
        imageData = data
        notifyDataSetChanged()
    }
}