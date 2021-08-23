package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentSponsoredAdDetailBinding
import com.sushmoyr.ajkalnewyork.models.core.ads.SponsoredAds
import kotlinx.coroutines.launch


class SponsoredAdDetailFragment : Fragment() {

    private var _binding: FragmentSponsoredAdDetailBinding?=null
    private val binding get() = _binding!!
    private val args: SponsoredAdDetailFragmentArgs by navArgs()
    private val viewModel: MainUserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSponsoredAdDetailBinding.inflate(inflater, container, false)

        val ad = args.ad
        println(ad)

        if(ad.paymentId!=null){
            binding.paymentStatus.text = resources.getString(R.string.payment_status_paid)
            binding.paymentStatus.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.adPaymentButton.visibility = View.GONE
            binding.adDeleteButton.visibility = View.GONE
        }else{
            binding.paymentStatus.text = resources.getString(R.string.payment_status_unpaid)
            binding.paymentStatus.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))
            binding.adPaymentButton.visibility = View.VISIBLE
            binding.adDeleteButton.visibility = View.VISIBLE
        }
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
            binding.adPaymentButton.visibility = View.GONE
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
        lifecycleScope.launch {
            Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
            val response = viewModel.deleteAd(ad)
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Ad deleted!!!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            else Toast.makeText(requireContext(), "Error! Try again!!", Toast.LENGTH_SHORT).show()
        }   
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