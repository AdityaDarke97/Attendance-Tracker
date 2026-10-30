package com.mdwc.attendancetracker.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.MonthDayBinder
import com.mdwc.attendancetracker.R
import com.mdwc.attendancetracker.databinding.ActivityCalendarBinding
import com.mdwc.attendancetracker.databinding.AttendanceInputLayoutBinding
import com.mdwc.attendancetracker.helpers.DayViewContainer
import com.mdwc.attendancetracker.models.Attendance
import com.mdwc.attendancetracker.models.CalendarViewModel
import kotlinx.coroutines.launch
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.sequences.forEachIndexed
import androidx.core.view.isVisible
import java.time.LocalDate

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private var records: Map<Long, Attendance> = emptyMap()
    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recordsByDate.collect { newMap ->
                    records = newMap
                    binding.calendarView.notifyCalendarChanged()
                }
            }
        }

        val subjectId = intent.getLongExtra("subjectId", -1L)
        val subjectName = intent.getStringExtra("subjectName")

        if (subjectId == -1L) {
            finish()
            return
        }

        //top text view
        binding.subjectTv.setText(subjectName)
        binding.back.setOnClickListener {
            onBackPressed()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //calendar view logic
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {

            override fun create(view: View) = DayViewContainer(view)

            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val text = container.binding.tvDayNumber
                text.text = data.date.dayOfMonth.toString()

                val isToday = data.date == LocalDate.now()

                container.binding.dayRoot.foreground = if (isToday) {
                    ContextCompat.getDrawable(this@CalendarActivity, R.drawable.bg_today)
                } else {
                    null
                }


                if (data.position != DayPosition.MonthDate) {
                    text.visibility = View.INVISIBLE
                    container.binding.dayRoot.setOnClickListener(null)
                    container.binding.tvLectureCount.visibility=View.GONE
                    return
                }

                text.visibility = View.VISIBLE


                //bottomsheetdialog
                container.binding.dayRoot.setOnClickListener {
                    val dialog = BottomSheetDialog(this@CalendarActivity)
                    val sheetBinding = AttendanceInputLayoutBinding.inflate(layoutInflater)

                    val formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy", Locale.getDefault())

                    val attendanceRecord = records[data.date.toEpochDay()]
                    var count = attendanceRecord?.lectureCount ?: 1

                    if (attendanceRecord == null) {
                        sheetBinding.lecCount.text = count.toString()
                        sheetBinding.clearButton.visibility = View.GONE
                        sheetBinding.toggleButtonGroup.check(R.id.present_btn)
                    } else {
                        sheetBinding.lecCount.text = count.toString()
                        if (attendanceRecord.isPresent) {
                            sheetBinding.toggleButtonGroup.check(R.id.present_btn)
                        } else {
                            sheetBinding.toggleButtonGroup.check(R.id.absent_btn)
                        }
                    }

                    // increment/decrement logic
                    sheetBinding.incrementLecCount.setOnClickListener {
                        if (count >= 10) {
                            Toast.makeText(
                                this@CalendarActivity,
                                "Lecture Count can't be greater than 10!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            count++
                            sheetBinding.lecCount.text = count.toString()
                        }
                    }
                    sheetBinding.decrementLecCount.setOnClickListener {
                        if (count == 1) {
                            Toast.makeText(
                                this@CalendarActivity,
                                "Lecture Count can't be less than 1",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            count--
                            sheetBinding.lecCount.text = count.toString()
                        }
                    }

                    sheetBinding.selectedDate.text = data.date.format(formatter)
                    dialog.setContentView(sheetBinding.root)
                    dialog.show()

                    sheetBinding.saveButton.setOnClickListener {
                        if (sheetBinding.toggleButtonGroup.checkedButtonId == R.id.present_btn) {
                            viewModel.saveAttendance(data.date, true, count)
                            dialog.dismiss()
                        } else {
                            viewModel.saveAttendance(data.date, false, count)
                            dialog.dismiss()
                        }
                    }
                    sheetBinding.clearButton.setOnClickListener {
                        viewModel.clearAttendance(data.date)
                        dialog.dismiss()
                    }
                }

                val record = records[data.date.toEpochDay()]
                if(record==null){
                    container.binding.tvLectureCount.visibility=View.GONE
                }
                else{
                    container.binding.tvLectureCount.visibility=View.VISIBLE
                    container.binding.tvLectureCount.text=record.lectureCount.toString()
                }
                container.binding.dayRoot.setBackgroundResource(
                    when {
                        record == null -> {
                            R.drawable.day_border_none
                        }

                        record.isPresent -> {
                            R.drawable.bg_present
                        }

                        else -> {
                            R.drawable.bg_absent
                        }
                    }
                )
            }
        }

        var currentMonth = YearMonth.now()
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        // next/prev month onclicklisteners
        binding.btnPrevMonth.setOnClickListener {
            binding.calendarView.smoothScrollToMonth(currentMonth.minusMonths(1))
        }
        binding.btnNextMonth.setOnClickListener {
            binding.calendarView.smoothScrollToMonth(currentMonth.plusMonths(1))
        }

        binding.calendarView.monthScrollListener = { month ->
            currentMonth = month.yearMonth
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