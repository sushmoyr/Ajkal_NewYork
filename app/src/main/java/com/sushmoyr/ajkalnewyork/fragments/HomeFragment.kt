package com.sushmoyr.ajkalnewyork.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sushmoyr.ajkalnewyork.NewsAdapter
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.databinding.FragmentHomeBinding
import com.sushmoyr.ajkalnewyork.models.News
import java.io.IOException


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val adapter: NewsAdapter by lazy {
        NewsAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setUpSelectionChips()

        setUpRecyclerView()

        val jsonFileString =
            context?.let { getJsonDataFromAsset(it.applicationContext, "sample_data.json") }
        if (jsonFileString != null) {
            val gson = Gson()
            val newsType = object : TypeToken<List<News>>() {}.type

            val allNews: List<News> = gson.fromJson(jsonFileString, newsType)

            allNews.forEach { news ->
                Log.d("news", news.title.toString())
                Log.d("news", news.category.categoryTitleBn)
                Log.d("news", news.image.toString())
            }

            adapter.setData(allNews)
        }



        //binding.imageView5.clipToOutline = true


        return binding.root
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun setUpRecyclerView() {
        val rv = binding.newsRv
        val lm = LinearLayoutManager(requireContext())
        rv.layoutManager = lm
        rv.adapter = adapter
    }

    private fun setUpSelectionChips() {
        val chipGroup = binding.newsFilterChipGroup
        val filters = resources.getStringArray(R.array.news_filters)
        var isChecked = false
        filters.forEach {
            val chip = Chip(requireContext())
            chip.id = View.generateViewId()
            chip.text = it
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

        when(item.itemId){
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