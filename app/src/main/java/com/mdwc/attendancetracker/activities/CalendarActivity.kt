package com.mdwc.attendancetracker.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.mdwc.attendancetracker.R
import com.mdwc.attendancetracker.databinding.ActivityCalendarBinding
import com.mdwc.attendancetracker.helpers.DayViewContainer
import com.mdwc.attendancetracker.models.Attendance
import com.mdwc.attendancetracker.models.CalendarViewModel
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.sequences.forEachIndexed

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private var records: Map<Long, Attendance> = emptyMap()
    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjectId = intent.getLongExtra("subjectId", -1L)
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
                text.setOnClickListener {
                    Toast.makeText(applicationContext,text.text,Toast.LENGTH_LONG).show()
                }
            }
        }

        var currentMonth = YearMonth.now()
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        //OnClickListeners
        binding.btnPrevMonth.setOnClickListener {
            binding.calendarView.smoothScrollToMonth(currentMonth.minusMonths(1))
            currentMonth=currentMonth.minusMonths(1)
        }

        binding.btnNextMonth.setOnClickListener {
            binding.calendarView.smoothScrollToMonth(currentMonth.plusMonths(1))
            currentMonth=currentMonth.plusMonths(1)
        }

        binding.calendarView.monthScrollListener = { month ->
            binding.monthTv.text = month.yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        }

        binding.weekdaysRow.children.forEachIndexed { i, view ->
            (view as TextView).text = daysOfWeek(firstDayOfWeek)[i]
                .getDisplayName(TextStyle.SHORT, Locale.getDefault())
        }

        binding.calendarView.setup(
            currentMonth.minusMonths(100),
            currentMonth.plusMonths(100),
            firstDayOfWeek
        )
        binding.calendarView.scrollToMonth(currentMonth)
    }
}