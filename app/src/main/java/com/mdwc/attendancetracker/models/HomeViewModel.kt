package com.mdwc.attendancetracker.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mdwc.attendancetracker.helpers.SubjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repository =
        SubjectRepository(AppDatabase.getInstance(app).attendanceDao())

    val subjects: StateFlow<List<SubjectWithStats>> =
        repository.subjectsWithStats
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addSubject(name: String) {
        if (name.isBlank()) return        // ignore empty names
        viewModelScope.launch { repository.addSubject(name) }
    }
}