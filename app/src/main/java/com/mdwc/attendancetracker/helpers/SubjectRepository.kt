package com.mdwc.attendancetracker.helpers

import com.mdwc.attendancetracker.models.Attendance
import com.mdwc.attendancetracker.models.AttendanceDao
import com.mdwc.attendancetracker.models.SubjectAttendance
import com.mdwc.attendancetracker.models.SubjectWithStats
import kotlinx.coroutines.flow.Flow

class SubjectRepository(private val dao: AttendanceDao) {

    // Live list for the home screen. Re-emits when data changes.
    val subjectsWithStats: Flow<List<SubjectWithStats>> = dao.getSubjectsWithStats()

    suspend fun addSubject(name: String): Long =
        dao.insertSubject(SubjectAttendance(name = name.trim()))

    suspend fun deleteSubject(subject: SubjectAttendance) = dao.deleteSubject(subject)

    fun recordsForSubject(subjectId: Long): Flow<List<Attendance>> =
        dao.getRecordsForSubject(subjectId)

    suspend fun saveAttendance(record: Attendance) = dao.upsertAttendance(record)

    suspend fun clearAttendance(subjectId: Long, date: Long) = dao.deleteAttendance(subjectId, date)
}