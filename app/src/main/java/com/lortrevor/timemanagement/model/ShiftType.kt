package com.lortrevor.timemanagement.model

/** Every shift is associated with the day of it's beginning, except NIGHT and NIGHT_LONG */
enum class ShiftType(val shiftName: String, val regularMinutes: Int, val overtimeMinutes: Int) {
    MORNING("Morning", 465, 0),
    MORNING_LONG("Morning Long", 465, 200),
    EVENING("Evening", 465, 0),
    EVENING_LONG("Evening Long", 465, 200),
    NIGHT("Night", 355, 0),
    NIGHT_LONG("Night Long", 355, 310)
}