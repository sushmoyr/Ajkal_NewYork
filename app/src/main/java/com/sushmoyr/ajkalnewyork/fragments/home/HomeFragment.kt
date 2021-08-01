package com.sushmoyr.ajkalnewyork.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.sushmoyr.ajkalnewyork.NewsAdapter
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.DrawerViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModel
import com.sushmoyr.ajkalnewyork.databinding.FragmentHomeBinding
import com.sushmoyr.ajkalnewyork.fragments.home.adpters.HomeItemsAdapter
import com.sushmoyr.ajkalnewyork.fragments.home.viewmodel.HomeViewModel
import com.sushmoyr.ajkalnewyork.fragments.home.viewmodel.HomeViewModelFactory
import com.sushmoyr.ajkalnewyork.models.Category
import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.repository.Repository


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: Repository
    private val model: DrawerViewModel by activityViewModels()
    private val adapter: NewsAdapter by lazy {
        NewsAdapter()
    }
    private val homeAdapter: HomeItemsAdapter by lazy {
        HomeItemsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = Repository()
        val factory = HomeViewModelFactory(repository)
        viewModel = ViewModelProvider(requireActivity(), factory).get(HomeViewModel::class.java)
        viewModel.getHomeItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        setUpCategoryGroup()

        setUpRecyclerView()

        model.selectedCategory.observe(viewLifecycleOwner, { it ->
            Log.d("viewmodel", it)
            setCategoryFilter(it)

            var categoryId: Int? = null
            model.categoryListData.forEach { cat->
                if(cat.categoryName == it && cat.id!=0)
                    categoryId = cat.id
            }

            viewModel.getHomeItems(categoryId)
        })



        viewModel.getAllAds()

        viewModel.homeItems.observe(viewLifecycleOwner, {
            homeAdapter.setData(it)
        })


        //binding.imageView5.clipToOutline = true


        return binding.root
    }

    private fun setCategoryFilter(name: String){
        val chipGroup = binding.newsFilterChipGroup
        chipGroup.children.forEach { chip->
            if (chip is Chip){
                if(chip.text.equals(name)){
                    chipGroup.check(chip.id)
                    binding.horizontalScrollView.smoothScrollTo(
                        chip.left - chip.paddingLeft,
                        chip.top
                    )
                }
            }
        }
    }


    private fun setUpRecyclerView() {
        val rv = binding.newsRv
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
                    val directions = HomeFragmentDirections
                        .actionHomeFragmentToNewsDetailsActivity(item.toNews())
                    findNavController().navigate(directions)
                }
                is DataModel.GalleryItem -> {
                    findNavController().navigate(R.id.action_global_galleryFragment)
                    Toast.makeText(requireContext(), "Gallery", Toast.LENGTH_SHORT).show()
                }
                else -> Log.d("HomeFragment", "No click listeners added")
            }
        }



    }

    private fun setUpCategoryGroup() {
        //Get filters from api
        viewModel.getAllCats()
        viewModel.allCategory.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val filters = response.body()!!
                homeAdapter.categories = filters
                filters.forEach {
                    Log.d("json", it.toString())
                }
                setUpSelectionChips(filters)
            }
        })
    }

    private fun setUpSelectionChips(filters: List<Category>) {
        val chipGroup = binding.newsFilterChipGroup
        chipGroup.removeAllViews()
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it.categoryName
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
                        model.setValue(chip.text.toString())
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

    //Inflating Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.search_menu -> Toast.makeText(
                requireContext(),
                "Search Selected",
                Toast.LENGTH_SHORT
            ).show()
            R.id.info -> findNavController().navigate(R.id.action_global_infoActivity)
        }

        return super.onOptionsItemSelected(item)
    }

    //Menu Inflating

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}