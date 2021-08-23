package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.AdLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSize
import com.sushmoyr.ajkalnewyork.models.core.ads.AdvertisementSizeItem
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds


class UserAdsAdapter: RecyclerView.Adapter<UserAdsAdapter.MyViewHolder>() {

    private val data: MutableList<SponsoredAds> = mutableListOf()
    private val sizeList: MutableList<AdvertisementSizeItem> = mutableListOf()

    var onClickListener:((sponsoredAd: SponsoredAds, sizeItem: AdvertisementSizeItem?,
                          makePayment:Boolean)->Unit) ?= null

    inner class MyViewHolder(val binding: AdLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(sponsoredAd: SponsoredAds) {
            val sizeItem = getSizeText(sponsoredAd.sizeId)
            val context = binding.root.context
            binding.adAmount.text = "$${sponsoredAd.amount}"
            binding.adSize.text = when(sizeItem) {
                null -> ""
                else -> "${sizeItem.name} W: ${sizeItem.width}, H: ${sizeItem.height}"
            }


            if(sponsoredAd.paymentId!=null){
                binding.adStatus.text = context.resources.getString(R.string.payment_status_paid)
                binding.adStatus.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.makePaymentBtn.visibility = View.GONE
            }else{
                binding.adStatus.text = context.resources.getString(R.string.payment_status_unpaid)
                binding.adStatus.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.makePaymentBtn.visibility = View.VISIBLE
            }

            binding.makePaymentBtn.setOnClickListener {
                onClickListener?.invoke(sponsoredAd, sizeItem, true)
            }


            binding.adTitle.text = sponsoredAd.adTitle
            binding.adValidity.text = sponsoredAd.expDate
            Glide.with(binding.root)
                .load(sponsoredAd.adImage)
                .into(binding.imageView5)

            binding.root.setOnClickListener {
                onClickListener?.invoke(sponsoredAd, sizeItem, false)
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