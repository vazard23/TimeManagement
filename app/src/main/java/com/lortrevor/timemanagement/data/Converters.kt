package com.lortrevor.timemanagement.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters{
    @TypeConverter
    fun localDateToString(date: LocalDate): String = date.toString()
    @TypeConverter
    fun stringToLocalDate(text: String): LocalDate = LocalDate.parse(text)

}