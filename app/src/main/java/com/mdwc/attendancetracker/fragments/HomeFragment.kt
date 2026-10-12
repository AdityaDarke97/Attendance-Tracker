package com.mdwc.attendancetracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdwc.attendancetracker.adapters.SubjectAdapter
import com.mdwc.attendancetracker.databinding.FragmentHomeBinding
import com.mdwc.attendancetracker.models.Subject
import io.paperdb.Paper

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var subjectAdapter: SubjectAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subjectAdapter = SubjectAdapter(emptyList())
        binding.subjectsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = subjectAdapter
        }

        loadSubjects()
    }
    override fun onResume() {
        super.onResume()
        loadSubjects()
    }
    private fun loadSubjects() {
        val subjects = Paper.book().read<List<Subject>>("MySubjects", emptyList())
        subjects?.let {
            subjectAdapter.updateData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}