package com.sushmoyr.ajkalnewyork.activities

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sushmoyr.ajkalnewyork.NewsAdapter
import com.sushmoyr.ajkalnewyork.R
import com.sushmoyr.ajkalnewyork.activities.viewmodels.NewsDetailActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.activities.viewmodels.NewsDetailViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityNewsDetailsBinding
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.NewsBodyLayoutBinding
import com.sushmoyr.ajkalnewyork.models.News
import com.sushmoyr.ajkalnewyork.repository.Repository
import com.sushmoyr.ajkalnewyork.utils.toNewsList

class NewsDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailsBinding
    private val args: NewsDetailsActivityArgs by navArgs()
    private lateinit var viewModel: NewsDetailViewModel
    private val newsAdapter: NewsAdapter by lazy {
        NewsAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val repository = Repository()
        viewModel = ViewModelProvider(this, NewsDetailActivityViewModelFactory(repository))
            .get(NewsDetailViewModel::class.java)


        viewModel.setDisplayNews(args.news)
        moreNewsRv()
        //updateUi(args.news)
        viewModel.displayNews.observe(this, {news->
            binding.newsDetailRootScroll.smoothScrollTo(0,0)
            updateUi(news)
            //
        })

        newsAdapter.itemClickListener = {
            Log.d("itemClick", it.newsTitle)
            viewModel.setDisplayNews(it)
        }

    }

    private fun updateUi(news: News){
        val newsBodyList = news.description.lines()
        binding.detailNewsTitle.text = news.newsTitle
        binding.newsDetailCat.categoryNameText.text = resources.getString(R.string.dummy_cat)
        binding.detailNewsWriter.text = news.createdBy
        binding.detailNewsTime.text = resources.getString(R.string.news_time)
        Glide.with(this)
            .load(news.defaultImage)
            .into(binding.detailNewsCover)

        val paragraphOffset = 2
        val adsOffset = 2
        var adcount = 0

        if(binding.detailNewsBodyLayout.childCount != 0)
            binding.detailNewsBodyLayout.removeAllViews()

        newsBodyList.forEach {
            if (it.isNotEmpty() && it.isNotBlank()) {
                val textView = NewsBodyLayoutBinding.inflate(LayoutInflater.from(this), null, false)
                textView.root.text = it
                binding.detailNewsBodyLayout.addView(textView.root)
            }
        }

        viewModel.advertisements.observe(this, {
            val ads = it.shuffled()
            val totalBlocks = newsBodyList.size
            for (i in 0..totalBlocks step paragraphOffset) {
                Log.d("newsBodyAds", "i = $i")
                val adsLayoutRoot = LinearLayout(this)
                adsLayoutRoot.orientation = LinearLayout.HORIZONTAL
                adsLayoutRoot.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
                adsLayoutRoot.gravity = Gravity.CENTER
                adsLayoutRoot.setPadding(8, 0, 8, 0)

                for (j in 0 until adsOffset) {
                    if (adcount < ads.size) {
                        val view = AdvertisementLayoutBinding.inflate(
                            LayoutInflater.from(this),
                            null, false
                        )

                        Glide.with(this)
                            .load(ads[adcount++].imagePath)
                            .into(view.addImage)
                        adsLayoutRoot.addView(view.root)
                        val space = Space(this)
                        space.layoutParams = ViewGroup.LayoutParams(24, MATCH_PARENT)
                        adsLayoutRoot.addView(space)
                    }
                }
                if(i !=0)
                    binding.detailNewsBodyLayout.addView(adsLayoutRoot, i)
            }
        })
    }

    private fun moreNewsRv(){

        val rv = binding.moreNewsRv
        rv.apply {
            layoutManager = LinearLayoutManager(this@NewsDetailsActivity)
            adapter = newsAdapter
        }

        viewModel.news.observe(this, {
            val newsItems = toNewsList(it.shuffled())
            newsAdapter.setData(newsItems)
        })

        newsAdapter.itemClickListener = {
            Log.d("itemClick", it.newsTitle)
            viewModel.setDisplayNews(it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }


}