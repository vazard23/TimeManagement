package com.lortrevor.timemanagement.ui.calendar

import java.time.LocalDate
import java.time.YearMonth

fun YearMonth.toCalendarCells(): List<LocalDate?> {
    val emptyCells = atDay(1).dayOfWeek.value - 1

    val emptyCellsListAtBeginning = List(emptyCells) { null }
    val filledCellsList = (1..lengthOfMonth()).map { atDay(it) }
    val listSize = emptyCellsListAtBeginning.size + filledCellsList.size
    val endEmptyCells = (7 - listSize % 7) % 7
    val emptyCellsListAtEnd = List(endEmptyCells) { null }

    return emptyCellsListAtBeginning + filledCellsList + emptyCellsListAtEnd
}