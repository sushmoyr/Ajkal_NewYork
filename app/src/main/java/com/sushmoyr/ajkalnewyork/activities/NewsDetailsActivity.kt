package com.sushmoyr.ajkalnewyork.activities

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.sushmoyr.ajkalnewyork.fragments.NewsAdapter
import com.sushmoyr.ajkalnewyork.activities.viewmodels.NewsDetailActivityViewModelFactory
import com.sushmoyr.ajkalnewyork.activities.viewmodels.NewsDetailViewModel
import com.sushmoyr.ajkalnewyork.databinding.ActivityNewsDetailsBinding
import com.sushmoyr.ajkalnewyork.databinding.AdvertisementLayoutBinding
import com.sushmoyr.ajkalnewyork.databinding.NewsBodyLayoutBinding
import com.sushmoyr.ajkalnewyork.models.core.News
import com.sushmoyr.ajkalnewyork.repository.RemoteDataSource
import com.sushmoyr.ajkalnewyork.utils.toNewsList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        val repository = RemoteDataSource()
        viewModel = ViewModelProvider(this, NewsDetailActivityViewModelFactory(repository))
            .get(NewsDetailViewModel::class.java)


        viewModel.setDisplayNews(args.news)

        moreNewsRv()
        //updateUi(args.news)
        viewModel.displayNews.observe(this, {news->
            binding.newsDetailRootScroll.smoothScrollTo(0,0)
            updateUi(news)
            newsAdapter.shuffle()
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

        val formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val publishedDate = LocalDateTime.parse(news.createdAt, formatter)
        val createdAt = "${publishedDate.dayOfMonth} ${publishedDate.month.name}, ${publishedDate
            .year} at ${publishedDate.hour}:${publishedDate.minute}"

        binding.detailNewsTime.text = createdAt

        Glide.with(this)
            .asBitmap()
            .load(news.defaultImage)
            .transition(BitmapTransitionOptions.withCrossFade())
            .into(binding.detailNewsCover)

        updateCategory(news.categoryId)
        updateUser(news.createdBy)

        val adSpacing = 2
        val adsOffset = 2
        var adcount = 0

        if(binding.detailNewsBodyLayout.childCount != 0)
            binding.detailNewsBodyLayout.removeAllViews()

        newsBodyList.forEach {
            if (it.isNotEmpty() && it.isNotBlank()) {
                val textView = NewsBodyLayoutBinding.inflate(LayoutInflater.from(this), null, false)
                textView.root.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(it)
                }
                binding.detailNewsBodyLayout.addView(textView.root)
            }
        }

        viewModel.advertisements.observe(this, {
            val ads = it.shuffled()
            val totalBlocks = newsBodyList.size
            var offsetCounter = 0
            if(totalBlocks >= adSpacing){
                for (i in 0..binding.detailNewsBodyLayout.childCount) {
                    Log.d("newsBodyAds", "i = $i")
                    Log.d("newsBodyAds", "childCount = ${binding.detailNewsBodyLayout.childCount}")

                    if(binding.detailNewsBodyLayout[i] !is LinearLayout){
                        ++offsetCounter
                    }

                    if(offsetCounter==adSpacing){
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
                                    .load(ads[adcount++].image)
                                    .into(view.addImage)
                                adsLayoutRoot.addView(view.root)
                                val space = Space(this)
                                space.layoutParams = ViewGroup.LayoutParams(24, MATCH_PARENT)
                                adsLayoutRoot.addView(space)
                            }
                        }

                        binding.detailNewsBodyLayout.addView(adsLayoutRoot, i+1)
                        offsetCounter = 0

                    }

                }
            }
        })
    }

    private fun updateUser(createdBy: String) {
        viewModel.getUser(createdBy)
        viewModel.createdBy.observe(this, {
            binding.detailNewsWriter.text = it.name
        })

    }

    private fun updateCategory(categoryId: String) {
        viewModel.getCategories(categoryId)
        viewModel.category.observe(this, {
            if(it != null){
                binding.newsDetailCat.categoryNameText.text = it.categoryName
            }
            else{
                binding.newsDetailCat.view.visibility = View.INVISIBLE
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

        viewModel.allCategory.observe(this, {
            newsAdapter.setCategoryList(it)
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