package com.mdwc.attendancetracker.models

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

data class SubjectWithStats(
    val id: Long,
    val name: String,
    val attended: Int,
    val total: Int
)

@Dao
interface AttendanceDao {

    @Insert
    suspend fun insertSubject(subject: SubjectAttendance): Long

    @Delete
    suspend fun deleteSubject(subject: SubjectAttendance)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttendance(record: Attendance)

    @Query("SELECT * FROM attendance WHERE subjectId = :subjectId")
    fun getRecordsForSubject(subjectId: Long): Flow<List<Attendance>>

    @Query("DELETE FROM attendance WHERE subjectId = :subjectId AND date = :date")
    suspend fun deleteAttendance(subjectId: Long, date: Long)

    @Query("""
        SELECT s.id AS id,
               s.name AS name,
               COALESCE(SUM(CASE WHEN a.isPresent = 1 THEN a.lectureCount END), 0) AS attended,
               COALESCE(SUM(a.lectureCount), 0) AS total
        FROM subjects s
        LEFT JOIN attendance a ON a.subjectId = s.id
        GROUP BY s.id
    """)
    fun getSubjectsWithStats(): Flow<List<SubjectWithStats>>
}