package com.lortrevor.timemanagement

import android.app.Application
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import com.lortrevor.timemanagement.data.AppDatabase
import com.lortrevor.timemanagement.data.ShiftRepository

class TimeManagementApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder<AppDatabase>(this, "time_management.db")
            .setDriver(AndroidSQLiteDriver())
            .build()
    }

    val repository by lazy {
        ShiftRepository(database.shiftDao())
    }

}