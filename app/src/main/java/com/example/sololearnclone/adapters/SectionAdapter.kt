package com.example.sololearnclone.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.sololearnclone.R
import com.example.sololearnclone.data.Section
import com.example.sololearnclone.databinding.ItemSectionBinding


class SectionAdapter(
    private val context: Context,
    private val sections: List<Section>,
    private val navController: NavController?,
) :
    RecyclerView.Adapter<SectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = sections[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            if ((position > 0 && sections[position - 1].sectionImage == R.drawable.ic_green_check)
                || position == 0
            ) {
                val bundle = Bundle()
                bundle.putString("lessonName", item.sectionName.toString())
                val action = R.id.action_SectionFragment_to_LessonFragment
                navController?.navigate(action, bundle)
            } else {
                Toast.makeText(
                    context,
                    "You must do all before ${sections[position].sectionName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount(): Int = sections.size

    class ViewHolder(binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvSection = binding.tvSection
        private val imgSection = binding.imgTask
        fun bind(item: Section) {
            tvSection.text = item.sectionName
            item.sectionImage?.let { imgSection.setImageResource(item.sectionImage) }
        }
    }
}