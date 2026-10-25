package com.lortrevor.timemanagement.ui.calendar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class CalendarCellsTest {

    @Test
    fun `october 2026 starts on thursday`() {

        val month = YearMonth.of(2026, 10)


        val cells = month.toCalendarCells()

        assertEquals(35, cells.size)
        assertTrue(cells.take(3).all { it == null })
        assertEquals(LocalDate.of(2026, 10, 1), cells[3])
        assertTrue(cells.takeLast(1).all {it == null})
    }

    @Test
    fun `june 2026 starts on monday`() {

        val month = YearMonth.of(2026, 6)

        val cells = month.toCalendarCells()

        assertEquals(35, cells.size)
        assertTrue(cells.take(0).all { it == null })
        assertEquals(LocalDate.of(2026, 6, 1), cells[0])
        assertTrue(cells.takeLast(5).all {it == null})
    }

    @Test
    fun `march 2026 starts on sunday`() {

        val month = YearMonth.of(2026, 3)

        val cells = month.toCalendarCells()

        assertEquals(42, cells.size)
        assertTrue(cells.take(6).all { it == null })
        assertEquals(LocalDate.of(2026, 3, 1), cells[6])
        assertTrue(cells.takeLast(5).all {it == null})
    }
}