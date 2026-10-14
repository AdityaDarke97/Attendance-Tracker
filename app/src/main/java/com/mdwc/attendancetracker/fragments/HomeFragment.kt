package com.mdwc.attendancetracker.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdwc.attendancetracker.activities.CalendarActivity
import com.mdwc.attendancetracker.adapters.SubjectAdapter
import com.mdwc.attendancetracker.databinding.FragmentHomeBinding
import com.mdwc.attendancetracker.models.HomeViewModel
import com.mdwc.attendancetracker.models.SubjectWithStats
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHomeBinding.bind(view)

        val adapter = SubjectAdapter { subject ->
            val intent = Intent(requireContext(), CalendarActivity::class.java).apply {
                putExtra("subjectId", subject.id)
                putExtra("subjectName", subject.name)
            }
            startActivity(intent)
        }
        binding.subjectsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.subjectsRecycler.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.subjects.collect { adapter.submitList(it) }
            }
        }
    }
}