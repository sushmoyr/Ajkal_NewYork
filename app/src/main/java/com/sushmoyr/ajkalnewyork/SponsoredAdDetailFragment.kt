package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.databinding.FragmentSponsoredAdDetailBinding
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds


class SponsoredAdDetailFragment : Fragment() {

    private var _binding: FragmentSponsoredAdDetailBinding?=null
    private val binding get() = _binding!!
    private val args: SponsoredAdDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSponsoredAdDetailBinding.inflate(inflater, container, false)

        val ad = args.ad
        val sizeData = args.size
        binding.ads = ad
        if(sizeData != null) {
            binding.adSize = sizeData
        }
        Glide.with(this)
            .load(ad.adImage)
            .into(binding.detailAdImage)

        binding.adUpdateButton.setOnClickListener{
            updateAdvertisement(ad)
        }
        binding.adDeleteButton.setOnClickListener{
            deleteAdvertisement(ad)
        }
        binding.adPaymentButton.setOnClickListener{
            makePayment(ad)
        }

        return binding.root
    }

    private fun makePayment(ad: SponsoredAds) {
        println(ad)
        val directions = SponsoredAdDetailFragmentDirections
            .actionSponsoredAdDetailFragmentToCreateAdvertisementActivity(ad)
        findNavController().navigate(directions)
    }

    private fun deleteAdvertisement(ad: SponsoredAds) {
        //TODO("Not yet implemented")
    }

    private fun updateAdvertisement(ad: SponsoredAds) {
        val directions = SponsoredAdDetailFragmentDirections
            .actionSponsoredAdDetailFragmentToEditAdvertisementFragment(ad)
        findNavController().navigate(directions)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}