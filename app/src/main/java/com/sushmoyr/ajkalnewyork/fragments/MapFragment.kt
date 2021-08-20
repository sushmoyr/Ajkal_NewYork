package com.sushmoyr.ajkalnewyork.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.DrawerViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentMapBinding
import com.sushmoyr.ajkalnewyork.fragments.home.adpters.HomeItemsAdapter
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.District
import com.sushmoyr.ajkalnewyork.models.core.Division
import com.sushmoyr.ajkalnewyork.utils.ResourceState

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModels()
    private val itemAdapter: HomeItemsAdapter by lazy{
        HomeItemsAdapter()
    }
    private val model: DrawerViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        setUpDivisionGroup()
        viewModel.getBdNews()
        if(!model.categoryListData.isNullOrEmpty()){
            itemAdapter.categories = model.categoryListData
        }

        filterObservers()

        loadingState()

        setUpRecyclerView()

        viewModel.bdNews.observe(viewLifecycleOwner, {
            Log.d("size", "item size ${it.size}")
            itemAdapter.setData(it)
        })

        model.getCategoryList().observe(viewLifecycleOwner, {
            if(!it.isNullOrEmpty()){
                itemAdapter.categories = it
            }
        })


        return binding.root
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

    private fun filterObservers(){
        viewModel.selectedDivision.observe(viewLifecycleOwner, {
            if (it != null) {
                val selectedDivision = viewModel.getDivisionFromName(it)
                if (selectedDivision != null) {
                    viewModel.getDistrictByDivision(selectedDivision.id)
                }
                else{
                    Log.d("hider", "returned null query")
                }
            }
            else{
                viewModel.districts.value = null
            }

        })

        viewModel.selectionPair.observe(viewLifecycleOwner, {
            val division = it.first?:"null"
            val district = it.second?:"null"
            Log.d("pairs", "pair div = $division and pair dist = $district")
            viewModel.getBdNews(it.first, it.second)
        })



        viewModel.districts.observe(viewLifecycleOwner, {
            if (!it.isNullOrEmpty()) {
                it.forEach { dist ->
                    Log.d("hider", dist.districtName)
                }
                setUpDistrictGroup()
                binding.districtScroll.visibility = View.VISIBLE

            } else
                binding.districtScroll.visibility = View.GONE
        })
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

    private fun setUpDistrictGroup() {
        //Get filters from api
        viewModel.districts.observe(viewLifecycleOwner, { districts ->
            if(districts!=null){
                val filters = districts.toMutableList()
                val firstItem =
                    District(
                        "default_district", districtName = resources.getString(
                            R.string
                                .default_district
                        )
                    )
                filters.add(0, firstItem)

                setUpDistrictSelectionChips(filters)
            }
        })
    }

    private fun setUpDistrictSelectionChips(filters: List<District>) {
        val chipGroup = binding.districtGroup
        chipGroup.removeAllViews()
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it.districtName
            chipGroup.addView(chip)

            if (!isChecked) {
                chipGroup.check(chip.id)
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                isChecked = true
            }
        }

        chipGroup.isSingleSelection = true

        val placeHolderChip = Chip(requireContext())

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach { chip ->
                if (chip is Chip) {
                    if (chip.id == checkedId) {
                        chip.chipStrokeWidth = 0f
                        chip.setChipBackgroundColorResource(R.color.secondaryColor)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        if(chip.text.toString()!=resources.getString(R.string.default_district)){
                            viewModel.setSelectedDistrict(chip.text.toString())
                        }
                        else{
                            viewModel.setSelectedDistrict(null)
                        }
                    } else {
                        chip.chipBackgroundColor = placeHolderChip.chipBackgroundColor
                        chip.chipStrokeWidth = placeHolderChip.chipStrokeWidth
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
            val chip = view?.findViewById<Chip>(checkedId)
            if (chip != null) {
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
            }

        }
    }


    private fun setUpDivisionGroup() {
        //Get filters from api
        viewModel.divisions.observe(viewLifecycleOwner, { divisions ->
            val filters = divisions.toMutableList()
            val firstItem =
                Division(
                    "default_division",
                    divisionName = resources.getString(R.string.default_division)
                )
            filters.add(0, firstItem)

            setUpSelectionChips(filters)
        })
    }

    private fun setUpSelectionChips(filters: List<Division>) {
        val chipGroup = binding.divisionGroup
        chipGroup.removeAllViews()
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it.divisionName
            chipGroup.addView(chip)

            if (!isChecked) {
                chipGroup.check(chip.id)
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
                chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                isChecked = true
            }
        }

        chipGroup.isSingleSelection = true

        val placeHolderChip = Chip(requireContext())

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            group.children.forEach { chip ->
                if (chip is Chip) {
                    if (chip.id == checkedId) {
                        chip.chipStrokeWidth = 0f
                        chip.setChipBackgroundColorResource(R.color.secondaryColor)
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        if(chip.text.toString()!=resources.getString(R.string.default_division))
                            viewModel.setSelectedDivision(chip.text.toString())
                        else
                            viewModel.setSelectedDivision(null)
                    } else {
                        chip.chipBackgroundColor = placeHolderChip.chipBackgroundColor
                        chip.chipStrokeWidth = placeHolderChip.chipStrokeWidth
                        chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                    }
                }
            }
            val chip = view?.findViewById<Chip>(checkedId)
            if (chip != null) {
                chip.chipStrokeWidth = 0f
                chip.setChipBackgroundColorResource(R.color.secondaryColor)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}