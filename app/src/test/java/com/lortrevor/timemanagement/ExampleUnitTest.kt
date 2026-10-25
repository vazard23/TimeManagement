package com.lortrevor.timemanagement.domain

import com.lortrevor.timemanagement.model.Shift
import com.lortrevor.timemanagement.model.ShiftType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class WorkHoursCalculatorTest {

    private val anyDate = LocalDate.of(2026, 10, 23)

    @Test
    fun `empty list gives zero`() {
        val result = calculateWorkMinutes(emptyList())

        assertEquals(WorkHoursTotal(0, 0), result)
    }

    @Test
    fun `mixed shifts are summed separately`() {
        val shifts = listOf(
            Shift(type = ShiftType.MORNING_LONG, date = anyDate),
            Shift(type = ShiftType.NIGHT, date = anyDate),
            Shift(type = ShiftType.NIGHT_LONG, date = anyDate)
        )

        val result = calculateWorkMinutes(shifts)

        // обычные: 7:45 + 5:55 + 5:55 = 19:35 = 1175 мин
        // переработка: 3:20 + 5:10 = 8:30 = 510 мин
        assertEquals(WorkHoursTotal(1175, 510), result)
    }

    @Test
    fun `every shift type has correct total paid time`() {
        // время смены минус неоплачиваемые паузы, посчитано вручную
        val expectedTotals = mapOf(
            ShiftType.MORNING to 465,      // 8:30 - 0:45
            ShiftType.EVENING to 465,      // 8:30 - 0:45
            ShiftType.NIGHT to 355,        // 6:30 - 0:35
            ShiftType.MORNING_LONG to 665, // 12:00 - 0:55
            ShiftType.EVENING_LONG to 665,
            ShiftType.NIGHT_LONG to 665
        )

        for ((type, expected) in expectedTotals) {
            val result = calculateWorkMinutes(listOf(Shift(type = type, date = anyDate)))
            assertEquals("Wrong total for $type", expected, result.regularMinutes + result.overtimeMinutes)
        }
    }
}