package com.sushmoyr.ajkalnewyork

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun main(){
    val date1 = LocalDateTime.now()
    val date2 = LocalDateTime.now().plusDays(3L)
    println("date1 = $date1")
    println("date1 = $date2")
    val formattedDate1 = date1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val formattedDate2 = date2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    println("Formatted 1 = $formattedDate1")
    println("Formatted 2 = $formattedDate2")
    val diff: Long = date2.toInstant(ZoneOffset.UTC).toEpochMilli() -
            date1.toInstant(ZoneOffset.UTC).toEpochMilli()
    println(ZoneId.systemDefault().id)
    val seconds = diff / 1000
    val minutes = (diff / 1000) / 60
    val hours = ((diff / 1000) / 60) / 60
    val days = (((diff / 1000) / 60) / 60) / 24
    println("days = $days")
}