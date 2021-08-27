package com.sushmoyr.ajkalnewyork

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.ajkalnewyork.activities.viewmodels.ArchiveViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.databinding.FragmentArchiveBinding
import com.sushmoyr.ajkalnewyork.fragments.NewsAdapter
import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.repository.Repository
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ArchiveFragment : Fragment() {

    private var _binding : FragmentArchiveBinding?= null
    private val binding get() =  _binding!!

    private val viewModel: ArchiveViewModel by viewModels()
    private val repository = Repository()
    private val mainViewModel: MainActivityViewModel by activityViewModels {
        MainActivityViewModelFactory(repository.remoteDataSource)
    }
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentArchiveBinding.inflate(inflater, container, false)

        //Setup category spinner
        val spinner = binding.categorySpinner
        val categoryNames: MutableList<String> = mutableListOf()
        val categoryList: MutableList<Category> = mutableListOf()

        mainViewModel.allCategories.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                newsAdapter.setCategoryList(response.body()!!)
                categoryNames.clear()
                response.body()!!.forEach {
                    categoryNames.add(it.categoryName)
                    categoryList.add(it)
                }
                val spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line, categoryNames)
                spinner.setAdapter(spinnerAdapter)
            }
        })


        viewModel.archiveNews.observe(viewLifecycleOwner, {

            if (it != null) {
                newsAdapter.setData(it)
                println(it.size.toString())
            } else {
                println("Null")
            }
        })

        val rv = binding.archiveRv
        rv.apply {
            this.adapter = newsAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(
                    DateValidatorPointBackward.now())
                .build()
        val builder = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder).build()

        binding.dateSelector.setOnClickListener {
            builder.show(childFragmentManager, "datePicker")
        }

        builder.addOnPositiveButtonClickListener { currentSelectedDate ->
            val dateTime: LocalDateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(currentSelectedDate),
                ZoneId.systemDefault()
            )
            binding.dateSelector.setText(builder.headerText)
            val dateAsFormattedText: String =
                dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            binding.dateSelector.setText(builder.headerText)
            Log.d("filter", dateAsFormattedText)
            newsAdapter.setFilter(filterDate = dateAsFormattedText)
        }

        spinner.setOnItemClickListener { parent, view, position, id ->
            val catName = categoryList[position].id
            newsAdapter.setFilter(filterCatId = catName)
        }

        newsAdapter.itemClickListener = {
            val directions = ArchiveFragmentDirections.actionArchiveFragmentToNewsDetailsActivity(it)
            findNavController().navigate(directions)
        }

        binding.clearFilter.setOnClickListener {
            newsAdapter.clearFilter()
            binding.categorySpinner.text.clear()
            binding.dateSelector.setText(resources.getString(R.string.select_a_date))
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}