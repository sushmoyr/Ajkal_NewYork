package com.sushmoyr.ajkalnewyork.fragments.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
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
import com.sushmoyr.ajkalnewyork.databinding.FragmentHomeBinding
import com.sushmoyr.ajkalnewyork.fragments.home.adpters.HomeItemsAdapter
import com.sushmoyr.ajkalnewyork.fragments.home.viewmodel.HomeViewModel
import com.sushmoyr.ajkalnewyork.models.utility.DataModel
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.utils.blink
import com.sushmoyr.ajkalnewyork.utils.getUserState


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val model: DrawerViewModel by activityViewModels()
    private val homeAdapter: HomeItemsAdapter by lazy {
        HomeItemsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getHomeItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeAdapter.itemCountListener = { size ->
            if (size != 0) {
                binding.adapterStatus.visibility = View.GONE
            } else
                binding.adapterStatus.visibility = View.VISIBLE
        }

        binding.homeSwipeToRefreshLayout.setOnRefreshListener {
            val selectedCategory = model.selectedCategory.value
            var categoryId: String? = null
            if (selectedCategory != resources.getString(R.string.defaultCategoryName)) {
                model.categoryListData.forEach { cat ->
                    if (cat.categoryName == selectedCategory)
                        categoryId = cat.id
                }
            }

            viewModel.getHomeItems(categoryId, true)
            viewModel.getBreakingNews(refreshing = true)
        }

        viewModel.onDataLoadComplete = { breakingNewsLoaded, homeItemsLoaded ->
            Log.d("refresh", "$breakingNewsLoaded and $homeItemsLoaded")
            if (breakingNewsLoaded && homeItemsLoaded && binding.homeSwipeToRefreshLayout.isRefreshing) {
                binding.homeSwipeToRefreshLayout.isRefreshing = false
            }
        }

        setUpCategoryGroup()

        setUpRecyclerView()

        setUpBreakingNews()

        model.selectedCategory.observe(viewLifecycleOwner, {
            setCategoryFilter(it)

            Log.d("SelectedCat", it)

            var categoryId: String? = null
            if (it != resources.getString(R.string.defaultCategoryName)) {
                model.categoryListData.forEach { cat ->
                    if (cat.categoryName == it)
                        categoryId = cat.id
                }
            }

            viewModel.getHomeItems(categoryId, true)
        })

        viewModel.getAllAds()

        viewModel.homeItems.observe(viewLifecycleOwner, {
            homeAdapter.setData(it)

        })

        return binding.root
    }

    private fun setUpBreakingNews() {
        val factory = TextView(requireContext())
        factory.textSize = 16f
        factory.maxLines = 1
        factory.ellipsize = TextUtils.TruncateAt.END
        factory.textAlignment = TEXT_ALIGNMENT_CENTER
        binding.breakingNewsTitle.setFactory {
            TextView(requireContext()).apply {
                this.maxLines = 1
                this.ellipsize = TextUtils.TruncateAt.END
                this.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }
        val inAnim = AnimationUtils.loadAnimation(
            requireContext(),
            android.R.anim.slide_in_left
        )
        val outAnim = AnimationUtils.loadAnimation(
            requireContext(),
            android.R.anim.slide_out_right
        )
        inAnim.duration = 200
        outAnim.duration = 200
        binding.breakingNewsTitle.inAnimation = inAnim
        binding.breakingNewsTitle.outAnimation = outAnim

        viewModel.getBreakingNews()
        viewModel.breakingNewsObserve.observe(viewLifecycleOwner) { breakingNews ->
            if (breakingNews == null) {
                isVisibleBreakingNewsSection(false)
            } else {
                isVisibleBreakingNewsSection(true)
                binding.breakingNewsTitle.setText(breakingNews.bnewsTitle)

            }

            viewModel.onFinished = {
                val action = HomeFragmentDirections.actionHomeFragmentToNewsDetailsActivity(it)
                findNavController().navigate(action)
            }

            binding.breakingNewsTitle.setOnClickListener {
                viewModel.getNewsById(breakingNews?.newsId)
            }

        }
    }

    private fun isVisibleBreakingNewsSection(visible: Boolean) {
        val visibility = when (visible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        val blinkTime = when (visible) {
            true -> Animation.INFINITE
            false -> 0
        }

        binding.breakingNewsTitle.visibility = visibility
        binding.breakingNewsBlinker.visibility = visibility
        binding.breakingNewsBlinker.blink(blinkTime)
        binding.breakingNewsName.visibility = visibility

    }

    private fun setCategoryFilter(name: String) {
        val chipGroup = binding.newsFilterChipGroup
        chipGroup.children.forEach { chip ->
            if (chip is Chip) {
                if (chip.text.equals(name)) {
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
            this.layoutManager = layoutManager
        }

        homeAdapter.itemClickListener = { view, item ->
            when (item) {
                is DataModel.News -> {
                    Log.d("news", item.toString())
                    val directions = HomeFragmentDirections
                        .actionHomeFragmentToNewsDetailsActivity(item.toNews())
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

    private fun setUpCategoryGroup() {
        //Get filters from api
        viewModel.getAllCats()
        viewModel.allCategory.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val filters = response.body()!!.toMutableList()
                val firstItem =
                    Category("default", resources.getString(R.string.defaultCategoryName))
                filters.add(0, firstItem)
                homeAdapter.categories = filters

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
                        model.selectedCategoryFilter(chip.text.toString())
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
            R.id.search_menu -> findNavController().navigate(R.id.action_homeFragment_to_searchFragment)

            R.id.info -> findNavController().navigate(R.id.action_global_infoActivity)
            R.id.user_profile -> {
                val userState = getUserState(activity)
                when (userState.isLoggedIn) {
                    true -> findNavController().navigate(R.id.action_homeFragment_to_userActivity)
                    false -> findNavController().navigate(R.id.action_homeFragment_to_authActivity)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }


    //Menu Inflating


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}