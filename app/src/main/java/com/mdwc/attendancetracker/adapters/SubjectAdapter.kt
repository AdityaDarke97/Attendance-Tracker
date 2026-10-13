package com.mdwc.attendancetracker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mdwc.attendancetracker.databinding.SubjectsCardBinding
import com.mdwc.attendancetracker.models.SubjectWithStats
import kotlin.math.roundToInt

class SubjectAdapter(
    private val onClick: (SubjectWithStats) -> Unit
) : ListAdapter<SubjectWithStats, SubjectAdapter.VH>(DIFF) {

    class VH(val binding: SubjectsCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(SubjectsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            idx.text = (position + 1).toString()
            subName.text = item.name
            percentage.text =
                if (item.total == 0) "--"
                else "${(item.attended * 100f / item.total).roundToInt()}%"
            root.setOnClickListener { onClick(item) }
        }
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<SubjectWithStats>() {
            override fun areItemsTheSame(a: SubjectWithStats, b: SubjectWithStats) = a.id == b.id
            override fun areContentsTheSame(a: SubjectWithStats, b: SubjectWithStats) = a == b
        }
    }
}