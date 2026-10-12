package com.mdwc.attendancetracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mdwc.attendancetracker.databinding.ActivityAddSubjectBinding
import com.mdwc.attendancetracker.models.Subject
import io.paperdb.Paper


class AddSubjectActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddSubjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val subjectName = binding.subnameEditText.text.toString().trim()

            if (subjectName.isNotEmpty()) {
                val newSubject = Subject(name = subjectName)

                val existingSubjects = Paper.book().read<MutableList<Subject>>("MySubjects", mutableListOf())

                existingSubjects?.add(newSubject)
                Paper.book().write<MutableList<Subject>>("MySubjects", existingSubjects as MutableList<Subject>)

                Toast.makeText(this, "Subject Added!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                binding.subnameEditText.error = "Please enter a subject name"
            }
        }
    }
}