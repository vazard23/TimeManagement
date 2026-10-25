package com.lortrevor.timemanagement.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class TimeFormattingTest {

    @Test
    fun formatsHoursAndMinutes() {
        assertEquals("116 ч 15 м", 6975.formatMinutes())
    }

    @Test
    fun formatsWholeHours() {
        assertEquals("2 ч 0 м", 120.formatMinutes())
    }

    @Test
    fun formatsLessThanHour() {
        assertEquals("0 ч 45 м", 45.formatMinutes())
    }

    @Test
    fun formatsZero() {
        assertEquals("0 ч 0 м", 0.formatMinutes())
    }
}