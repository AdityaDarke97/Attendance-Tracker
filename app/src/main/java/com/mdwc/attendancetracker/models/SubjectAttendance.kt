package com.mdwc.attendancetracker.models

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectAttendance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)

@Entity(
    tableName = "attendance",
    foreignKeys = [
        ForeignKey(
            entity = SubjectAttendance::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["subjectId", "date"], unique = true)
    ]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val date: Long,
    val isPresent: Boolean,
    val lectureCount: Int
)