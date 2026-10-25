package com.lortrevor.timemanagement.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lortrevor.timemanagement.model.Shift
import com.lortrevor.timemanagement.model.ShiftType
import java.time.LocalDate

@Entity(tableName = "shifts", indices = [Index(value = ["date"], unique = true)])
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: ShiftType,
    val date: LocalDate
)

fun ShiftEntity.toShift(): Shift = Shift(id = id, type = type, date = date)
fun Shift.toEntity(): ShiftEntity = ShiftEntity(id = id, type = type, date = date)
