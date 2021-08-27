package com.sushmoyr.ajkalnewyork.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sushmoyr.ajkalnewyork.NetworkResponse
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.DrawerViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentMapBinding
import com.sushmoyr.ajkalnewyork.fragments.home.adpters.HomeItemsAdapter
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.locations.Country
import com.sushmoyr.ajkalnewyork.models.core.locations.District
import com.sushmoyr.ajkalnewyork.models.core.locations.Location
import com.sushmoyr.ajkalnewyork.utils.ResourceState

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by activityViewModels()
    private val itemAdapter: HomeItemsAdapter by lazy{
        HomeItemsAdapter()
    }

    private val model: DrawerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        //loadingState()

        setUpRecyclerView()
        binding.filterButton.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment_to_locationSelectionFragment)
        }

        viewModel.localNewsList.observe(viewLifecycleOwner, {
            Log.d("mapmodel", "data changed")
            when(it){
                is NetworkResponse.Error -> {
                    Log.d("maps", "Error ${it.message?:"null"}")
                    binding.mapProgressBar.visibility = View.INVISIBLE
                    binding.mapProgressText.visibility = View.VISIBLE
                    binding.mapProgressText.text = resources.getString(R.string.error_loading)
                }
                is NetworkResponse.Loading -> {
                    Log.d("maps", "Loading news")
                    binding.mapProgressBar.visibility = View.VISIBLE
                    binding.mapProgressText.visibility = View.VISIBLE
                    binding.mapProgressText.text = resources.getString(R.string.loading_msg)
                }
                is NetworkResponse.Success -> {
                    Log.d("maps", "Loading Success")
                    binding.mapProgressBar.visibility = View.INVISIBLE
                    itemAdapter.setData(toDataModelNewsList(it.response))

                    if(it.response.isNullOrEmpty()){
                        binding.mapProgressText.visibility = View.VISIBLE
                        binding.mapProgressText.text = resources.getString(R.string.no_data)
                    }
                    else{
                        binding.mapProgressText.visibility = View.INVISIBLE
                        binding.mapProgressText.text = resources.getString(R.string.succeeded)
                    }


                }
            }
        })

        model.getCategoryList().observe(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()){
                itemAdapter.categories = it
            }
        })
        return binding.root
    }

    private fun toDataModelNewsList(response: List<News>?): List<DataModel> {
        if(response==null)
            return emptyList()
        val data = mutableListOf<DataModel>()
        response.forEach {
            data.add(it.toDataModel())
        }
        return data.toList()
    }


    private fun setUpRecyclerView() {
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (itemAdapter.getItemViewType(position)) {
                    R.layout.advertisement_layout -> 1
                    else -> 2
                }
            }
        }
        binding.mapNewsRv.apply {
            adapter = itemAdapter
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
        }

        itemAdapter.itemClickListener = { view, item ->
            when (item) {
                is DataModel.News -> {
                    Log.d("news", item.toString())
                    val directions = MapFragmentDirections
                        .actionMapFragmentToNewsDetailsActivity(item.toNews())
                    findNavController().navigate(directions)
                }
                is DataModel.GalleryItem -> {
                    findNavController().navigate(R.id.action_global_galleryFragment)
                    Toast.makeText(requireContext(), "Gallery", Toast.LENGTH_SHORT).show()
                }
                is DataModel.Advertisement -> {
                    Log.d("datamodel", item.toString())
                }
            }
        }
    }

    private fun loadingState(){
        viewModel.loadingState.observe(viewLifecycleOwner, {
            when(it){
                is ResourceState.Loading -> {
                    when(it.isLoading){
                        true -> {
                            binding.mapProgressBar.visibility = View.VISIBLE
                            binding.mapProgressText.visibility = View.VISIBLE
                            binding.mapProgressText.text = resources.getString(R.string.please_wait)
                        }
                        false -> {
                            binding.mapProgressBar.visibility = View.INVISIBLE
                            binding.mapProgressText.text = resources.getString(R.string.succeeded)
                        }
                    }
                }
                is ResourceState.Result -> {
                    when(it.isSuccess){
                        true -> {
                            binding.mapProgressText.visibility = View.INVISIBLE
                            binding.mapProgressBar.visibility = View.INVISIBLE
                            binding.mapNewsRv.visibility = View.VISIBLE
                        }
                        false -> {
                            binding.mapProgressBar.visibility = View.INVISIBLE
                            binding.mapProgressText.visibility = View.VISIBLE
                            binding.mapNewsRv.visibility = View.GONE
                            binding.mapProgressText.text = it.msg
                        }
                    }
                }
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}


