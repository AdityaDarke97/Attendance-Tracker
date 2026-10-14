package com.mdwc.attendancetracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mdwc.attendancetracker.R
import com.mdwc.attendancetracker.databinding.ActivityCalendarBinding
import com.mdwc.attendancetracker.databinding.ActivityLandingBinding
import com.mdwc.attendancetracker.databinding.FragmentHomeBinding

class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subjectId = intent.getLongExtra("subjectId",-1L)
        val subjectName = intent.getStringExtra("subjectName")

        if (subjectId == -1L) {
            finish()   // opened without an ID, so there's nothing to show
            return
        }

        binding.heyWelcomeTv.setText(subjectName)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
}