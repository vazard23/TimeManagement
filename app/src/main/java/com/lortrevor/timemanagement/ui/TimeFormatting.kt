package com.lortrevor.timemanagement.ui

fun Int.formatMinutes() : String {
    val hours = this/60
    val minutes = this % 60
    return "$hours ч $minutes м"
}