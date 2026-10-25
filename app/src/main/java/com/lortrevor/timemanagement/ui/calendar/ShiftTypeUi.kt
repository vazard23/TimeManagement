package com.lortrevor.timemanagement.ui.calendar

import androidx.compose.ui.graphics.Color
import com.lortrevor.timemanagement.model.ShiftType

fun ShiftType.label(): String = when (this) {
    ShiftType.MORNING -> "У"
    ShiftType.EVENING -> "В"
    ShiftType.NIGHT -> "Н"
    ShiftType.MORNING_LONG -> "У12"
    ShiftType.EVENING_LONG -> "В12"
    ShiftType.NIGHT_LONG -> "Н12"
}

fun ShiftType.color(): Color =
    if (overtimeMinutes > 0) Color(0xFFEF9A9A) else Color(0xFFFFE082)