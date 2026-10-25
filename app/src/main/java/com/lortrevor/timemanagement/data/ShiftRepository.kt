package com.lortrevor.timemanagement.data

import com.lortrevor.timemanagement.model.Shift
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ShiftRepository(private val shiftDao: ShiftDao) {

    suspend fun addShift(shift: Shift) = shiftDao.addShift(shift.toEntity())

    suspend fun updateShift(shift: Shift) = shiftDao.updateShift(shift.toEntity())


    suspend fun deleteShift(shift: Shift) = shiftDao.deleteShift(shift.toEntity())

    fun getShiftsFromPeriod(dateStart: LocalDate, dateEnd: LocalDate): Flow<List<Shift>> =
        shiftDao.getShiftsFromPeriod(dateStart, dateEnd)
            .map { entities -> entities.map(ShiftEntity::toShift) }
}