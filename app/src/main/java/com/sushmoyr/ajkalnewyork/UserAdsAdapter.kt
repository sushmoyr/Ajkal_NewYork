package com.sushmoyr.ajkalnewyork

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.databinding.AdLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSizeItem
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds


class UserAdsAdapter: RecyclerView.Adapter<UserAdsAdapter.MyViewHolder>() {

    private val data: MutableList<SponsoredAds> = mutableListOf()
    private val sizeList: MutableList<AdvertisementSizeItem> = mutableListOf()

    var onClickListener:((sponsoredAd: SponsoredAds, sizeItem: AdvertisementSizeItem?)->Unit) ?=
        null

    inner class MyViewHolder(val binding: AdLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sponsoredAd: SponsoredAds) {
            val sizeItem = getSizeText(sponsoredAd.sizeId)
            binding.adAmount.text = sponsoredAd.amount
            binding.adSize.text = when(sizeItem) {
                null -> ""
                else -> "${sizeItem.name} W: ${sizeItem.width}, H: ${sizeItem.height}"
            }
            binding.adTitle.text = sponsoredAd.adTitle
            binding.adValidity.text = sponsoredAd.expDate
            Glide.with(binding.root)
                .load(sponsoredAd.adImage)
                .into(binding.imageView5)

            binding.root.setOnClickListener {
                onClickListener?.invoke(sponsoredAd, sizeItem)
            }
        }

        private fun getSizeText(sizeId: String): AdvertisementSizeItem? {

            return sizeList.find {
                it.id == sizeId
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AdLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<SponsoredAds>, lastFetchedAdSizeData: AdvertisementSize){
        data.clear()
        data.addAll(newData)
        sizeList.clear()
        sizeList.addAll(lastFetchedAdSizeData)
        notifyDataSetChanged()
    }

}