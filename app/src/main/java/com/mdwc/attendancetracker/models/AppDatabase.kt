package com.mdwc.attendancetracker.models

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

@Database(
    entities = [SubjectAttendance::class, Attendance::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,
                    "attendance.db"
                )
                    .setDriver(BundledSQLiteDriver())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}