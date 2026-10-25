package com.lortrevor.timemanagement.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lortrevor.timemanagement.domain.WorkHoursTotal
import com.lortrevor.timemanagement.model.Shift
import com.lortrevor.timemanagement.model.ShiftType
import com.lortrevor.timemanagement.ui.formatMinutes
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.Factory)
) {

    val currentMonth by viewModel.currentMonth.collectAsStateWithLifecycle()
    val shifts by viewModel.shifts.collectAsStateWithLifecycle()
    val totals by viewModel.totals.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { viewModel.previousMonth() }) {
                Text("←")
            }

            Text(style = MaterialTheme.typography.titleLarge, text = currentMonth.toDisplayString())

            Button(onClick = { viewModel.nextMonth() }) {
                Text("→")
            }
        }

        CalendarGrid(
            month = currentMonth, shifts = shifts, onDayClick = { date -> selectedDate = date })

        MonthTotals(totals = totals)

        val date = selectedDate
        if (date != null) {
            val shift = shifts.find { it.date == date }
            DayDialog(
                date = date,
                shift = shift,
                onSelectType = { type ->
                    viewModel.addShift(date = date, shiftType = type)
                    selectedDate = null
                },
                onDelete = {
                    if (shift != null) viewModel.deleteShift(shift)
                    selectedDate = null
                },
                onDismiss = { selectedDate = null },
            )
        }
    }
}

private fun YearMonth.toDisplayString(): String {
    val monthName = month.getDisplayName(
        TextStyle.FULL_STANDALONE, Locale.getDefault()
    ).replaceFirstChar { it.titlecase() }

    return "$monthName $year"
}

@Composable
private fun WeekdayHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        for (day in DayOfWeek.entries) {
            Text(
                text = day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.getDefault()),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    month: YearMonth,
    shifts: List<Shift>,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shiftsByDate = shifts.associateBy { it.date }

    Column(modifier = modifier) {
        WeekdayHeader()

        for (week in month.toCalendarCells().chunked(7)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (date in week) {
                    DayCell(
                        date = date,
                        shift = shiftsByDate[date],
                        onClick = { if (date != null) onDayClick(date) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}


@Composable
private fun DayCell(
    date: LocalDate?, shift: Shift?, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val backgroundColor = shift?.type?.color() ?: Color.Transparent


    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(enabled = date != null, onClick = onClick)
            .padding(2.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (date != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = date.dayOfMonth.toString())
                if (shift != null) {
                    Text(text = shift.type.label())
                }
            }
        }
    }
}

@Composable
private fun MonthTotals(totals: WorkHoursTotal, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Обычные часы: ${totals.regularMinutes.formatMinutes()}")
        Text(text = "Сверхурочные: ${totals.overtimeMinutes.formatMinutes()}")
    }
}

@Composable
private fun DayDialog(
    date: LocalDate,
    shift: Shift?,
    onSelectType: (ShiftType) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = date.toString()) },
        text = {
            Column {
                Text(text = shift?.type?.shiftName ?: "Выходной")

                for (row in ShiftType.entries.chunked(3)) {
                    Row {
                        for (type in row) {
                            TextButton(onClick = { onSelectType(type) }) {
                                Text(text = type.label())
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Закрыть") }
        },
        dismissButton = {
            if (shift != null) {
                TextButton(onClick = onDelete) { Text("Удалить") }
            }
        },
    )
}

