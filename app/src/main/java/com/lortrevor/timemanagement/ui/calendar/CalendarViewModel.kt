package com.lortrevor.timemanagement.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lortrevor.timemanagement.TimeManagementApplication
import com.lortrevor.timemanagement.data.ShiftRepository
import com.lortrevor.timemanagement.domain.WorkHoursTotal
import com.lortrevor.timemanagement.domain.calculateWorkMinutes
import com.lortrevor.timemanagement.model.Shift
import com.lortrevor.timemanagement.model.ShiftType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class CalendarViewModel(private val repository: ShiftRepository) : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val shifts: StateFlow<List<Shift>> = _currentMonth
        .flatMapLatest { month ->
            repository.getShiftsFromPeriod(
                month.atDay(1),
                month.atEndOfMonth()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totals: StateFlow<WorkHoursTotal> = shifts
        .map ( ::calculateWorkMinutes )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = WorkHoursTotal(regularMinutes = 0, overtimeMinutes = 0)
        )

    fun nextMonth() {
        _currentMonth.update { month -> month.plusMonths(1) }
    }

    fun previousMonth() {
        _currentMonth.update { month -> month.minusMonths(1) }
    }

    fun addShift(date: LocalDate, shiftType: ShiftType) {
        viewModelScope.launch { repository.addShift(Shift(type = shiftType, date = date)) }
    }

    fun deleteShift(shift: Shift) {
        viewModelScope.launch { repository.deleteShift(shift) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as TimeManagementApplication
                CalendarViewModel(application.repository)
            }
        }
    }

}