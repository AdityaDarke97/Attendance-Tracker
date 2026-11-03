package com.mdwc.attendancetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun insertData() {
        val subjectName=binding.subnameEditText.text.toString().trim()
        val targetInp=binding.targetEditText.text.toString().toInt()

        mHomeViewModel.addSubject(subjectName, targetInp)
        Toast.makeText(this, "Added Subject", Toast.LENGTH_LONG).show()
    }
}