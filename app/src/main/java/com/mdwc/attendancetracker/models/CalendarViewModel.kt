package com.mdwc.attendancetracker.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mdwc.attendancetracker.helpers.SubjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private val subjectId: Long =
        savedStateHandle["subjectId"] ?: -1L

    private val repository =
        SubjectRepository(AppDatabase.getInstance(app).attendanceDao())

    val recordsByDate: StateFlow<Map<Long, Attendance>> =
        repository.recordsForSubject(subjectId)
            .map { list -> list.associateBy { it.date } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun saveAttendance(date: LocalDate, isPresent: Boolean, lectureCount: Int) {
        viewModelScope.launch {
            repository.saveAttendance(
                Attendance(
                    subjectId = subjectId,
                    date = date.toEpochDay(),
                    isPresent = isPresent,
                    lectureCount = lectureCount
                )
            )
        }
    }

    fun clearAttendance(date: LocalDate) {
        viewModelScope.launch { repository.clearAttendance(subjectId, date.toEpochDay()) }
    }
}