package com.sushmoyr.ajkalnewyork

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun main() {
    val item1: ParentClass = ParentClass.ChildClass1("12", "my name")
    val item2: ParentClass = ParentClass.ChildClass1("12", "my name")


    if(item1 == item2) {
        println("They are same")
    }
    else {
        println("They are different")
    }
    if (item1 === item2){
        println("Triple same")
    }
    else {
        println("Triple different")
    }
}

sealed class ParentClass {
    data class ChildClass1(
        val id: String,
        val name: String,
    ): ParentClass()

    data class ChildClass2(
        val id: String,
        val name: String,
        val age: Int
    ): ParentClass()

}