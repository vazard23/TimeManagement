package com.lortrevor.timemanagement.domain

import com.lortrevor.timemanagement.model.Shift

data class WorkHoursTotal(val regularMinutes: Int, val overtimeMinutes: Int)


fun calculateWorkMinutes(shifts: List<Shift>) : WorkHoursTotal{
    var regularMinutes = 0
    var overtimeMinutes = 0
    for (shift in shifts){
        regularMinutes += shift.type.regularMinutes
        overtimeMinutes += shift.type.overtimeMinutes
    }
    return WorkHoursTotal(regularMinutes, overtimeMinutes)
}