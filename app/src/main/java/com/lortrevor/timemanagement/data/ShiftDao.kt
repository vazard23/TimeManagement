package com.lortrevor.timemanagement.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ShiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShift(shift: ShiftEntity)

    @Update
    suspend fun updateShift(shift: ShiftEntity)

    @Delete
    suspend fun deleteShift(shift: ShiftEntity)

    @Query("SELECT * FROM shifts WHERE date BETWEEN :dateStart AND :dateEnd ORDER BY date ASC")
    fun getShiftsFromPeriod(dateStart: LocalDate, dateEnd: LocalDate): Flow<List<ShiftEntity>>

}