package com.sushmoyr.ajkalnewyork.fragments.user.userad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainUserViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentUserAdBinding
import com.sushmoyr.ajkalnewyork.utils.getUserState


class UserAdFragment : Fragment() {

    private var _binding: FragmentUserAdBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainUserViewModel by activityViewModels()
    private val userAdsAdapter by lazy {
        UserAdsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserAdBinding.inflate(inflater, container, false)

        val user = getUserState(requireActivity()).user!!

        viewModel.getUserSponsoredAds(user.id.toString())

        binding.userAdRoot.setOnRefreshListener {
            viewModel.getUserSponsoredAds(user.id.toString())
        }

        viewModel.userAdLoader.observe(viewLifecycleOwner, {
            if (!it && binding.userAdRoot.isRefreshing)
                binding.userAdRoot.isRefreshing = false
        })

        viewModel.userSponsoredAds.observe(viewLifecycleOwner, { ads ->
            userAdsAdapter.setData(ads, viewModel.lastFetchedAdSizeData)
        })

        binding.yourAdRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdsAdapter
            setHasFixedSize(true)
        }

        userAdsAdapter.onClickListener = { sponsoredAd, sizeItem, makePayment ->
            if (makePayment) {
                val directions = UserAdFragmentDirections
                    .actionUserAdFragmentToCreateAdvertisementActivity(sponsoredAd)
                findNavController().navigate(directions)
            } else {
                val directions = UserAdFragmentDirections
                    .actionUserAdFragmentToSponsoredAdDetailFragment(sponsoredAd, sizeItem)
                findNavController().navigate(directions)
            }
        }

        return binding.root
    }


}