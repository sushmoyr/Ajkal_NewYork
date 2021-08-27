package com.sushmoyr.ajkalnewyork.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.sushmoyr.ajkalnewyork.activities.viewmodels.ArchiveViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModel
import com.sushmoyr.ajkalnewyork.activities.viewmodels.MainActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.databinding.ActivityArchiveBinding
import com.sushmoyr.ajkalnewyork.fragments.NewsAdapter
import com.sushmoyr.ajkalnewyork.repository.Repository
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.util.Log
import android.widget.Toast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialCalendar.newInstance
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class ArchiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArchiveBinding
    private val viewModel: ArchiveViewModel by viewModels()
    private val repository = Repository()
    private val mainViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModelFactory(repository.remoteDataSource)
    }
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArchiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.allCategories.observe(this, {
            if (it.isSuccessful) {
                newsAdapter.setCategoryList(it.body()!!)
            }
        })


        viewModel.archiveNews.observe(this, {

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
            this.layoutManager = LinearLayoutManager(this@ArchiveActivity)
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
            builder.show(supportFragmentManager, "datePicker")
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
            newsAdapter.setFilter(dateAsFormattedText)
        }

    }
}