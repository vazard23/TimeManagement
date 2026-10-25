package com.lortrevor.timemanagement.model

import java.time.LocalDate

data class Shift(val id: Long = 0, val type: ShiftType, val date: LocalDate)
