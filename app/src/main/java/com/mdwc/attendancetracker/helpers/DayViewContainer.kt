package com.mdwc.attendancetracker.helpers

import android.view.View
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer
import com.mdwc.attendancetracker.databinding.ItemCalendarDayBinding

class DayViewContainer(view: View) : ViewContainer(view) {
    val binding = ItemCalendarDayBinding.bind(view)
    lateinit var day: CalendarDay      // set in bind(), read by the click listener

    init {
        view.setOnClickListener {
            // TODO: if (day.position == DayPosition.MonthDate) → open the dialog for day.date
        }
    }

}
