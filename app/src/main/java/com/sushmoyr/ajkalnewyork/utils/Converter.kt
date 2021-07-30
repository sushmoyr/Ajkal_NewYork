package com.sushmoyr.ajkalnewyork.utils

import com.sushmoyr.ajkalnewyork.models.DataModel
import com.sushmoyr.ajkalnewyork.models.News

fun toNewsList(news: List<DataModel.News>): List<News>{
    val data = mutableListOf<News>()
    news.forEach {
        data.add(it.toNews())
    }
    return data
}