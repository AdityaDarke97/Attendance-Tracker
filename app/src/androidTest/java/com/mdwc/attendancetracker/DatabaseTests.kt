package com.mdwc.attendancetracker

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mdwc.attendancetracker.models.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class AttendanceDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: AttendanceDao

    private fun day(d: Int) = LocalDate.of(2026, 10, d).toEpochDay()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder<AppDatabase>(context)
            .setDriver(BundledSQLiteDriver())
            .build()
        dao = db.attendanceDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun savingSameDateTwiceKeepsOneRow() = runTest {
        val mathsId = dao.insertSubject(SubjectAttendance(name = "Maths"))

        dao.upsertAttendance(Attendance(subjectId = mathsId, date = day(10), isPresent = true, lectureCount = 2))
        dao.upsertAttendance(Attendance(subjectId = mathsId, date = day(10), isPresent = false, lectureCount = 2))

        val records = dao.getRecordsForSubject(mathsId).first()

        assertEquals(1, records.size)
        assertEquals(false, records[0].isPresent)
    }

    @Test
    fun statsAreCalculatedCorrectly() = runTest {
        val mathsId = dao.insertSubject(SubjectAttendance(name = "Maths"))
        val physicsId = dao.insertSubject(SubjectAttendance(name = "Physics"))

        dao.upsertAttendance(Attendance(subjectId = mathsId, date = day(10), isPresent = true, lectureCount = 2))
        dao.upsertAttendance(Attendance(subjectId = mathsId, date = day(11), isPresent = false, lectureCount = 1))
        dao.upsertAttendance(Attendance(subjectId = mathsId, date = day(12), isPresent = true, lectureCount = 2))
        dao.upsertAttendance(Attendance(subjectId = physicsId, date = day(12), isPresent = true, lectureCount = 2))
        dao.upsertAttendance(Attendance(subjectId = physicsId, date = day(13), isPresent = false, lectureCount = 1))

        val stats = dao.getSubjectsWithStats().first()
        val maths = stats.first { it.name == "Maths" }
        val physics = stats.first { it.name == "Physics" }

        assertEquals(4, maths.attended)
        assertEquals(5, maths.total)
        assertEquals(2, physics.attended)
        assertEquals(3, physics.total)
    }
}