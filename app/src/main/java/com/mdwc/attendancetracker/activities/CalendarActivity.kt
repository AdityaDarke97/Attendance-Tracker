package com.mdwc.attendancetracker.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.mdwc.attendancetracker.R
import com.mdwc.attendancetracker.databinding.ActivityCalendarBinding
import com.mdwc.attendancetracker.helpers.DayViewContainer
import com.mdwc.attendancetracker.models.Attendance
import com.mdwc.attendancetracker.models.CalendarViewModel
import java.time.YearMonth

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private var records: Map<Long, Attendance> = emptyMap()
    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjectId = intent.getLongExtra("subjectId",-1L)
        val subjectName = intent.getStringExtra("subjectName")

        if (subjectId == -1L) {
            finish()
            return
        }

        binding.heyWelcomeTv.setText(subjectName)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val text = container.binding.tvDayNumber
                text.text = data.date.dayOfMonth.toString()

                if (data.position != DayPosition.MonthDate) {
                    text.visibility = View.INVISIBLE
                    return
                }
                text.visibility = View.VISIBLE

                val record = records[data.date.toEpochDay()]
                text.setBackgroundResource(
                    when {
                        record == null -> R.drawable.day_border_none
                        record.isPresent -> R.drawable.bg_present
                        else -> R.drawable.bg_absent
                    }
                )
            }

        }

        val currentMonth = YearMonth.now()
        binding.calendarView.setup(
            currentMonth.minusMonths(12),
            currentMonth.plusMonths(1),
            firstDayOfWeekFromLocale()
        )
        binding.calendarView.scrollToMonth(currentMonth)
    }
}