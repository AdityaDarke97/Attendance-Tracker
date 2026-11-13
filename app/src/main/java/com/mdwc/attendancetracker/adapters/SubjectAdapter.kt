package com.mdwc.attendancetracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.mdwc.attendancetracker.R
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
            subName.text = item.name

            val pct = if (item.total == 0) null else item.attended * 100f / item.total

            if (pct == null) {
                percentage.text = "0%"
            } else {
                if (pct >= item.target) {
                    percentageCircle.progressBarColor= getColor(holder.itemView.context,R.color.green_present)
                }
                else{
                    percentageCircle.progressBarColor= getColor(holder.itemView.context,R.color.red_absent)
                }
                percentage.text = "${pct.roundToInt()}%"
                percentageCircle.visibility = View.VISIBLE
                percentageCircle.setProgressWithAnimation(pct,500)
            }


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