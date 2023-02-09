package com.example.sololearnclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.data.Course
import com.example.sololearnclone.R
import com.example.sololearnclone.databinding.ItemCourseBinding

class HomeAdapter(private val courses: List<Course>, private val navController: NavController?):
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = courses[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            navController?.navigate(R.id.action_HomeFragment_to_SectionFragment)
        }
    }

    override fun getItemCount(): Int = courses.size

    class ViewHolder(binding: ItemCourseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imgCourse = binding.imgCourse
        fun bind(item: Course) {
            item.courseImage?.let { imgCourse.setBackgroundResource(it) }
        }
    }
}