package com.sushmoyr.ajkalnewyork.fragments.trending

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentTrendingBinding
import com.sushmoyr.ajkalnewyork.fragments.home.adpters.HomeItemsAdapter
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.repository.Repository

class TrendingFragment : Fragment() {

    private var _binding: FragmentTrendingBinding? = null
    private val binding get() = _binding!!
    private val _repository by lazy {
        Repository()
    }
    private val repository get() = _repository.remoteDataSource
    private val homeAdapter: HomeItemsAdapter by lazy {
        HomeItemsAdapter()
    }

    private lateinit var viewModel: TrendingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTrendingBinding.inflate(inflater, container, false)
        val factory = TrendingViewModelFactory(_repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(TrendingViewModel::class.java)
        viewModel.getTrendingNews()

        setUpRecyclerView()

        viewModel.trendingItems.observe(viewLifecycleOwner, {
            homeAdapter.setData(it)
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        val rv = binding.trendingRv
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (homeAdapter.getItemViewType(position)) {
                    R.layout.advertisement_layout -> 1
                    else -> 2
                }
            }
        }
        rv.apply {
            adapter = homeAdapter
            setHasFixedSize(true)
            this.layoutManager = layoutManager
        }


        homeAdapter.itemClickListener = { view, item ->
            when(item){
                is DataModel.News -> {
                    val directions = TrendingFragmentDirections.actionTrendingFragmentToNewsDetailsActivity(item.toNews())
                    findNavController().navigate(directions)
                }
                else -> Log.d("HomeFragment", "No click listeners added")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}