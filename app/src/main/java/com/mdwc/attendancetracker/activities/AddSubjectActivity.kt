package com.mdwc.attendancetracker.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mdwc.attendancetracker.databinding.ActivityAddSubjectBinding


class AddSubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}