package com.mdwc.attendancetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mdwc.attendancetracker.databinding.ActivityAddSubjectBinding
import com.mdwc.attendancetracker.models.HomeViewModel


class AddSubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubjectBinding
    private lateinit var mHomeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mHomeViewModel = ViewModelProvider(this)[HomeViewModel::class]
        binding.submitButton.setOnClickListener {
            insertData()
            onBackPressed()
        }
    }

    private fun insertData() {
        val subjectName=binding.subnameEditText.text.toString().trim()
        mHomeViewModel.addSubject(subjectName)
        Toast.makeText(this, "Added Subject", Toast.LENGTH_LONG).show()
    }
}